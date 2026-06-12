import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { OfferService } from '../../../core/services/offer.service';
import { OfferResponse } from '../../../core/interfaces/offer.interface';

@Component({
  selector: 'app-offer-search',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatCardModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
  ],
  templateUrl: './offer-search.component.html',
  styleUrl: './offer-search.component.scss',
})
export class OfferSearchComponent {
  private readonly offerService = inject(OfferService);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);
  private readonly fb = inject(FormBuilder);

  offers = signal<OfferResponse[]>([]);
  loading = signal(false);
  searched = signal(false);

  readonly today = new Date();

  searchForm: FormGroup = this.fb.group({
    departureCity: ['', [Validators.required, Validators.maxLength(100)]],
    returnCity: ['', [Validators.required, Validators.maxLength(100)]],
    departureDate: [null, Validators.required],
    returnDate: [null, Validators.required],
    acrissCode: [''],
  });

  onSearch(): void {
    if (this.searchForm.invalid) return;

    const v = this.searchForm.value;

    if (v.returnDate <= v.departureDate) {
      this.snackBar.open(
        'La date de retour doit être après la date de départ.',
        'Fermer',
        { duration: 4000 },
      );
      return;
    }

    const request = {
      departureCity: v.departureCity.trim(),
      returnCity: v.returnCity.trim(),
      departureDateTime: this.toIso(v.departureDate),
      returnDateTime: this.toIso(v.returnDate),
      acrissCode: v.acrissCode?.trim()?.toUpperCase() || undefined,
    };

    this.loading.set(true);
    this.searched.set(false);

    this.offerService.searchOffers(request).subscribe({
      next: (offers) => {
        this.offers.set(offers);
        this.searched.set(true);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.snackBar.open(
          'Erreur lors de la recherche. Veuillez réessayer.',
          'Fermer',
          { duration: 4000 },
        );
      },
    });
  }

  goToDetail(id: string): void {
    this.router.navigate(['/offers', id], {
      state: {
        departureDateTime: this.toIso(this.searchForm.value.departureDate),
        returnDateTime: this.toIso(this.searchForm.value.returnDate),
      },
    });
  }

  private toIso(date: Date): string {
    const d = new Date(date);
    d.setHours(10, 0, 0, 0);
    return d.toISOString().slice(0, 19);
  }

  goHome(): void {
    this.router.navigate(['/home']);
  }
}
