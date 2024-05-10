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

/** A diamond push-up activity that can be executed by an user. */
public class ActivityDiamondPushUp extends ActivityRepetition implements ActivityHard {
    /** Creates a new empty diamond push-up activity. */
    public ActivityDiamondPushUp() {
        super();
    }

    /**
     * Creates a new diamond push-up activity from the value of its fields.
     *
     * @param executionTime Duration of the activity.
     * @param executionDate When the activity was / will be executed.
     * @param bpm Cardiac rhythm of the user while executing this activity.
     * @param numberOfReps Number of repetitions in a set.
     * @throws ActivityException <code>executionTime</code> lasts less than a second.
     * @throws ActivityException <code>bpm</code> isn't positive.
     * @throws ActivityException <code>numberOfReps</code> isn't positive.
     */
    public ActivityDiamondPushUp(
            Duration executionTime, LocalDateTime executionDate, int bpm, int numberOfReps)
            throws ActivityException {

        super(executionTime, executionDate, bpm, numberOfReps);
    }

    /**
     * Copy constructor of a diamond push-up activity.
     *
     * @param activity Activity to be copied.
     */
    public ActivityDiamondPushUp(ActivityDiamondPushUp activity) {
        super(activity);
    }

    @Override
    public double countCalories(User user) {
        double MET; /* Metabolic Equivalent of Task */
        if (this.getNumberOfReps() <= 40) MET = 4.5;
        else MET = 9;

        return MET
                * this.getBPM()
                * (this.getExecutionTime().toSeconds() / 3600.0)
                * user.getCalorieMultiplier();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.getClass());
    }

    @Override
    public ActivityDiamondPushUp clone() {
        return new ActivityDiamondPushUp(this);
    }

    @Override
    public String toString() {
        return String.format(
                "ActivityDiamondPushUp(executionTime = \"%s\", executionDate = \"%s\", bpm = %d, numberOfReps = %d)",
                this.getExecutionTime().toString(),
                this.getExecutionDate().toString(),
                this.getBPM(),
                this.getNumberOfReps());
    }
}
