package org.example.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import org.example.user.BeginnerUser;
import org.example.useractivities.UserActivities;
import org.junit.jupiter.api.Test;

public class ActivityMountainRunTest {
    private final ActivityMountainRun mountainRun =
            new ActivityMountainRun(
                    Duration.ofMinutes(120), LocalDateTime.of(2030, 12, 25, 00, 00), 69, 20, 0.8);

    @Test
    public void testClone() {
        final ActivityMountainRun clone = this.mountainRun.clone();
        assertTrue(this.mountainRun.getExecutionTime().equals(clone.getExecutionTime()));
        assertTrue(this.mountainRun.getExecutionDate().equals(clone.getExecutionDate()));
        assertTrue(this.mountainRun.getBPM() == clone.getBPM());
        assertTrue(this.mountainRun.getDistanceToTraverse() == clone.getDistanceToTraverse());
        assertTrue(this.mountainRun.getAltimetry() == clone.getAltimetry());
    }

    @Test
    public void countCalories() {
        final BeginnerUser beginner =
                new BeginnerUser(
                        1,
                        "José Lopes",
                        "UMinho",
                        "a104541@alunos.uminho.pt",
                        60,
                        new UserActivities());
        assertEquals(this.mountainRun.countCalories(beginner), 3850.2, 0.1);
    }

    @Test
    public void testToString() {
        assertEquals(
                this.mountainRun.toString(),
                "ActivityMountainRun(executionTime = \"PT2H\", executionDate = \"2030-12-25T00:00\", bpm = 69, distanceToTraverse = 20.000, altimetry = 0.800)");
    }

    @Test
    public void testSerialize() {
        try {
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
            objectOutputStream.writeObject(this.mountainRun);

            byte bytes[] = byteOutputStream.toByteArray();
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);

            Activity read = (Activity) objectInputStream.readObject();
            assertEquals(read, this.mountainRun);
        } catch (IOException e) {
            assertTrue(false);
        } catch (ClassNotFoundException e) {
            assertTrue(false);
        }
    }
}
