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

/** An activity that involves distance. */
public abstract class ActivityDistance extends Activity {
    /** Distance of route to be traversed, in kilometers. */
    private double distanceToTraverse;

    /** Creates a new empty distance activity. */
    public ActivityDistance() {
        super();
        this.distanceToTraverse = 1.0;
    }

    /**
     * Creates a new distance activity from the value of its fields.
     *
     * @param executionTime Duration of the activity.
     * @param executionDate When the activity was / will be executed.
     * @param bpm Cardiac rhythm of the user while executing this activity.
     * @param distanceToTraverse Distance of the route to be traversed, in kilometers.
     * @throws ActivityException <code>executionTime</code> lasts less than a second.
     * @throws ActivityException <code>bpm</code> isn't positive.
     * @throws ActivityException <code>distanceToTraverse</code> isn't positive.
     */
    public ActivityDistance(Duration      executionTime,
                            LocalDateTime executionDate,
                            int           bpm,
                            double        distanceToTraverse) throws ActivityException {

        super(executionTime, executionDate, bpm);
        this.setDistanceToTraverse(distanceToTraverse);
    }

    /**
     * Copy constructor of a distance activity.
     *
     * @param activity Activity to be copied.
     */
    public ActivityDistance(ActivityDistance activity) {
        super(activity);
        this.distanceToTraverse = activity.getDistanceToTraverse();
    }

    /**
     * Gets the distance to traverse in this activity.
     *
     * @return The distance to traverese in this activity.
     */
    public double getDistanceToTraverse() {
        return this.distanceToTraverse;
    }

    /**
     * Sets the distance to traverse in this activity.
     *
     * @param distanceToTraverse The distance to traverse of this activity, in kilometers.
     * @throws ActivityException <code>distanceToTraverse</code> isn't positive.
     */
    public void setDistanceToTraverse(double distanceToTraverse) throws ActivityException {
        if (distanceToTraverse <= 0)
            throw new ActivityException("Distance to traverse should be positive!");
        this.distanceToTraverse = distanceToTraverse;
    }

    @Override
    public abstract double countCalories(User user);

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.distanceToTraverse);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || (this.getClass() != object.getClass()))
            return false;

        ActivityDistance activity = (ActivityDistance) object;
        return super.equals(activity) &&
            this.distanceToTraverse == activity.getDistanceToTraverse();
    }

    @Override
    public abstract ActivityDistance clone();

    @Override
    public abstract String toString();
}
