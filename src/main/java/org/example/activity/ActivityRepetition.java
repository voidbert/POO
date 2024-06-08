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

/** An activity that involves repeating an exercise many times. */
public abstract class ActivityRepetition extends Activity {
    /** Number of repetitions in a set. */
    private int numberOfReps;

    /** Creates a new empty repetition activity. */
    public ActivityRepetition() {
        super();
        this.numberOfReps = 1;
    }

    /**
     * Creates a new repetition activity from the value of its fields.
     *
     * @param executionTime Duration of the activity.
     * @param executionDate When the activity was / will be executed.
     * @param bpm Cardiac rhythm of the user while executing this activity.
     * @param numberOfReps Number of repetitions in a set.
     * @throws ActivityException <code>executionTime</code> lasts less than a second.
     * @throws ActivityException <code>bpm</code> isn't positive.
     * @throws ActivityException <code>numberOfReps</code> isn't positive.
     */
    public ActivityRepetition(Duration      executionTime,
                              LocalDateTime executionDate,
                              int           bpm,
                              int           numberOfReps) throws ActivityException {

        super(executionTime, executionDate, bpm);
        this.setNumberOfReps(numberOfReps);
    }

    /**
     * Copy constructor of a repetition activity.
     *
     * @param activity Activity to be copied.
     */
    public ActivityRepetition(ActivityRepetition activity) {
        super(activity);
        this.numberOfReps = activity.getNumberOfReps();
    }

    /**
     * Gets the number of repetitions of this activity.
     *
     * @return The number of repetitions of this activity.
     */
    public int getNumberOfReps() {
        return this.numberOfReps;
    }

    /**
     * Sets the number of repetitions of this activity.
     *
     * @param numberOfReps The number of repetitions of this activity.
     * @throws ActivityException <code>numberOfReps</code> isn't positive.
     */
    public void setNumberOfReps(int numberOfReps) throws ActivityException {
        if (numberOfReps <= 0)
            throw new ActivityException("Number of repetitions should be positive!");
        this.numberOfReps = numberOfReps;
    }

    @Override
    public abstract double countCalories(User user);

    @Override
    public int hashCode() {
        return Objects.hash(Integer.valueOf(super.hashCode()), Integer.valueOf(this.numberOfReps));
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || (this.getClass() != object.getClass()))
            return false;

        ActivityRepetition activity = (ActivityRepetition) object;
        return super.equals(activity) && this.numberOfReps == activity.getNumberOfReps();
    }

    @Override
    public abstract ActivityRepetition clone();

    @Override
    public abstract String toString();
}
