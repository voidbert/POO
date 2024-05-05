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

public class ActivityWeightLifting extends ActivityRepetitionWeighted implements ActivityHard {
    public ActivityWeightLifting() {
        super();
    }

    public ActivityWeightLifting(
            Duration executionTime,
            LocalDateTime executionDate,
            int bpm,
            int numberOfReps,
            double weightsHeft) {

        super(executionTime, executionDate, bpm, numberOfReps, weightsHeft);
    }

    public ActivityWeightLifting(ActivityWeightLifting activity) {
        super(activity);
    }

    @Override
    public double countCalories(User user) {
        double MET; /* Metabolic Equivalent of Task */
        if (this.getNumberOfReps() <= 15) MET = 3.5;
        else if (this.getNumberOfReps() <= 30) MET = 5.0;
        else MET = 6.0;

        return MET * this.getBPM() * (this.getWeightsHeft() / 200.0) * user.getCalorieMultiplier();
    }

    @Override
    public ActivityWeightLifting clone() {
        return new ActivityWeightLifting(this);
    }

    @Override
    public String toString() {
        return String.format(
                "ActivityWeightLifting(executionTime = \"%s\", executionDate = \"%s\", bpm = %d, numberOfReps = %d, weightsHeft = %.2f)",
                this.getExecutionTime().toString(),
                this.getExecutionDate().toString(),
                this.getBPM(),
                this.getNumberOfReps(),
                this.getWeightsHeft());
    }
}