export interface ChatMessageResponse {
  id: string;
  conversationId: string;
  userId: string | null;
  subject: string | null;
  content: string;
  direction: string;
  attachmentUrl: string | null;
  isRead: boolean;
  createdAt: string;
}
