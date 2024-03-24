package org.example;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

public class ApplicationTest {
    @Test public void appHasAGreeting() {
        Application classUnderTest = new Application();
        assertNotNull("App should have a greeting", classUnderTest.getGreeting());
    }
}
