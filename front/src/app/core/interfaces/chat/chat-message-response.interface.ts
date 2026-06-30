export interface ChatMessageResponse {
  id: string;
  conversationId: string;
  userId: string | null;
  firstName?: string;
  email?: string;
  subject: string | null;
  content: string;
  direction: string;
  attachmentUrl: string | null;
  isRead: boolean;
  createdAt: string;
}
