package bojanstipic.skeleton;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class TestData {

    @PostConstruct
    private void init() {}
}
