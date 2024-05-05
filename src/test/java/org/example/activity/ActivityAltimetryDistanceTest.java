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
package org.example.activity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class ActivityAltimetryDistanceTest {
    @Test
    public void testEquals() {
        final ActivityAltimetryDistance mountainRun =
                new ActivityMountainRun(Duration.ofMinutes(20), LocalDateTime.MIN, 69, 20, 0.5);

        ActivityAltimetryDistance clone = mountainRun.clone();
        assertEquals(clone, mountainRun);

        clone.setAltimetry(0.999);
        assertNotEquals(clone, mountainRun);

        clone = mountainRun.clone();
        clone.setBPM(100);
        assertNotEquals(clone, mountainRun);
    }

    @Test
    public void setAltimetry() {
        ActivityAltimetryDistance mountainRun = new ActivityMountainRun();

        assertThrows(
                RuntimeException.class,
                () -> {
                    mountainRun.setAltimetry(-0.001);
                });
        assertThrows(
                RuntimeException.class,
                () -> {
                    mountainRun.setAltimetry(1.001);
                });
        assertDoesNotThrow(
                () -> {
                    mountainRun.setDistanceToTraverse(1.0);
                });
    }
}
