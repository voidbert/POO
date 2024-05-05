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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class ActivityTest {
    private Activity activity = new ActivityPushUp();

    @Test
    public void testEquals() {
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
    public void setBPM() {
        assertThrows(
                RuntimeException.class,
                () -> {
                    this.activity.setBPM(0);
                });
        assertThrows(
                RuntimeException.class,
                () -> {
                    this.activity.setBPM(-1);
                });
        assertDoesNotThrow(
                () -> {
                    this.activity.setBPM(60);
                });
    }

    @Test
    public void setExecutionTime() {
        assertThrows(
                RuntimeException.class,
                () -> {
                    this.activity.setExecutionTime(Duration.ofMillis(999));
                });
        assertDoesNotThrow(
                () -> {
                    this.activity.setExecutionTime(Duration.ofSeconds(1));
                });
    }
}
