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
import java.util.Objects;

/** A weight lifting activity that can be executed by an user. */
public class ActivityWeightLifting extends ActivityRepetitionWeighted {
    /** Creates a new empty weight lifting activity. */
    public ActivityWeightLifting() {
        super();
    }

    /**
     * Creates a new weight lifting activity from the value of its fields.
     *
     * @param executionTime Duration of the activity.
     * @param executionDate When the activity was / will be executed.
     * @param bpm Cardiac rhythm of the user while executing this activity.
     * @param numberOfReps Number of repetitions in a set.
     * @param weightsHeft Heft of the weigths, in kilograms.
     * @throws ActivityException <code>executionTime</code> lasts less than a second.
     * @throws ActivityException <code>bpm</code> isn't positive.
     * @throws ActivityException <code>numberOfReps</code> isn't positive.
     * @throws ActivityException <code>weightsHeft</code> isn't positive.
     */
    public ActivityWeightLifting(Duration      executionTime,
                                 LocalDateTime executionDate,
                                 int           bpm,
                                 int           numberOfReps,
                                 double        weightsHeft) throws ActivityException {

        super(executionTime, executionDate, bpm, numberOfReps, weightsHeft);
    }

    /**
     * Copy constructor of a weight lifiting activity.
     *
     * @param activity Activity to be copied.
     */
    public ActivityWeightLifting(ActivityWeightLifting activity) {
        super(activity);
    }

    @Override
    public double countCalories(User user) {
        double MET; /* Metabolic Equivalent of Task */
        if (this.getNumberOfReps() <= 15)
            MET = 3.5;
        else if (this.getNumberOfReps() <= 30)
            MET = 5.0;
        else
            MET = 6.0;

        return MET * this.getBPM() * (this.getWeightsHeft() / 200.0) * user.getCalorieMultiplier();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.getClass());
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
