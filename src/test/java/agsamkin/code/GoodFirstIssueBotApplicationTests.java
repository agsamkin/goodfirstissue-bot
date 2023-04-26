package agsamkin.code;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@SpringBootTest
class GoodFirstIssueBotApplicationTests {
    @Test
    void contextLoads() {
        assertEquals(true, true);
    }
}
