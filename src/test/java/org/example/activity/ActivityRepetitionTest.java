package org.example.activity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class ActivityRepetitionTest {
    @Test
    public void testEquals() {
        final ActivityRepetition pushUp =
                new ActivityPushUp(Duration.ofMinutes(20), LocalDateTime.MIN, 69, 15);

        ActivityRepetition clone = pushUp.clone();
        assertEquals(clone, pushUp);

        clone.setNumberOfReps(40);
        assertNotEquals(clone, pushUp);

        clone = pushUp.clone();
        clone.setBPM(100);
        assertNotEquals(clone, pushUp);
    }

    @Test
    public void setNumberOfReps() {
        ActivityRepetition pushUp = new ActivityPushUp();

        assertThrows(
                RuntimeException.class,
                () -> {
                    pushUp.setNumberOfReps(-1);
                });
        assertDoesNotThrow(
                () -> {
                    pushUp.setNumberOfReps(101);
                });
    }
}
