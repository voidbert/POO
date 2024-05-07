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
import org.example.user.IntermediateUser;
import org.junit.jupiter.api.Test;

public class ActivityTrackRunTest {
    private final ActivityTrackRun trackRun =
            new ActivityTrackRun(
                    Duration.ofSeconds(505), LocalDateTime.of(2030, 12, 25, 00, 00), 80, 2);

    @Test
    public void testClone() {
        ActivityTrackRun clone = this.trackRun.clone();
        assertTrue(this.trackRun.getExecutionTime().equals(clone.getExecutionTime()));
        assertTrue(this.trackRun.getExecutionDate().equals(clone.getExecutionDate()));
        assertTrue(this.trackRun.getBPM() == clone.getBPM());
        assertTrue(this.trackRun.getDistanceToTraverse() == clone.getDistanceToTraverse());
    }

    @Test
    public void countCalories() {
        final IntermediateUser intermediate =
                new IntermediateUser(1, "José Lopes", "UMinho", "a104541@alunos.uminho.pt", 60);
        assertEquals(this.trackRun.countCalories(intermediate), 2960.0, 0.1);
    }

    @Test
    public void testToString() {
        assertEquals(
                this.trackRun.toString(),
                "ActivityTrackRun(executionTime = \"PT8M25S\", executionDate = \"2030-12-25T00:00\", bpm = 80, distanceToTraverse = 2.000)");
    }

    @Test
    public void testSerialize() {
        try {
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
            objectOutputStream.writeObject(this.trackRun);

            byte bytes[] = byteOutputStream.toByteArray();
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);

            Activity read = (Activity) objectInputStream.readObject();
            assertEquals(read, this.trackRun);
        } catch (IOException e) {
            assertTrue(false);
        } catch (ClassNotFoundException e) {
            assertTrue(false);
        }
    }
}
