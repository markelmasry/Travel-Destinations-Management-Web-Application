import { Component, OnInit, Inject, PLATFORM_ID, ChangeDetectorRef, OnDestroy } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { DestinationService } from '../../../core/services/destination';
import { WantVisitService } from '../../../core/services/want-visit';
import { DestinationDto, WantVisitResponse, Page } from '../../../shared/models/app.models';

import { Subject, Subscription } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

@Component({
  selector: 'app-user-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-dashboard.html',
  styleUrls: ['./user-dashboard.scss']
})
export class UserDashboardComponent implements OnInit, OnDestroy {
  username: string = 'Explorer';
  userId!: number;

  allDestinations: DestinationDto[] = [];
  savedDestinations: DestinationDto[] = [];
  displayedDestinations: DestinationDto[] = [];

  savedVisitsMap: Map<number, number> = new Map<number, number>();
  searchQuery: string = '';
  viewMode: 'all' | 'saved' = 'all';
  isProcessingVisit: boolean = false;

  currentPage: number = 0;
  pageSize: number = 12;
  totalPages: number = 0;
  currentSavedPage: number = 0;

  searchSubject: Subject<string> = new Subject<string>();
  private searchSubscription!: Subscription;

  constructor(
    private destinationService: DestinationService,
    private wantVisitService: WantVisitService,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      this.extractUserInfo();

      if (this.userId) {
        this.loadUserVisitList();
      }
      this.loadDestinations();

      this.searchSubscription = this.searchSubject.pipe(
        debounceTime(400),
        distinctUntilChanged()
      ).subscribe(searchText => {
        this.searchQuery = searchText;
        this.currentPage = 0;
        this.loadDestinations(0);
      });
    }
  }

  ngOnDestroy() {
    if (this.searchSubscription) {
      this.searchSubscription.unsubscribe();
    }
  }

  extractUserInfo() {
    if (isPlatformBrowser(this.platformId)) {
      const userStr = localStorage.getItem('current_user');
      if (userStr) {
        const user = JSON.parse(userStr);
        this.userId = user.id;
        this.username = user.username || this.username;
      }
    }
  }

  onSearchChange(searchText: string) {
    this.searchSubject.next(searchText);
  }

  loadDestinations(page: number = 0) {
    this.destinationService.searchDestinations(this.searchQuery, page, this.pageSize).subscribe({
      next: (data: Page<DestinationDto>) => {
        this.allDestinations = data.content;
        this.currentPage = data.number;
        this.totalPages = data.totalPages;

        this.updateDisplay();
        this.cdr.detectChanges();
      },
      error: (err: any) => console.error('Failed to load destinations', err)
    });
  }

  loadUserVisitList() {
    this.wantVisitService.getUserVisitList(this.userId).subscribe({
      next: (visits: WantVisitResponse[]) => {
        this.savedVisitsMap.clear();
        this.savedDestinations = [];

        visits.forEach(visit => {
          if (visit.destination && visit.destination.id && visit.id) {
            this.savedVisitsMap.set(visit.destination.id, visit.id);
            this.savedDestinations.push(visit.destination);
          }
        });

        this.updateDisplay();
        this.cdr.detectChanges();
      },
      error: (err: any) => console.error('Failed to load visit list', err)
    });
  }

  toggleVisit(dest: DestinationDto) {
    if (this.isProcessingVisit || !this.userId || !dest.id) return;

    this.isProcessingVisit = true;

    if (this.isVisited(dest.id)) {
      const visitId = this.savedVisitsMap.get(dest.id);
      if (visitId) {
        this.wantVisitService.removeFromVisitList(visitId, this.userId).subscribe({
          next: () => {
            this.savedVisitsMap.delete(dest.id as number);
            this.savedDestinations = this.savedDestinations.filter(d => d.id !== dest.id);

            if (this.viewMode === 'saved' && this.currentSavedPage > 0 && this.currentSavedPage * this.pageSize >= this.savedDestinations.length) {
              this.currentSavedPage--;
            }

            this.updateDisplay();
            this.isProcessingVisit = false;
            this.cdr.detectChanges();
          },
          error: (err: any) => {
            console.error('Failed to remove from list', err);
            this.isProcessingVisit = false;
          }
        });
      } else {
        this.isProcessingVisit = false;
      }
    } else {
      this.wantVisitService.addToVisitList(dest.id, this.userId).subscribe({
        next: (response: WantVisitResponse) => {
          if (response.destination && response.destination.id) {
            this.savedVisitsMap.set(response.destination.id, response.id);
            if (!this.savedDestinations.some(d => d.id === response.destination.id)) {
              this.savedDestinations.push(response.destination);
            }
          }
          this.updateDisplay();
          this.isProcessingVisit = false;
          this.cdr.detectChanges();
        },
        error: (err: any) => {
          if (err.status === 409) {
            this.loadUserVisitList();
          } else {
            console.error('Failed to add to list', err);
          }
          this.isProcessingVisit = false;
        }
      });
    }
  }

  isVisited(destId: number | undefined): boolean {
    if (!destId) return false;
    return this.savedVisitsMap.has(destId);
  }

  setViewMode(mode: 'all' | 'saved') {
    this.viewMode = mode;
    this.currentSavedPage = 0;
    this.updateDisplay();
    this.cdr.detectChanges();
  }

  updateDisplay() {
    if (this.viewMode === 'all') {
      this.displayedDestinations = [...this.allDestinations];
    } else {
      const startIndex = this.currentSavedPage * this.pageSize;
      const endIndex = startIndex + this.pageSize;
      this.displayedDestinations = this.savedDestinations.slice(startIndex, endIndex);
    }
  }

  get getTotalSavedPages(): number {
    return Math.ceil(this.savedDestinations.length / this.pageSize) || 1;
  }

  nextPage() {
    if (this.viewMode === 'all') {
      if (this.currentPage < this.totalPages - 1) {
        this.loadDestinations(this.currentPage + 1);
      }
    } else {
      if (this.currentSavedPage < this.getTotalSavedPages - 1) {
        this.currentSavedPage++;
        this.updateDisplay();
        this.cdr.detectChanges();
      }
    }
  }

  prevPage() {
    if (this.viewMode === 'all') {
      if (this.currentPage > 0) {
        this.loadDestinations(this.currentPage - 1);
      }
    } else {
      if (this.currentSavedPage > 0) {
        this.currentSavedPage--;
        this.updateDisplay();
        this.cdr.detectChanges();
      }
    }
  }

  logout() {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('jwt_token');
      localStorage.removeItem('current_user');
    }
    this.router.navigate(['/login']);
  }
}
