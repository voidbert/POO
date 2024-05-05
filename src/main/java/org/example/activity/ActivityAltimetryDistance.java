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

public abstract class ActivityAltimetryDistance extends ActivityDistance {
    private double altimetry;

    public ActivityAltimetryDistance() {
        super();
        this.altimetry = 0.0;
    }

    public ActivityAltimetryDistance(
            Duration executionTime,
            LocalDateTime exexutionDate,
            int bpm,
            double distanceToTraverse,
            double altimetry) {

        super(executionTime, exexutionDate, bpm, distanceToTraverse);
        this.setAltimetry(altimetry);
    }

    public ActivityAltimetryDistance(ActivityAltimetryDistance activity) {
        super(activity);
        this.altimetry = activity.getAltimetry();
    }

    public double getAltimetry() {
        return this.altimetry;
    }

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

    public abstract ActivityAltimetryDistance clone();

    public abstract String toString();
}
