import { Component, OnInit, ChangeDetectorRef, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Page, DestinationDto } from '../../../shared/models/app.models';
import { DestinationService } from '../../../core/services/destination';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.scss'
})
export class AdminDashboardComponent implements OnInit {
  username: string = 'Admin';
  adminSearchCountry: string = '';
  statusMessage: string = '';
  suggestedDestinations: DestinationDto[] = [];
  savedDestinations: DestinationDto[] = [];
  currentPage: number = 0;
  pageSize: number = 12;
  totalPages: number = 0;
  totalElements: number = 0;

  constructor(
    private destinationService: DestinationService,
    private cdr: ChangeDetectorRef,
    @Inject(PLATFORM_ID) private platformId: Object,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      const userString = localStorage.getItem('current_user');
      if (userString) {
        this.username = JSON.parse(userString).username;
      }
    }
    this.loadCurrentDestinations(0);
  }

  loadCurrentDestinations(page: number = 0, clearSearch: boolean = false): void {
    if (clearSearch) {
      this.adminSearchCountry = '';
    }

    this.destinationService.searchDestinations(this.adminSearchCountry, page, this.pageSize).subscribe({
      next: (data: Page<DestinationDto>) => {
        this.savedDestinations = data.content;
        this.currentPage = data.number;
        this.totalPages = data.totalPages;
        this.totalElements = data.totalElements;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Failed to parse database records:', err)
    });
  }

  fetchAdminSuggestion(): void {
    if (!this.adminSearchCountry.trim()) return;
    this.statusMessage = 'Searching third-party API configurations...';
    this.suggestedDestinations = [];

    this.destinationService.fetchSuggestions(this.adminSearchCountry).subscribe({
      next: (backendResponse) => {
        if (backendResponse && backendResponse.suggestions && backendResponse.suggestions.length > 0) {
          this.suggestedDestinations = backendResponse.suggestions;
          this.statusMessage = `Found ${this.suggestedDestinations.length} matching destinations.`;
        } else {
          this.suggestedDestinations = [];
          this.statusMessage = 'No recommendations matching country found.';
        }
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.statusMessage = 'Failed to fetch third-party API configurations.';
        this.cdr.detectChanges();
      }
    });
  }

  addSingleToDb(dest: DestinationDto): void {
    this.destinationService.createDestination(dest).subscribe({
      next: () => {
        this.statusMessage = `✅ ${dest.country} saved successfully!`;
        this.suggestedDestinations = this.suggestedDestinations.filter(d => d.country !== dest.country);
        this.loadCurrentDestinations(0, true);
      },
      error: (err) => {
        console.error('Failed to add destination:', err);
        this.statusMessage = 'Failed to save configuration element to database.';
        this.cdr.detectChanges();
      }
    });
  }

  bulkAddAll(): void {
    if (this.suggestedDestinations.length === 0) return;
    this.statusMessage = 'Executing transactional bulk save...';

    this.destinationService.createDestinationsBulk(this.suggestedDestinations).subscribe({
      next: () => {
        this.statusMessage = `✅ Successfully bulk-added ${this.suggestedDestinations.length} destinations!`;
        this.suggestedDestinations = [];
        this.loadCurrentDestinations(0, true);
      },
      error: (err) => {
        console.error('Transactional batch insert failed:', err);
        this.statusMessage = 'Failed to execute transactional bulk save.';
        this.cdr.detectChanges();
      }
    });
  }

  deleteDestination(id: number | undefined): void {
    if (!id) return;

    if (confirm('Permanently remove this destination configuration?')) {
      this.destinationService.deleteDestination(id).subscribe({
        next: () => {
          if (this.savedDestinations.length === 1 && this.currentPage > 0) {
            this.loadCurrentDestinations(this.currentPage - 1);
          } else {
            this.loadCurrentDestinations(this.currentPage);
          }
        },
        error: (err) => {
          console.error('Cascade validation constraint failure:', err);
          alert('Could not remove destination. It might be linked to a user\'s visit list.');
        }
      });
    }
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.loadCurrentDestinations(this.currentPage + 1);
    }
  }

  prevPage(): void {
    if (this.currentPage > 0) {
      this.loadCurrentDestinations(this.currentPage - 1);
    }
  }

  logout(): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('current_user');
      localStorage.removeItem('jwt_token');
    }
    this.router.navigate(['/login']);
  }
}
