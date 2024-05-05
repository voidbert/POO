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

public abstract class ActivityDistance extends Activity {
    private double distanceToTraverse; /* In Kilometers */

    public ActivityDistance() {
        super();
        this.distanceToTraverse = 1.0;
    }

    public ActivityDistance(
            Duration executionTime,
            LocalDateTime executionDate,
            int bpm,
            double distanceToTraverse) {

        super(executionTime, executionDate, bpm);
        this.setDistanceToTraverse(distanceToTraverse);
    }

    public ActivityDistance(ActivityDistance activity) {
        super(activity);
        this.distanceToTraverse = activity.getDistanceToTraverse();
    }

    public double getDistanceToTraverse() {
        return this.distanceToTraverse;
    }

    public void setDistanceToTraverse(double distanceToTraverse) {
        if (distanceToTraverse <= 0)
            throw new IllegalArgumentException("Distance to traverse should be a positive number!");
        this.distanceToTraverse = distanceToTraverse;
    }

    public abstract double countCalories(User user);

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || (this.getClass() != object.getClass())) return false;

        ActivityDistance activity = (ActivityDistance) object;
        return (super.equals(activity)
                && this.distanceToTraverse == activity.getDistanceToTraverse());
    }

    public abstract ActivityDistance clone();

    public abstract String toString();
}
