package org.example.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalDateTime;
import org.example.user.AdvancedUser;
import org.junit.jupiter.api.Test;

public class ActivityPushUpTest {
    private final ActivityPushUp pushUp =
            new ActivityPushUp(
                    Duration.ofMinutes(15), LocalDateTime.of(2030, 12, 25, 00, 00), 110, 70);

    @Test
    public void testClone() {
        final ActivityPushUp clone = this.pushUp.clone();
        assertTrue(this.pushUp.getExecutionTime().equals(clone.getExecutionTime()));
        assertTrue(this.pushUp.getExecutionDate().equals(clone.getExecutionDate()));
        assertTrue(this.pushUp.getBPM() == clone.getBPM());
        assertTrue(this.pushUp.getNumberOfReps() == clone.getNumberOfReps());
    }

    @Test
    public void countCalories() {
        final AdvancedUser advanced =
                new AdvancedUser(1, "José Lopes", "UMinho", "a104541@alunos.uminho.pt", 50);
        assertEquals(this.pushUp.countCalories(advanced), 309.4, 0.1);
    }

    @Test
    public void testToString() {
        assertEquals(
                this.pushUp.toString(),
                "ActivityPushUp(executionTime = \"PT15M\", executionDate = \"2030-12-25T00:00\", bpm = 110, numberOfReps = 70)");
    }
}
