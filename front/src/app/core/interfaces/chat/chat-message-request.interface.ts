export interface ChatMessageRequest {
  conversationId: string;
  userId?: string | null;
  subject?: string | null;
  content: string;
  direction: 'USER_TO_SUPPORT' | 'SUPPORT_TO_USER';
}


