/*
 * Copyright 2024 Diogo Costa, Humberto Gomes, José Lopes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.example.fitness;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActivityWeightLiftingTest {
    private ActivityWeightLifting weightLifting;

    public ActivityWeightLiftingTest() throws ActivityException {
        this.weightLifting = new ActivityWeightLifting(Duration.ofMinutes(5),
                                                       LocalDateTime.of(2024, 5, 5, 11, 14),
                                                       90,
                                                       15,
                                                       30);
    }

    @Test
    public void countCalories() throws UserException {
        final IntermediateUser intermediate = new IntermediateUser(1,
                                                                   "José Lopes",
                                                                   "UMinho",
                                                                   "a104541@alunos.uminho.pt",
                                                                   60,
                                                                   new UserActivities());
        assertEquals(this.weightLifting.countCalories(intermediate), 59.1, 0.1);
    }

    @Test
    public void testClone() {
        ActivityWeightLifting clone = this.weightLifting.clone();
        assertEquals(this.weightLifting.getExecutionTime(), clone.getExecutionTime());
        assertEquals(this.weightLifting.getExecutionDate(), clone.getExecutionDate());
        assertEquals(this.weightLifting.getBPM(), clone.getBPM());
        assertEquals(this.weightLifting.getNumberOfReps(), clone.getNumberOfReps());
        assertEquals(this.weightLifting.getWeightsHeft(), clone.getWeightsHeft());
    }

    @Test
    public void testToString() {
        assertEquals(
            this.weightLifting.toString(),
            "ActivityWeightLifting(executionTime = \"PT5M\", executionDate = \"2024-05-05T11:14\", bpm = 90, numberOfReps = 15, weightsHeft = 30.00)");
    }

    @Test
    public void testSerialize() {
        TestUtils.serialize(this.weightLifting);
    }
}
