package com.yourcaryourway.api.dto;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageResponse {
    private UUID id;
    private String firstName;
    private String email;
    private String conversationId;
    private UUID userId;
    private String subject;
    private String content;
    private String direction;
    private String attachmentUrl;
    private boolean isRead;
    private Instant createdAt;
}