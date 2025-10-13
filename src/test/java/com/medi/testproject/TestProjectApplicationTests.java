package com.medi.testproject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestProjectApplicationTests {

    @Test
    void contextLoads() {
    }

//    @Autowired
//    private DataSource dataSource;

    @Test
    void testConnection() throws Exception {
//        try (Connection conn = dataSource.getConnection()) {
//            System.out.println("✅ DB 연결 성공: " + conn);
//        }
    }

}
