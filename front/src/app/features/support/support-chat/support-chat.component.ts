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
import { AuthService } from '../../../core/services/auth.service';

import { ChatService } from '../../../core/services/chat.service';
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
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);
  private readonly authService = inject(AuthService);

  private readonly subscriptions = new Subscription();

  messages = signal<ChatMessageResponse[]>([]);
  loading = signal(true);
  sending = signal(false);
  connected = signal(false);

  currentUserEmail = signal<string | null>(null);
  isSupportAgent = signal(false);
  currentUserId = signal<string | null>(null);

  messageControl = new FormControl('', [Validators.maxLength(1000)]);

  ngOnInit(): void {
    const currentUser = this.authService.getCurrentUser();
    this.currentUserEmail.set(currentUser?.email ?? null);
    this.currentUserId.set(currentUser?.id ?? null);
    this.isSupportAgent.set(
      currentUser?.email === 'support@yourcaryourway.com',
    );
    this.loadHistory();

    this.chatService.connect(CONVERSATION_ID);

    this.subscriptions.add(
      this.chatService.connected$.subscribe((isConnected) => {
        this.connected.set(isConnected);
      }),
    );

    this.subscriptions.add(
      this.chatService.messages$.subscribe((message) => {
        this.messages.update((list) => [...list, message]);
        this.scrollToBottom();
      }),
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
    this.chatService.disconnect();
  }

  send(): void {
    if (!this.canSend()) {
      return;
    }

    const content = this.messageControl.value?.trim() ?? '';
    this.sending.set(true);

    const sent = this.chatService.sendMessage({
      conversationId: CONVERSATION_ID,
      userId: this.currentUserId(),
      subject: 'Support client',
      content,
      direction: this.isSupportAgent() ? 'SUPPORT_TO_USER' : 'USER_TO_SUPPORT',
    });

    if (!sent) {
      this.sending.set(false);
      this.snackBar.open(
        'Message non envoyé : WebSocket non connecté.',
        'Fermer',
        { duration: 3000 },
      );
      return;
    }

    this.messageControl.reset('');
    this.messageControl.markAsPristine();
    this.messageControl.markAsUntouched();
    this.messageControl.updateValueAndValidity();

    this.sending.set(false);
  }

  goBack(): void {
    this.router.navigate(['/home']);
  }

  isMyMessage(message: ChatMessageResponse): boolean {
    return message.email === this.currentUserEmail();
  }

  formatTime(dt: string | null | undefined): string {
    if (!dt) {
      return '';
    }

    const date = new Date(dt);

    if (Number.isNaN(date.getTime())) {
      return '';
    }

    return new Intl.DateTimeFormat(navigator.language || 'fr-FR', {
      hour: '2-digit',
      minute: '2-digit',
    }).format(date);
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
          { duration: 3000 },
        );
      },
    });
  }

  private scrollToBottom(): void {
    setTimeout(() => {
      const element = document.getElementById('chat-messages');

      if (element) {
        element.scrollTop = element.scrollHeight;
      }
    }, 50);
  }

  canSend(): boolean {
    const content = this.messageControl.value?.trim();

    return (
      !!content &&
      !this.messageControl.hasError('maxlength') &&
      !this.sending() &&
      this.connected()
    );
  }

  getSenderLabel(message: ChatMessageResponse): string {
    if (this.isMyMessage(message)) {
      return 'Vous';
    }
    return message.firstName || 'Support client';
  }

  getSenderColor(msg: ChatMessageResponse): string {
    if (this.isMyMessage(msg)) {
      return '#BDD7EE';
    }

    const colors = [
      '#2b5983',
      '#375623',
      '#B8860B',
      '#6A1B9A',
      '#00695C',
      '#365820',
    ];
    const label = this.getSenderLabel(msg);
    let hash = 0;
    for (let i = 0; i < label.length; i++) {
      hash = label.charCodeAt(i) + ((hash << 5) - hash);
    }
    return colors[Math.abs(hash) % colors.length];
  }
}
