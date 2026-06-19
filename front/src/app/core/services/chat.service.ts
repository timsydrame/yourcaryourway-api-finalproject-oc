import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { environment } from '../../../environments/environment';
import { ChatMessageRequest } from '../interfaces/chat/chat-message-request.interface';
import { ChatMessageResponse } from '../interfaces/chat/chat-message-response.interface';

@Injectable({
  providedIn: 'root',
})
export class ChatService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/api/v1/support`;
  private readonly wsUrl = `${environment.apiUrl}/ws`;

  private stompClient: Client | null = null;
  private messageSubject = new Subject<ChatMessageResponse>();
  private connectedSubject = new BehaviorSubject<boolean>(false);

  messages$ = this.messageSubject.asObservable();
  connected$ = this.connectedSubject.asObservable();

  connect(conversationId: string): void {
    this.disconnect();

    this.stompClient = new Client({
      webSocketFactory: () => new SockJS(this.wsUrl),
      reconnectDelay: 5000,

      onConnect: () => {
        console.log('✅ WebSocket connecté');
        this.connectedSubject.next(true);

        this.stompClient?.subscribe(
          `/topic/conversations/${conversationId}`,
          (message: IMessage) => {
            console.log('📩 Message reçu:', message.body);
            const body: ChatMessageResponse = JSON.parse(message.body);
            this.messageSubject.next(body);
          },
        );
      },

      onWebSocketClose: () => {
        console.warn('WebSocket fermé');
        this.connectedSubject.next(false);
      },

      onStompError: (frame) => {
        console.error('Erreur STOMP:', frame);
        this.connectedSubject.next(false);
      },
    });

    this.stompClient.activate();
  }

  disconnect(): void {
    if (this.stompClient) {
      this.stompClient.deactivate();
      this.stompClient = null;
    }

    this.connectedSubject.next(false);
  }

  sendMessage(request: ChatMessageRequest): boolean {
    console.log('Tentative envoi message:', request);
    console.log('STOMP connecté ?', this.stompClient?.connected);

    if (!this.stompClient?.connected) {
      console.warn('❌ WebSocket non connecté, message non envoyé.');
      return false;
    }

    this.stompClient.publish({
      destination: '/app/chat.send',
      body: JSON.stringify(request),
    });

    console.log('✅ Message publié vers /app/chat.send');

    return true;
  }

  getHistory(conversationId: string): Observable<ChatMessageResponse[]> {
    return this.http.get<ChatMessageResponse[]>(
      `${this.apiUrl}/messages/${conversationId}`,
    );
  }
}
