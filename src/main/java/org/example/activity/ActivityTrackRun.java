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

public class ActivityTrackRun extends ActivityDistance implements ActivityHard {
    public ActivityTrackRun() {
        super();
    }

    public ActivityTrackRun(
            Duration executionTime,
            LocalDateTime executionDate,
            int bpm,
            double distanceToTraverse) {

        super(executionTime, executionDate, bpm, distanceToTraverse);
    }

    public ActivityTrackRun(ActivityTrackRun activity) {
        super(activity);
    }

    @Override
    public double countCalories(User user) {
        double MET; /* Metabolic Equivalent of Task */
        double kmPerHour =
                this.getDistanceToTraverse() / (this.getExecutionTime().toSeconds() / 3600.0);

        if (kmPerHour <= 6.7593) MET = 6.5;
        else if (kmPerHour <= 12.0701) MET = 11.8;
        else if (kmPerHour <= 15.4497) MET = 14.8;
        else MET = 18.0; /* High competition track racing */

        return MET * this.getBPM() * this.getDistanceToTraverse() * user.getCalorieMultiplier();
    }

    @Override
    public ActivityTrackRun clone() {
        return new ActivityTrackRun(this);
    }

    @Override
    public String toString() {
        return String.format(
                "ActivityTrackRun(executionTime = \"%s\", executionDate = \"%s\", bpm = %d, distanceToTraverse = %.3f)",
                this.getExecutionTime().toString(),
                this.getExecutionDate().toString(),
                this.getBPM(),
                this.getDistanceToTraverse());
    }
}