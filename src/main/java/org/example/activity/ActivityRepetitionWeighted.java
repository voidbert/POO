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

public abstract class ActivityRepetitionWeighted extends ActivityRepetition {
    private double weightsHeft; /* In kg */

    public ActivityRepetitionWeighted() {
        super();
        this.weightsHeft = 1.0;
    }

    public ActivityRepetitionWeighted(
            Duration executionTime,
            LocalDateTime executionDate,
            int bpm,
            int numberOfReps,
            double weightsHeft) {

        super(executionTime, executionDate, bpm, numberOfReps);
        this.setWeightsHeft(weightsHeft);
    }

    public ActivityRepetitionWeighted(ActivityRepetitionWeighted activity) {
        super(activity);
        this.weightsHeft = activity.getWeightsHeft();
    }

    public double getWeightsHeft() {
        return this.weightsHeft;
    }

    public void setWeightsHeft(double weightsHeft) {
        if (weightsHeft <= 0)
            throw new IllegalArgumentException("Weights' heft should be a positive number!");
        this.weightsHeft = weightsHeft;
    }

    public abstract double countCalories(User user);

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || (this.getClass() != object.getClass())) return false;

        ActivityRepetitionWeighted activity = (ActivityRepetitionWeighted) object;
        return (super.equals(activity) && this.weightsHeft == activity.getWeightsHeft());
    }

    public abstract ActivityRepetitionWeighted clone();

    public abstract String toString();
}
