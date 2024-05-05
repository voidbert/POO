package org.example.activity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class ActivityRepetitionWeightedTest {
    @Test
    public void testEquals() {
        final ActivityRepetitionWeighted weightLifting =
                new ActivityWeightLifting(Duration.ofMinutes(20), LocalDateTime.MIN, 69, 15, 20);

        ActivityRepetitionWeighted clone = weightLifting.clone();
        assertEquals(clone, weightLifting);

        clone.setWeightsHeft(10);
        assertNotEquals(clone, weightLifting);

        clone = weightLifting.clone();
        clone.setBPM(100);
        assertNotEquals(clone, weightLifting);
    }

    @Test
    public void setNumberOfReps() {
        ActivityRepetitionWeighted weightLifting = new ActivityWeightLifting();

        assertThrows(
                RuntimeException.class,
                () -> {
                    weightLifting.setWeightsHeft(-1);
                });
        assertDoesNotThrow(
                () -> {
                    weightLifting.setWeightsHeft(40);
                });
    }
}
