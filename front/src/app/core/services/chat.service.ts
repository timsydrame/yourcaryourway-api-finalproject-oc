import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
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

  messages$ = this.messageSubject.asObservable();

  connect(conversationId: string): void {
    this.disconnect();

    this.stompClient = new Client({
      webSocketFactory: () => new SockJS(this.wsUrl),
      onConnect: () => {
        this.stompClient?.subscribe(
          `/topic/conversations/${conversationId}`,
          (message: IMessage) => {
            const body: ChatMessageResponse = JSON.parse(message.body);
            this.messageSubject.next(body);
          },
        );
      },
      onStompError: (frame) => {
        console.error('WebSocket error:', frame);
      },
    });

    this.stompClient.activate();
  }

  disconnect(): void {
    if (this.stompClient) {
      this.stompClient.deactivate();
      this.stompClient = null;
    }
  }

  sendMessage(request: ChatMessageRequest): void {
    if (!this.stompClient?.connected) {
      console.warn('WebSocket non connecté, message non envoyé.');
      return;
    }

    this.stompClient.publish({
      destination: '/app/chat.send',
      body: JSON.stringify(request),
    });
  }

  getHistory(conversationId: string): Observable<ChatMessageResponse[]> {
    return this.http.get<ChatMessageResponse[]>(
      `${this.apiUrl}/messages/${conversationId}`,
    );
  }
}
