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

public class ActivityDiamondPushUpTest {
    private ActivityDiamondPushUp pushUp;

    public ActivityDiamondPushUpTest() throws ActivityException {
        this.pushUp =
                new ActivityDiamondPushUp(
                        Duration.ofMinutes(15), LocalDateTime.of(2030, 12, 25, 00, 00), 110, 70);
    }

    @Test
    public void countCalories() throws UserException {
        final AdvancedUser advanced =
                new AdvancedUser(
                        1,
                        "José Lopes",
                        "UMinho",
                        "a104541@alunos.uminho.pt",
                        50,
                        new UserActivities());
        assertEquals(this.pushUp.countCalories(advanced), 371.3, 0.1);
    }

    @Test
    public void testClone() {
        final ActivityDiamondPushUp clone = this.pushUp.clone();
        assertEquals(this.pushUp.getExecutionTime(), clone.getExecutionTime());
        assertEquals(this.pushUp.getExecutionDate(), clone.getExecutionDate());
        assertEquals(this.pushUp.getBPM(), clone.getBPM());
        assertEquals(this.pushUp.getNumberOfReps(), clone.getNumberOfReps());
    }

    @Test
    public void testToString() {
        assertEquals(
                this.pushUp.toString(),
                "ActivityDiamondPushUp(executionTime = \"PT15M\", executionDate = \"2030-12-25T00:00\", bpm = 110, numberOfReps = 70)");
    }

    @Test
    public void testSerialize() {
        TestUtils.serialize(this.pushUp);
    }
}
