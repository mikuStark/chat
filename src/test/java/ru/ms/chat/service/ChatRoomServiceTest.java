package ru.ms.chat.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import ru.ms.chat.ConfigTest;
import ru.ms.chat.repo.ChatRoomRepository;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(value = {"/create-room-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-room-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ChatRoomServiceTest extends ConfigTest {

    @Autowired
    ChatRoomService chatRoomService;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Test
    void getChatId() {
        String chatId = chatRoomService.getChatId("1", "2", true);
        assertThat(chatId).isNotNull();
    }

    @Test
    void getChatIdExist() {
        String chatId = chatRoomService.getChatId("3", "4", true);
        assertThat(chatId).isEqualTo("3_4");
    }
}