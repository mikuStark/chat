package ru.ms.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.ms.chat.dto.ChatIndicator;
import ru.ms.chat.dto.ChatMessage;
import ru.ms.chat.service.ChatMessageService;
import ru.ms.chat.service.ChatRoomService;

import java.util.Optional;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ChatMessageService chatMessageService;
    @Autowired
    private ChatRoomService chatRoomService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        String chatId = chatRoomService.getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);
        chatMessage.setChatId(chatId);

        ChatMessage saved = chatMessageService.save(chatMessage);

        //todo метод convertAndSendToUser для отправки уведомления адресату
        // добавляет префикс /user и recipientId к адресу: /user/{recipientId}/queue/messages
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(),"/queue/messages",
                new ChatIndicator(
                        saved.getId(),
                        saved.getSenderId(),
                        saved.getSenderName()));
    }
}
