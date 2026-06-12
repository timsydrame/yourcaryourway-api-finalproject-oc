import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatChipsModule } from '@angular/material/chips';
import { ReservationService } from '../../../core/services/reservation.service';
import { ReservationResponse } from '../../../core/interfaces/reservation.interface';

@Component({
  selector: 'app-my-reservations',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatChipsModule,
  ],
  templateUrl: './my-reservations.component.html',
  styleUrl: './my-reservations.component.scss',
})
export class MyReservationsComponent implements OnInit {
  private readonly reservationService = inject(ReservationService);
  private readonly snackBar = inject(MatSnackBar);
  private readonly router = inject(Router);

  reservations = signal<ReservationResponse[]>([]);
  loading = signal(true);
  cancellingId = signal<string | null>(null);

  ngOnInit(): void {
    this.loadReservations();
  }

  loadReservations(): void {
    this.loading.set(true);
    this.reservationService.getMyReservations().subscribe({
      next: (data) => {
        this.reservations.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.snackBar.open('Erreur lors du chargement.', 'Fermer', {
          duration: 3000,
        });
      },
    });
  }

  cancel(id: string): void {
    this.cancellingId.set(id);
    this.reservationService.cancel(id).subscribe({
      next: () => {
        this.snackBar.open('Réservation annulée.', 'Fermer', {
          duration: 3000,
        });
        this.cancellingId.set(null);
        this.loadReservations();
      },
      error: (err) => {
        this.cancellingId.set(null);
        const msg = err.error?.message || 'Annulation impossible.';
        this.snackBar.open(msg, 'Fermer', { duration: 4000 });
      },
    });
  }

  getStatusColor(status: string): string {
    switch (status) {
      case 'CONFIRMED':
        return 'primary';
      case 'CANCELLED':
        return 'warn';
      default:
        return 'accent';
    }
  }

  getStatusLabel(status: string): string {
    switch (status) {
      case 'CONFIRMED':
        return 'Confirmée';
      case 'CANCELLED':
        return 'Annulée';
      case 'PENDING':
        return 'En attente';
      case 'MODIFIED':
        return 'Modifiée';
      default:
        return status;
    }
  }

  formatDate(dt: string): string {
    return new Date(dt).toLocaleDateString('fr-FR', {
      day: '2-digit',
      month: 'long',
      year: 'numeric',
    });
  }

  goToSearch(): void {
    this.router.navigate(['/offers']);
  }

  goHome(): void {
    this.router.navigate(['/home']);
  }
}
