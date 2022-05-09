package ru.ms.chat.repo;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ms.chat.dto.ChatMessage;
import ru.ms.chat.dto.MessageStatus;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {

    long countBySenderIdAndRecipientIdAndStatus(
            String senderId, String recipientId, MessageStatus status);

    List<ChatMessage> findByChatId(String chatId);

//    List<ChatMessage> findBySenderIdAndRecipientId(String senderId, String recipientId);

    @Transactional
    @Modifying
    @Query("UPDATE ChatMessage c SET c.status = :status WHERE c.senderId = :senderId AND c.recipientId = :recipientId")
    void updateStatusFromDelivered(
            @Param("status") MessageStatus status,
            @Param("senderId") String senderId,
            @Param("recipientId") String recipientId);

}
