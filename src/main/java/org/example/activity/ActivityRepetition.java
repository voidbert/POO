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

public abstract class ActivityRepetition extends Activity {
    private int numberOfReps;

    public ActivityRepetition() {
        super();
        this.numberOfReps = 1;
    }

    public ActivityRepetition(
            Duration executionTime, LocalDateTime executionDate, int bpm, int numberOfReps) {

        super(executionTime, executionDate, bpm);
        this.setNumberOfReps(numberOfReps);
    }

    public ActivityRepetition(ActivityRepetition activity) {
        super(activity);
        this.numberOfReps = activity.getNumberOfReps();
    }

    public int getNumberOfReps() {
        return this.numberOfReps;
    }

    public void setNumberOfReps(int numberOfReps) {
        if (numberOfReps <= 0)
            throw new IllegalArgumentException("Number of reps should be a positive number!");
        this.numberOfReps = numberOfReps;
    }

    public abstract double countCalories(User user);

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || (this.getClass() != object.getClass())) return false;

        ActivityRepetition activity = (ActivityRepetition) object;
        return (super.equals(activity) && this.numberOfReps == activity.getNumberOfReps());
    }

    public abstract ActivityRepetition clone();

    public abstract String toString();
}