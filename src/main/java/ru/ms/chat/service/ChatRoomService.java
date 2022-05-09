package ru.ms.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ms.chat.dto.ChatRoom;
import ru.ms.chat.repo.ChatRoomRepository;

import java.util.Optional;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    public Optional<String> getChatId(
            String senderId, String recipientId, boolean createIfNotExist) {

        return chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(en -> {
                    if (en.getChatId().isEmpty() || en.getChatId() == null) {
                        return getFinalChatId(senderId, recipientId, createIfNotExist);
                    } else {
                        return en.getChatId();
                    }
                });
    }

    private String getFinalChatId(String senderId, String recipientId, boolean createIfNotExist) {
        if(!createIfNotExist) {
            return null;
        }
        String chatId =
                String.format("%s_%s", senderId, recipientId);

        ChatRoom senderRecipient = ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

        ChatRoom recipientSender = ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();
        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);

        return chatId;
    }

}
