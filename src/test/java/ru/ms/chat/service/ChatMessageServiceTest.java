package ru.ms.chat.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import ru.ms.chat.ConfigTest;
import ru.ms.chat.dto.ChatMessage;
import ru.ms.chat.dto.MessageStatus;
import ru.ms.chat.exeption.ResourceNotFoundException;
import ru.ms.chat.repo.ChatMessageRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;

@Sql(value = {"/create-message-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-message-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ChatMessageServiceTest extends ConfigTest {

    @Autowired
    ChatMessageService chatMessageService;
    @Autowired
    ChatMessageRepository repository;
    @MockBean
    ChatRoomService chatRoomService;

    @Test
    void findById() {
        ChatMessage message = chatMessageService.findById("1");
        assertThat(message).isNotNull();
        try {
            chatMessageService.findById("qwer");
        } catch (ResourceNotFoundException ex) {
            System.out.println("OK");
        }
    }

    @Test
    void countNewMessages() {
        long count = repository.countBySenderIdAndRecipientIdAndStatus(
                "1", "1", MessageStatus.DELIVERED);
        assertThat(count).isNotNull();
        assertThat(count).isEqualTo(1);
    }

    @Test
    void updateStatusFromDelivered() {
        ChatMessage message = repository.findById("2").orElse(null);
        assertThat(message.getStatus()).isEqualTo(MessageStatus.RECEIVED);
        repository.updateStatusFromDelivered(MessageStatus.DELIVERED, "2", "2");
        message = repository.findById("2").orElse(null);
        assertThat(message.getStatus()).isEqualTo(MessageStatus.DELIVERED);
    }

    @Test
    void save() {
        ChatMessage message = ChatMessage.builder().id("33")
                .senderId("33").recipientId("33").build();
        repository.save(message);
        message = repository.findById("33").orElse(null);
        assertThat(message).isNotNull();
    }

    @Test
    void findChatMessages() {
        when(chatRoomService.getChatId(any(), any(), anyBoolean())).thenReturn("1_2");
        List<ChatMessage> messages = chatMessageService.findChatMessages("1", "1");
        assertThat(messages).isNotEmpty();
    }
}