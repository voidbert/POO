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
import java.util.Objects;
import org.example.user.User;

/** A distance activity with altimetry that can be executed by an user. */
public abstract class ActivityAltimetryDistance extends ActivityDistance {
    /** Altimetry difficulty value. */
    private double altimetry;

    /** Creates a new empty distance activity with altimetry. */
    public ActivityAltimetryDistance() {
        super();
        this.altimetry = 0.0;
    }

    /**
     * Creates a new distance activity with altimetry from the value of its fields.
     *
     * @param executionTime Duration of the activity.
     * @param executionDate When the activity was / will be executed.
     * @param bpm Cardiac rhythm of the user while executing this activity.
     * @param distanceToTraverse Distance of the route to be traversed, in kilometers.
     * @param altimetry Altimetry difficulty level.
     * @throws IllegalArgumentException <code>distanceToTraverse</code> isn't a positive number.
     * @throws IllegalArgumentException <code>altimetry</code> isn't in [0.0; 1.0].
     */
    public ActivityAltimetryDistance(
            Duration executionTime,
            LocalDateTime executionDate,
            int bpm,
            double distanceToTraverse,
            double altimetry) {

        super(executionTime, executionDate, bpm, distanceToTraverse);
        this.setAltimetry(altimetry);
    }

    /**
     * Copy constructor of a distance activity with altimetry.
     *
     * @param activity Activity to be copied.
     */
    public ActivityAltimetryDistance(ActivityAltimetryDistance activity) {
        super(activity);
        this.altimetry = activity.getAltimetry();
    }

    /**
     * Gets the altimetry difficulty level of this activity.
     *
     * @return The altimetry difficulty level of this activity.
     */
    public double getAltimetry() {
        return this.altimetry;
    }

    /**
     * Sets the altimetry difficulty level of this activity.
     *
     * @param altimetry Altimetry difficulty level of this activity.
     * @throws IllegalArgumentException <code>altimetry</code> isn't in [0.0; 1.0].
     */
    public void setAltimetry(double altimetry) {
        if (altimetry < 0.0 || altimetry > 1.0)
            throw new IllegalArgumentException("Altimetry of activity must be in [0.0; 1.0]!");
        this.altimetry = altimetry;
    }

    public abstract double countCalories(User user);

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || (this.getClass() != object.getClass())) return false;

        ActivityAltimetryDistance activity = (ActivityAltimetryDistance) object;
        return (super.equals(activity) && this.altimetry == activity.getAltimetry());
    }

    @Override
    public int hashCode() {
        return Objects.hash(Integer.valueOf(super.hashCode()), Double.valueOf(this.altimetry));
    }

    public abstract ActivityAltimetryDistance clone();

    public abstract String toString();
}
