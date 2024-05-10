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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class ActivityMountainRunTest {
    private ActivityMountainRun mountainRun;

    public ActivityMountainRunTest() throws ActivityException {
        this.mountainRun =
                new ActivityMountainRun(
                        Duration.ofMinutes(120),
                        LocalDateTime.of(2030, 12, 25, 00, 00),
                        69,
                        20,
                        0.8);
    }

    @Test
    public void countCalories() throws UserException {
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
    public void testClone() {
        final ActivityMountainRun clone = this.mountainRun.clone();
        assertEquals(this.mountainRun.getExecutionTime(), clone.getExecutionTime());
        assertEquals(this.mountainRun.getExecutionDate(), clone.getExecutionDate());
        assertEquals(this.mountainRun.getBPM(), clone.getBPM());
        assertEquals(this.mountainRun.getDistanceToTraverse(), clone.getDistanceToTraverse());
        assertEquals(this.mountainRun.getAltimetry(), clone.getAltimetry());
    }

    @Test
    public void testToString() {
        assertEquals(
                this.mountainRun.toString(),
                "ActivityMountainRun(executionTime = \"PT2H\", executionDate = \"2030-12-25T00:00\", bpm = 69, distanceToTraverse = 20.000, altimetry = 0.800)");
    }

    @Test
    public void testSerialize() {
        TestUtils.serialize(this.mountainRun);
    }
}
