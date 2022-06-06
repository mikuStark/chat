package ru.ms.chat;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("/application-dev.properties")
public class ConfigTest {
}
