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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ActivityTest {
    private Activity activity = new ActivityPushUp();

    @Test
    public void setBPM() {
        assertThrows(ActivityException.class, () -> { this.activity.setBPM(0); });
        assertThrows(ActivityException.class, () -> { this.activity.setBPM(-1); });
        assertDoesNotThrow(() -> { this.activity.setBPM(60); });
    }

    @Test
    public void setExecutionTime() {
        assertThrows(ActivityException.class,
                     () -> { this.activity.setExecutionTime(Duration.ofMillis(999)); });
        assertDoesNotThrow(() -> { this.activity.setExecutionTime(Duration.ofSeconds(1)); });
    }

    @Test
    public void overlaps() throws ActivityException {
        final Activity a1 =
            new ActivityPushUp(Duration.ofMinutes(20), LocalDateTime.of(2024, 5, 6, 10, 0), 70, 15);
        final Activity a2 = new ActivityPushUp(Duration.ofMinutes(20),
                                               LocalDateTime.of(2024, 5, 6, 10, 10),
                                               70,
                                               15);
        final Activity a3 = new ActivityPushUp(Duration.ofMinutes(20),
                                               LocalDateTime.of(2024, 5, 6, 10, 20),
                                               90,
                                               15);

        assertTrue(a1.overlaps(a1));
        assertTrue(a1.overlaps(a2));
        assertTrue(a2.overlaps(a1));
        assertFalse(a3.overlaps(a1));
        assertFalse(a1.overlaps(a3));
        assertTrue(a2.overlaps(a3));
        assertTrue(a3.overlaps(a2));
    }

    @Test
    public void testEquals() throws ActivityException {
        final Activity mountainRun =
            new ActivityMountainRun(Duration.ofMinutes(20), LocalDateTime.MIN, 69, 20, 0.500);
        final Activity pushUp =
            new ActivityPushUp(Duration.ofMinutes(20), LocalDateTime.MIN, 69, 15);
        final Activity trackRun =
            new ActivityTrackRun(Duration.ofMinutes(20), LocalDateTime.MIN, 69, 20);
        final Activity weightLifting =
            new ActivityWeightLifting(Duration.ofMinutes(20), LocalDateTime.MIN, 69, 15, 20);

        assertNotEquals(mountainRun, pushUp);
        assertNotEquals(mountainRun, trackRun);
        assertNotEquals(mountainRun, weightLifting);
        assertNotEquals(pushUp, trackRun);
        assertNotEquals(pushUp, weightLifting);
        assertNotEquals(trackRun, weightLifting);
    }

    @Test
    public void compareTo() throws ActivityException {
        final Activity a1 =
            new ActivityPushUp(Duration.ofMinutes(20), LocalDateTime.of(2024, 5, 6, 10, 0), 70, 15);
        final Activity a2 = new ActivityPushUp(Duration.ofMinutes(20),
                                               LocalDateTime.of(2024, 5, 6, 10, 10),
                                               70,
                                               15);
        final Activity a3 = new ActivityPushUp(Duration.ofMinutes(30),
                                               LocalDateTime.of(2024, 5, 6, 10, 10),
                                               90,
                                               15);
        final Activity a4 = new ActivityTrackRun(Duration.ofMinutes(30),
                                                 LocalDateTime.of(2024, 5, 6, 10, 10),
                                                 90,
                                                 10.0);

        assertTrue(a1.compareTo(a1) == 0);
        assertTrue(a1.compareTo(a2) < 0);
        assertTrue(a2.compareTo(a1) > 0);
        assertTrue(a1.compareTo(a3) < 0);
        assertTrue(a1.compareTo(a4) < 0);
        assertTrue(a3.compareTo(a4) != 0);
    }
}
