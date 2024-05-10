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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class ActivityRepetitionTest {
    private ActivityRepetition repetition;

    public ActivityRepetitionTest() throws ActivityException {
        this.repetition = new ActivityPushUp(Duration.ofMinutes(20), LocalDateTime.MIN, 69, 15);
    }

    @Test
    public void testEquals() {
        ActivityRepetition clone = this.repetition.clone();
        assertEquals(clone, this.repetition);

        try {
            clone.setNumberOfReps(40);
        } catch (ActivityException e) {
        }
        assertNotEquals(clone, this.repetition);

        clone = this.repetition.clone();
        try {
            clone.setBPM(100);
        } catch (ActivityException e) {
        }
        assertNotEquals(clone, this.repetition);
    }

    @Test
    public void setNumberOfReps() {
        assertThrows(
                ActivityException.class,
                () -> {
                    this.repetition.setNumberOfReps(-1);
                });
        assertDoesNotThrow(
                () -> {
                    this.repetition.setNumberOfReps(101);
                });
    }
}
