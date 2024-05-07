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

public class ActivityWeightLiftingTest {
    private final ActivityWeightLifting weightLifting =
            new ActivityWeightLifting(
                    Duration.ofMinutes(5), LocalDateTime.of(2024, 5, 5, 11, 14), 90, 15, 30);

    @Test
    public void testClone() {
        ActivityWeightLifting clone = this.weightLifting.clone();
        assertTrue(this.weightLifting.getExecutionTime().equals(clone.getExecutionTime()));
        assertTrue(this.weightLifting.getExecutionDate().equals(clone.getExecutionDate()));
        assertTrue(this.weightLifting.getBPM() == clone.getBPM());
        assertTrue(this.weightLifting.getNumberOfReps() == clone.getNumberOfReps());
        assertTrue(this.weightLifting.getWeightsHeft() == clone.getWeightsHeft());
    }

    @Test
    public void countCalories() {
        final IntermediateUser intermediate =
                new IntermediateUser(1, "José Lopes", "UMinho", "a104541@alunos.uminho.pt", 60);
        assertEquals(this.weightLifting.countCalories(intermediate), 59.1, 0.1);
    }

    @Test
    public void testToString() {
        assertEquals(
                this.weightLifting.toString(),
                "ActivityWeightLifting(executionTime = \"PT5M\", executionDate = \"2024-05-05T11:14\", bpm = 90, numberOfReps = 15, weightsHeft = 30.00)");
    }

    @Test
    public void testSerialize() {
        try {
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
            objectOutputStream.writeObject(this.weightLifting);

            byte bytes[] = byteOutputStream.toByteArray();
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);

            Activity read = (Activity) objectInputStream.readObject();
            assertEquals(read, this.weightLifting);
        } catch (IOException e) {
            assertTrue(false);
        } catch (ClassNotFoundException e) {
            assertTrue(false);
        }
    }
}
