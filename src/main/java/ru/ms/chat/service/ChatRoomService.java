package ru.ms.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ms.chat.dto.ChatRoom;
import ru.ms.chat.repo.ChatRoomRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    public String getChatId(
            String senderId, String recipientId, boolean createIfNotExist) {

        return chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .orElse(getFinalChatId(senderId, recipientId, createIfNotExist));
    }

    private String getFinalChatId(String senderId, String recipientId, boolean createIfNotExist) {
        if(!createIfNotExist) {
            return null;
        }
        String chatId = String.format("%s_%s", senderId, recipientId);

        ChatRoom senderRecipient = ChatRoom
                .builder()
                .id(UUID.randomUUID().toString())
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

        ChatRoom recipientSender = ChatRoom
                .builder()
                .id(UUID.randomUUID().toString())
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();
        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);

        return chatId;
    }

}
