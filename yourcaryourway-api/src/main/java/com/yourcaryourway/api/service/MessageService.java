package com.yourcaryourway.api.service;

import com.yourcaryourway.api.dto.ChatMessageRequest;
import com.yourcaryourway.api.dto.ChatMessageResponse;
import com.yourcaryourway.api.model.Message;
import com.yourcaryourway.api.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public ChatMessageResponse sendMessage(ChatMessageRequest request) {

        // 1. Sauvegarder en base
        Message message = Message.builder()
                .conversationId(request.getConversationId().trim())
                .userId(request.getUserId())
                .subject(request.getSubject())
                .content(request.getContent().trim())
                .direction(request.getDirection())
                .build();

        Message saved = messageRepository.save(message);
        ChatMessageResponse response = toResponse(saved);

        // 2. Diffuser en temps réel à tous les abonnés
        messagingTemplate.convertAndSend(
                "/topic/conversations/" + request.getConversationId(),
                response
        );

        return response;
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getHistory(String conversationId) {
        return messageRepository
                .findByConversationIdOrderByCreatedAtAsc(conversationId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ChatMessageResponse toResponse(Message m) {
        return ChatMessageResponse.builder()
                .id(m.getId())
                .conversationId(m.getConversationId())
                .userId(m.getUserId())
                .subject(m.getSubject())
                .content(m.getContent())
                .direction(m.getDirection() != null ? m.getDirection().name() : null)
                .attachmentUrl(m.getAttachmentUrl())
                .isRead(m.isRead())
                .createdAt(m.getCreatedAt())
                .build();
    }
}