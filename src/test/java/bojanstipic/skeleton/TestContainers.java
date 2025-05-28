package bojanstipic.skeleton;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;

interface TestContainers {
    @Container
    @ServiceConnection
    static RedisContainer redis = new RedisContainer("redis:alpine");
}
