import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { OfferService } from '../../../core/services/offer.service';
import { ReservationService } from '../../../core/services/reservation.service';
import { OfferResponse } from '../../../core/interfaces/offer.interface';

@Component({
  selector: 'app-offer-detail',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
  ],
  templateUrl: './offer-detail.component.html',
  styleUrl: './offer-detail.component.scss',
})
export class OfferDetailComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly offerService = inject(OfferService);
  private readonly reservationService = inject(ReservationService);
  private readonly snackBar = inject(MatSnackBar);

  offer = signal<OfferResponse | null>(null);
  loading = signal(true);
  reserving = signal(false);

  departureDateTime = signal<string>('');
  returnDateTime = signal<string>('');

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (!id) {
      this.loading.set(false);
      this.snackBar.open('Offre introuvable.', 'Fermer', { duration: 3000 });
      this.router.navigate(['/offers']);
      return;
    }

    const state = history.state;

    if (state?.departureDateTime) {
      this.departureDateTime.set(state.departureDateTime);
    }

    if (state?.returnDateTime) {
      this.returnDateTime.set(state.returnDateTime);
    }

    this.offerService.findById(id).subscribe({
      next: (offer) => {
        this.offer.set(offer);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.snackBar.open('Offre introuvable.', 'Fermer', { duration: 3000 });
        this.router.navigate(['/offers']);
      },
    });
  }

  reserve(): void {
    const o = this.offer();

    if (!o) {
      return;
    }

    if (!this.departureDateTime() || !this.returnDateTime()) {
      this.snackBar.open(
        'Veuillez relancer une recherche avant de réserver.',
        'Fermer',
        { duration: 4000 },
      );
      this.router.navigate(['/offers']);
      return;
    }

    this.reserving.set(true);

    this.reservationService
      .create({
        offerId: o.id,
        departureDateTime: this.departureDateTime(),
        returnDateTime: this.returnDateTime(),
      })
      .subscribe({
        next: () => {
          this.snackBar.open('Réservation confirmée ! 🎉', 'Fermer', {
            duration: 3000,
          });
          this.router.navigate(['/reservations']);
        },
        error: (err) => {
          this.reserving.set(false);
          const msg = err.error?.message || 'Erreur lors de la réservation.';
          this.snackBar.open(msg, 'Fermer', { duration: 4000 });
        },
      });
  }

  goBack(): void {
    this.router.navigate(['/offers']);
  }
}
