package vladislavmaltsev.terranotabot.loggingtest;


import org.junit.jupiter.api.Test;
import vladislavmaltsev.terranotabot.logging.MockLogs;

public class MockLogsTest {
    @Test
    public void testPerformSomeTask() {
        MockLogs mockLogs = new MockLogs();
        mockLogs.getMockLogs();
    }
}