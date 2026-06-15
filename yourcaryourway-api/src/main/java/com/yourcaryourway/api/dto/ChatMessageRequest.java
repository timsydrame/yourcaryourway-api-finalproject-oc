package com.yourcaryourway.api.dto;

import com.yourcaryourway.api.model.Message;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageRequest {

    @NotBlank(message = "La conversation est obligatoire")
    @Size(max = 100)
    private String conversationId;

    private UUID userId;

    @Size(max = 255)
    private String subject;

    @NotBlank(message = "Le message ne peut pas être vide")
    private String content;

    @NotNull(message = "La direction est obligatoire")
    private Message.Direction direction;
}