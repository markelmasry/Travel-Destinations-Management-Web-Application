import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DestinationService } from '../../core/services/destination';
import { DestinationDto } from '../../shared/models/app.models';

@Component({
  selector: 'app-destinations',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './destinations.html',
  styleUrls: ['./destinations.css']
})
export class DestinationsComponent implements OnInit {
  destinations: DestinationDto[] = [];
  searchCountry: string = '';

  constructor(private destinationService: DestinationService) {}

  ngOnInit(): void {
    this.loadAllDestinations();
  }

  loadAllDestinations(): void {
    this.destinationService.getAllDestinations().subscribe({
      next: (data) => {
        this.destinations = data;
        console.log('Destinations loaded successfully!', data);
      },
      error: (err) => {
        console.error('Could not fetch destinations from server', err);
      }
    });
  }

  onSearch(): void {
    if (this.searchCountry.trim() === '') {
      this.loadAllDestinations();
      return;
    }

    this.destinationService.searchDestinations(this.searchCountry).subscribe({
      next: (pageResult) => {
        this.destinations = pageResult.content;
      },
      error: (err) => {
        console.error('Search failed', err);
      }
    });
  }
}
