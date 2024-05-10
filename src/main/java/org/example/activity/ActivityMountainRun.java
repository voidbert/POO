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

/** A mountain run activity that can be executed by an user. */
public class ActivityMountainRun extends ActivityAltimetryDistance {
    /** Creates a new empty mountain run activity. */
    public ActivityMountainRun() {
        super();
    }

    /**
     * Creates a new mountain run activity from the value of its fields.
     *
     * @param executionTime Duration of the activity.
     * @param executionDate When the activity was / will be executed.
     * @param bpm Cardiac rhythm of the user while executing this activity.
     * @param distanceToTraverse Distance of the route to be traversed, in kilometers.
     * @param altimetry Altimetry difficulty level.
     * @throws ActivityException <code>executionTime</code> lasts less than a second.
     * @throws ActivityException <code>bpm</code> isn't positive.
     * @throws ActivityException <code>distanceToTraverse</code> isn't positive.
     * @throws ActivityException <code>altimetry</code> not in [0.0; 1.0].
     */
    public ActivityMountainRun(
            Duration executionTime,
            LocalDateTime executionDate,
            int bpm,
            double distanceToTraverse,
            double altimetry)
            throws ActivityException {

        super(executionTime, executionDate, bpm, distanceToTraverse, altimetry);
    }

    /**
     * Copy constructor of a mountain run activity.
     *
     * @param activity Activity to be copied.
     */
    public ActivityMountainRun(ActivityMountainRun activity) {
        super(activity);
    }

    @Override
    public double countCalories(User user) {
        double MET; /* Metabolic Equivalent of Task */
        double kmPerHour =
                this.getDistanceToTraverse() / (this.getExecutionTime().toSeconds() / 3600.0);

        if (kmPerHour <= 7.24) MET = 10.3;
        else if (kmPerHour <= 9.66) MET = 13.3;
        else MET = 15.5;

        return MET
                * this.getBPM()
                * (this.getExecutionTime().toSeconds() / 3600.0)
                * (1.0 + this.getAltimetry())
                * user.getCalorieMultiplier();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.getClass());
    }

    @Override
    public ActivityMountainRun clone() {
        return new ActivityMountainRun(this);
    }

    @Override
    public String toString() {
        return String.format(
                "ActivityMountainRun(executionTime = \"%s\", executionDate = \"%s\", bpm = %d, distanceToTraverse = %.3f, altimetry = %.3f)",
                this.getExecutionTime().toString(),
                this.getExecutionDate().toString(),
                this.getBPM(),
                this.getDistanceToTraverse(),
                this.getAltimetry());
    }
}
