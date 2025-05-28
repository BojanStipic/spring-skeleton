package bojanstipic.skeleton;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@ImportTestcontainers(TestContainers.class)
@TestInstance(Lifecycle.PER_CLASS)
public class TestBase {}
