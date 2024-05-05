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

import java.time.Duration;
import java.time.LocalDateTime;
import org.example.user.User;

public class ActivityPushUp extends ActivityRepetition {
    public ActivityPushUp() {
        super();
    }

    public ActivityPushUp(
            Duration executionTime, LocalDateTime executionDate, int bpm, int numberOfReps) {

        super(executionTime, executionDate, bpm, numberOfReps);
    }

    public ActivityPushUp(ActivityPushUp activity) {
        super(activity);
    }

    @Override
    public double countCalories(User user) {
        double MET; /* Metabolic Equivalent of Task */
        if (this.getNumberOfReps() <= 40) MET = 3.8;
        else MET = 7.5;

        return MET
                * this.getBPM()
                * (this.getExecutionTime().toSeconds() / 3600.0)
                * user.getCalorieMultiplier();
    }

    @Override
    public ActivityPushUp clone() {
        return new ActivityPushUp(this);
    }

    @Override
    public String toString() {
        return String.format(
                "ActivityPushUp(executionTime = \"%s\", executionDate = \"%s\", bpm = %d, numberOfReps = %d)",
                this.getExecutionTime().toString(),
                this.getExecutionDate().toString(),
                this.getBPM(),
                this.getNumberOfReps());
    }
}