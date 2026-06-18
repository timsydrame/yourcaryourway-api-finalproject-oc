import { Component, inject, signal, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatIconModule } from '@angular/material/icon';

import { ChatService } from '../../../core/services/chat.service';
import { AuthService } from '../../../core/services/auth.service';
import { ChatMessageResponse } from '../../../core/interfaces/chat/chat-message-response.interface';

const CONVERSATION_ID = 'demo-user-support';

@Component({
  selector: 'app-support-chat',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatIconModule,
  ],
  templateUrl: './support-chat.component.html',
  styleUrl: './support-chat.component.scss',
})
export class SupportChatComponent implements OnInit, OnDestroy {
  private readonly chatService = inject(ChatService);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);

  private subscription: Subscription | null = null;

  messages = signal<ChatMessageResponse[]>([]);
  loading = signal(true);
  sending = signal(false);
  connected = signal(false);

  messageControl = new FormControl('', [
    Validators.required,
    Validators.maxLength(1000),
  ]);

  currentUser = this.getCurrentUserSafely();

  ngOnInit(): void {
    this.loadHistory();
    this.connectWebSocket();
    this.listenToMessages();
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
    this.chatService.disconnect();
  }

  send(): void {
    if (this.messageControl.invalid || this.sending()) {
      return;
    }

    const content = this.messageControl.value?.trim();

    if (!content) {
      return;
    }

    this.sending.set(true);

    this.chatService.sendMessage({
      conversationId: CONVERSATION_ID,
      userId: this.currentUser?.id ?? null,
      subject: 'Support client',
      content,
      direction: 'USER_TO_SUPPORT',
    });

    this.messageControl.reset('');
    this.messageControl.markAsPristine();
    this.messageControl.markAsUntouched();
    this.messageControl.updateValueAndValidity();

    this.sending.set(false);

    this.scrollToBottom();
  }

  goBack(): void {
    this.router.navigate(['/home']);
  }

  isMyMessage(msg: ChatMessageResponse): boolean {
    return msg.direction === 'USER_TO_SUPPORT';
  }

  formatTime(dt: string): string {
    return new Date(dt).toLocaleTimeString('fr-FR', {
      hour: '2-digit',
      minute: '2-digit',
    });
  }

  private loadHistory(): void {
    this.loading.set(true);

    this.chatService.getHistory(CONVERSATION_ID).subscribe({
      next: (history) => {
        this.messages.set(history);
        this.loading.set(false);
        this.scrollToBottom();
      },
      error: () => {
        this.loading.set(false);
        this.snackBar.open(
          'Erreur lors du chargement de l’historique.',
          'Fermer',
          {
            duration: 3000,
          },
        );
      },
    });
  }

  private connectWebSocket(): void {
    this.chatService.connect(CONVERSATION_ID);
    this.connected.set(true);
  }

  private listenToMessages(): void {
    this.subscription = this.chatService.messages$.subscribe((msg) => {
      this.messages.update((list) => [...list, msg]);
      this.scrollToBottom();
    });
  }

  private scrollToBottom(): void {
    setTimeout(() => {
      const el = document.getElementById('chat-messages');

      if (el) {
        el.scrollTop = el.scrollHeight;
      }
    }, 50);
  }

  private getCurrentUserSafely(): { id?: string } | null {
    return null;
  }
}
