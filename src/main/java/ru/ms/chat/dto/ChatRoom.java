package ru.ms.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "chat_room")
public class ChatRoom {

    @Id
    private String id = UUID.randomUUID().toString();
    //конкатенация senderId_recipientId, для каждой беседы мы сохраняем две сущности с одинаковыми chatId
    private String chatId;
    private String senderId;
    private String recipientId;

}
