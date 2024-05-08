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

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import org.example.user.User;

/** An exercise activity that can be executed by an user. */
public abstract class Activity implements Serializable, Comparable {
    /** Duration of the activity. */
    private Duration executionTime;

    /** Time when the activity was / will be executed. */
    private LocalDateTime executionDate;

    /** Cardiac rhythm of the user while executing this activity. */
    private int bpm;

    /** Creates a new empty activity. */
    public Activity() {
        this.executionTime = Duration.ZERO;
        this.executionDate = LocalDateTime.MIN;
        this.bpm = 0;
    }

    /**
     * Creates a new activity from the value of its fields.
     *
     * @param executionTime Duration of the activity.
     * @param executionDate When the activity was / will be executed.
     * @param bpm Cardiac rhythm of the user while executing this activity.
     * @throws IllegalArgumentException <code>executionTime</code> lasts less than a second.
     * @throws IllegalArgumentException <code>bpm</code> isn't positive.
     */
    public Activity(Duration executionTime, LocalDateTime executionDate, int bpm) {
        this.setExecutionTime(executionTime);
        this.executionDate = executionDate;
        this.setBPM(bpm);
    }

    /**
     * Copy constructor of an activity.
     *
     * @param activity Activity to be copied.
     */
    public Activity(Activity activity) {
        this.executionTime = activity.getExecutionTime();
        this.executionDate = activity.getExecutionDate();
        this.bpm = activity.getBPM();
    }

    /**
     * Gets the duration of this activity.
     *
     * @return The duration of this activity.
     */
    public Duration getExecutionTime() {
        return this.executionTime;
    }

    /**
     * Gets the time when this activity was / will be executed.
     *
     * @return The time when this activity was / will be executed.
     */
    public LocalDateTime getExecutionDate() {
        return this.executionDate;
    }

    /**
     * Gets the cardiac rhythm of the user while executing this activity.
     *
     * @return The cardiac rhythm of the user while executing this activity.
     */
    public int getBPM() {
        return this.bpm;
    }

    /**
     * Sets the duration of this activity.
     *
     * @param executionTime The duration of this activity.
     * @throws IllegalArgumentException <code>executionTime</code> lasts less than a second.
     */
    public void setExecutionTime(Duration executionTime) {
        if (executionTime.toSeconds() == 0)
            throw new IllegalArgumentException("An exercise should last at least one second long!");
        this.executionTime = executionTime;
    }

    /**
     * Sets the time when this activity was / will be executed.
     *
     * @param executionDate The time when this activity was / will be executed.
     */
    public void setExecutionDate(LocalDateTime executionDate) {
        this.executionDate = executionDate;
    }

    /**
     * Sets the cardiac rhythm of the user while executing this activity.
     *
     * @param bpm the cardiac rhythm of the user while executing this activity.
     * @throws IllegalArgumentException <code>bpm</code> isn't positive.
     */
    public void setBPM(int bpm) throws IllegalArgumentException {
        if (bpm <= 0)
            throw new IllegalArgumentException(
                    "The average BPM during exercise must be a positive number!");
        this.bpm = bpm;
    }

    /**
     * Checks if this activity overlaps another activity.
     *
     * @param activity Activity to be checked for overlapping.
     * @return Whether if this activity overlaps another activity.
     */
    public boolean overlaps(Activity activity) {
        LocalDateTime thisStart = this.getExecutionDate();
        LocalDateTime thisEnd = thisStart.plusSeconds(this.executionTime.toSeconds());
        LocalDateTime activityStart = activity.getExecutionDate();
        LocalDateTime activityEnd =
                activityStart.plusSeconds(activity.getExecutionTime().toSeconds());

        return thisStart.isBefore(activityEnd) && activityStart.isBefore(thisEnd);
    }

    /**
     * Counts the calories that the user executing this activity consumes.
     *
     * @param user User executing this activity.
     * @return The calories that the user executing this activity consumes.
     */
    public abstract double countCalories(User user);

    /**
     * Checks if this activity is equal to another object.
     *
     * @param obj Object to be compared with this activity.
     * @return Whether <code>this</code> is equal to <code>obj</code>.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || (this.getClass() != obj.getClass())) return false;

        Activity activity = (Activity) obj;
        return (this.executionTime.equals(activity.getExecutionTime())
                && this.executionDate.equals(activity.getExecutionDate())
                && this.bpm == activity.getBPM());
    }

    /**
     * Calculates the hash code of this activity.
     *
     * @return The hash code of this activity.
     */
    public int hashCode() {
        return Objects.hash(this.executionTime, this.executionDate, Integer.valueOf(this.bpm));
    }

    /**
     * Compares this activity to another one, sorting them by execution date.
     *
     * @param obj Object to be compared to this activity.
     * @return 0 if the activities have the same date, 1 if
     */
    public int compareTo(Object obj) {
        Activity activity = (Activity) obj; /* This is supposed to fail with exception if need be */

        int dateCompare = this.executionDate.compareTo(activity.getExecutionDate());
        int durationCompare = this.executionTime.compareTo(activity.getExecutionTime());
        if (dateCompare != 0) {
            return dateCompare;
        } else if (durationCompare != 0) {
            return durationCompare;
        } else if (this.equals(obj)) {
            return 0;
        } else {
            /* Just don't return 0 if the activities are different. Ignore order */
            return 1;
        }
    }

    /**
     * Creates a deep copy of this activity.
     *
     * @return A deep copy of this activity.
     */
    public abstract Activity clone();

    /**
     * Creates a debug string representation of this activity.
     *
     * @return A debug string representation of this activity.
     */
    public abstract String toString();
}
