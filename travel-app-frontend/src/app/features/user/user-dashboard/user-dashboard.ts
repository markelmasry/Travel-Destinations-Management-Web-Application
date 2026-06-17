import { Component, OnInit, Inject, PLATFORM_ID, ChangeDetectorRef, OnDestroy } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { DestinationService } from '../../../core/services/destination';
import { WishlistService } from '../../../core/services/wishlist.service';
import { DestinationDto, WishlistItemResponseDto, Page } from '../../../shared/models/app.models';

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
    private wishlistService: WishlistService,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      this.extractUserInfo();

      if (this.username && this.username !== 'Explorer') {
        this.loadUserWishlist();
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

  loadUserWishlist() {
    this.wishlistService.getUserWishlist(this.username).subscribe({
      next: (visits: WishlistItemResponseDto[]) => {
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
      error: (err: any) => console.error('Failed to load wishlist items', err)
    });
  }

  toggleVisit(dest: DestinationDto) {
    if (this.isProcessingVisit || !dest.id || !this.username) return;

    this.isProcessingVisit = true;

    if (this.isVisited(dest.id)) {
      const wishlistId = this.savedVisitsMap.get(dest.id);
      if (wishlistId) {
        this.wishlistService.removeFromWishlist(wishlistId, this.username).subscribe({
          next: () => {
            this.savedVisitsMap.delete(dest.id!);
            this.savedDestinations = this.savedDestinations.filter(d => d.id !== dest.id);
            this.updateDisplay();
            this.isProcessingVisit = false;
            this.cdr.detectChanges();
          },
          error: (err: any) => {
            console.error('Failed to remove from wishlist:', err);
            this.isProcessingVisit = false;
          }
        });
      } else {
        this.isProcessingVisit = false;
      }
    } else {
      this.wishlistService.addToWishlist(dest.id, this.username).subscribe({
        next: (response: WishlistItemResponseDto) => {
          this.savedVisitsMap.set(dest.id!, response.id);
          this.savedDestinations.push(dest);
          this.updateDisplay();
          this.isProcessingVisit = false;
          this.cdr.detectChanges();
        },
        error: (err: any) => {
          console.error('Failed to add to wishlist:', err);
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
      localStorage.clear();
    }
    this.router.navigate(['/login']);
  }
}
