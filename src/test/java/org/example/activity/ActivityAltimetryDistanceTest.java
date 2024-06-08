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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ActivityAltimetryDistanceTest {
    private ActivityAltimetryDistance altimetryDistance;

    public ActivityAltimetryDistanceTest() throws ActivityException {
        this.altimetryDistance =
            new ActivityMountainRun(Duration.ofMinutes(20), LocalDateTime.MIN, 69, 20, 0.5);
    }

    @Test
    public void testEquals() {
        ActivityAltimetryDistance clone = this.altimetryDistance.clone();
        assertEquals(clone, this.altimetryDistance);

        try {
            clone.setAltimetry(0.999);
        } catch (ActivityException e) {}
        assertNotEquals(clone, this.altimetryDistance);

        clone = this.altimetryDistance.clone();
        try {
            clone.setBPM(100);
        } catch (ActivityException e) {}
        assertNotEquals(clone, this.altimetryDistance);
    }

    @Test
    public void setAltimetry() {
        assertThrows(ActivityException.class,
                     () -> { this.altimetryDistance.setAltimetry(-0.001); });
        assertThrows(ActivityException.class,
                     () -> { this.altimetryDistance.setAltimetry(1.001); });
        assertDoesNotThrow(() -> { this.altimetryDistance.setAltimetry(0.0); });
        assertDoesNotThrow(() -> { this.altimetryDistance.setAltimetry(1.0); });
    }
}
