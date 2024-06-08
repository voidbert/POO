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

/** A repetetion activity that also involves wheights. */
public abstract class ActivityRepetitionWeighted extends ActivityRepetition {
    /** Heft of the weights, in kilograms. */
    private double weightsHeft;

    /** Creates a new empty repetition activity with weights. */
    public ActivityRepetitionWeighted() {
        super();
        this.weightsHeft = 1.0;
    }

    /**
     * Creates a new repetition activity with weights from the value of its fields.
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
    public ActivityRepetitionWeighted(Duration      executionTime,
                                      LocalDateTime executionDate,
                                      int           bpm,
                                      int           numberOfReps,
                                      double        weightsHeft) throws ActivityException {

        super(executionTime, executionDate, bpm, numberOfReps);
        this.setWeightsHeft(weightsHeft);
    }

    /**
     * Copy constructor of a repetition activity with weigths.
     *
     * @param activity Activity to be copied.
     */
    public ActivityRepetitionWeighted(ActivityRepetitionWeighted activity) {
        super(activity);
        this.weightsHeft = activity.getWeightsHeft();
    }

    /**
     * Gets the weights heft of this activity.
     *
     * @return The weigths heft of this activity.
     */
    public double getWeightsHeft() {
        return this.weightsHeft;
    }

    /**
     * Sets the weights heft of this activity.
     *
     * @param weightsHeft The weights heft of this activity.
     * @throws ActivityException <code>weightsHeft</code> isn't positive.
     */
    public void setWeightsHeft(double weightsHeft) throws ActivityException {
        if (weightsHeft <= 0)
            throw new ActivityException("Weights' heft should be positive!");
        this.weightsHeft = weightsHeft;
    }

    @Override
    public abstract double countCalories(User user);

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.weightsHeft);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || (this.getClass() != object.getClass()))
            return false;

        ActivityRepetitionWeighted activity = (ActivityRepetitionWeighted) object;
        return super.equals(activity) && this.weightsHeft == activity.getWeightsHeft();
    }

    @Override
    public abstract ActivityRepetitionWeighted clone();

    @Override
    public abstract String toString();
}
