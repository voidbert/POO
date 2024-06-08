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

public class ActivityTrackRunTest {
    private ActivityTrackRun trackRun;

    public ActivityTrackRunTest() throws ActivityException {
        this.trackRun = new ActivityTrackRun(Duration.ofSeconds(505),
                                             LocalDateTime.of(2030, 12, 25, 00, 00),
                                             80,
                                             2);
    }

    @Test
    public void countCalories() throws UserException {
        final IntermediateUser intermediate = new IntermediateUser(1,
                                                                   "José Lopes",
                                                                   "UMinho",
                                                                   "a104541@alunos.uminho.pt",
                                                                   60,
                                                                   new UserActivities());
        assertEquals(this.trackRun.countCalories(intermediate), 2960.0, 0.1);
    }

    @Test
    public void testClone() {
        ActivityTrackRun clone = this.trackRun.clone();
        assertEquals(this.trackRun.getExecutionTime(), clone.getExecutionTime());
        assertEquals(this.trackRun.getExecutionDate(), clone.getExecutionDate());
        assertEquals(this.trackRun.getBPM(), clone.getBPM());
        assertEquals(this.trackRun.getDistanceToTraverse(), clone.getDistanceToTraverse());
    }

    @Test
    public void testToString() {
        assertEquals(
            this.trackRun.toString(),
            "ActivityTrackRun(executionTime = \"PT8M25S\", executionDate = \"2030-12-25T00:00\", bpm = 80, distanceToTraverse = 2.000)");
    }

    @Test
    public void testSerialize() {
        TestUtils.serialize(this.trackRun);
    }
}
