package com.yourcaryourway.api.controller;

import com.yourcaryourway.api.dto.ChatMessageResponse;
import com.yourcaryourway.api.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/support")
@RequiredArgsConstructor
public class MessageRestController {

    private final MessageService messageService;

    @GetMapping("/messages/{conversationId}")
    public ResponseEntity<List<ChatMessageResponse>> getHistory(
            @PathVariable String conversationId
    ) {
        return ResponseEntity.ok(messageService.getHistory(conversationId));
    }
}