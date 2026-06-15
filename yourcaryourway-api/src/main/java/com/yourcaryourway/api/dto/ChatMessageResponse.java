package com.yourcaryourway.api.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageResponse {
    private UUID id;
    private String conversationId;
    private UUID userId;
    private String subject;
    private String content;
    private String direction;
    private String attachmentUrl;
    private boolean isRead;
    private LocalDateTime createdAt;
}