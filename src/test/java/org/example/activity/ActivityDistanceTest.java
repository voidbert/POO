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

public class ActivityDistanceTest {
    private ActivityDistance distance;

    public ActivityDistanceTest() throws ActivityException {
        this.distance = new ActivityTrackRun(Duration.ofMinutes(20), LocalDateTime.MIN, 69, 20.0);
    }

    @Test
    public void testEquals() {
        ActivityDistance clone = this.distance.clone();
        assertEquals(clone, this.distance);

        try {
            clone.setDistanceToTraverse(14.5);
        } catch (ActivityException e) {
        }
        assertNotEquals(clone, this.distance);

        clone = this.distance.clone();
        try {
            clone.setBPM(100);
        } catch (ActivityException e) {
        }
        assertNotEquals(clone, this.distance);
    }

    @Test
    public void setDistanceToTraverse() {
        assertThrows(
                ActivityException.class,
                () -> {
                    this.distance.setDistanceToTraverse(-1.0);
                });
        assertDoesNotThrow(
                () -> {
                    this.distance.setDistanceToTraverse(420.0);
                });
    }
}
