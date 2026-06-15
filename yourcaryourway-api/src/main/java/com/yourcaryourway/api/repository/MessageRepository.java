package com.yourcaryourway.api.repository;

import com.yourcaryourway.api.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query("""
        SELECT m FROM Message m
        WHERE m.conversationId = :conversationId
        ORDER BY m.createdAt ASC
    """)
    List<Message> findByConversationIdOrderByCreatedAtAsc(
            @Param("conversationId") String conversationId
    );
}