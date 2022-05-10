package ru.ms.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ms.chat.dto.ChatMessage;
import ru.ms.chat.dto.MessageStatus;
import ru.ms.chat.exeption.ResourceNotFoundException;
import ru.ms.chat.repo.ChatMessageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository repository;
    @Autowired
    private ChatRoomService chatRoomService;

    public ChatMessage save(ChatMessage chatMessage) {
        chatMessage.setStatus(MessageStatus.RECEIVED);
        repository.save(chatMessage);
        return chatMessage;
    }

    public long countNewMessages(String senderId, String recipientId) {
        return repository.countBySenderIdAndRecipientIdAndStatus(
                senderId, recipientId, MessageStatus.RECEIVED);
    }

    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {

        String chatId = chatRoomService.getChatId(senderId, recipientId, false);

        List<ChatMessage> messages = repository.findByChatId(chatId);
//                .orElse(new ArrayList<>());

        if(messages != null && !messages.isEmpty()) {
            repository.updateStatusFromDelivered(MessageStatus.DELIVERED, senderId, recipientId);
        }

        return messages;
    }

    public ChatMessage findById(String id) {
        return repository
                .findById(id)
                .map(chatMessage -> {
                    chatMessage.setStatus(MessageStatus.DELIVERED);
                    return repository.save(chatMessage);
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException("can't find message (" + id + ")"));
    }

}
