import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DestinationsComponent } from './destinations';

describe('Destinations', () => {
  let component: DestinationsComponent;
  let fixture: ComponentFixture<DestinationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DestinationsComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(DestinationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
