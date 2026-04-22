package com.cashsystem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CashSystemApplicationTest {

    @Test
    void contextLoads() {
        // 测试Spring上下文是否正常加载
        assertTrue(true, "Spring上下文应正常加载");
    }

    @Test
    void mainMethodStartsApplication() {
        // 测试主方法是否能正常启动
        assertDoesNotThrow(() -> {
            CashSystemApplication.main(new String[]{});
        });
    }
}