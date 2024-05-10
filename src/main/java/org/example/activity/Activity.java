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

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

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
        this.executionTime = Duration.ofSeconds(1);
        this.executionDate = LocalDateTime.MIN;
        this.bpm = 1;
    }

    /**
     * Creates a new activity from the value of its fields.
     *
     * @param executionTime Duration of the activity.
     * @param executionDate When the activity was / will be executed.
     * @param bpm Cardiac rhythm of the user while executing this activity.
     * @throws ActivityException <code>executionTime</code> lasts less than a second.
     * @throws ActivityException <code>bpm</code> isn't positive.
     */
    public Activity(Duration executionTime, LocalDateTime executionDate, int bpm)
            throws ActivityException {
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
     * Gets the time when this activity was / will be completed.
     *
     * @return The time when this activity was / will be completed.
     */
    public LocalDateTime getEndDate() {
        return this.executionDate.plusSeconds(this.executionTime.toSeconds());
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
     * @throws ActivityException <code>executionTime</code> lasts less than a second.
     */
    public void setExecutionTime(Duration executionTime) throws ActivityException {
        if (executionTime.toSeconds() == 0)
            throw new ActivityException("An activity should be at least one second long!");
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
     * @throws ActivityException <code>bpm</code> isn't positive.
     */
    public void setBPM(int bpm) throws ActivityException {
        if (bpm <= 0) throw new ActivityException("Average BPM during exercise must be positive!");
        this.bpm = bpm;
    }

    /**
     * Checks if this activity overlaps another activity.
     *
     * @param activity Activity to be checked for overlapping.
     * @return Whether if this activity overlaps another activity.
     */
    public boolean overlaps(Activity activity) {
        LocalDateTime thisStart = this.executionDate;
        LocalDateTime thisEnd = this.getEndDate();
        LocalDateTime activityStart = activity.getExecutionDate();
        LocalDateTime activityEnd = activity.getEndDate();

        return thisStart.isBefore(activityEnd) && activityStart.isBefore(thisEnd);
    }

    /**
     * Counts the calories that the user executing this activity burns.
     *
     * @param user User executing this activity.
     * @return The calories that the user executing this activity burns.
     */
    public abstract double countCalories(User user);

    /**
     * Calculates the hash code of this activity.
     *
     * @return The hash code of this activity.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.executionTime, this.executionDate, this.bpm);
    }

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
     * Compares this activity to another one, sorting them by execution date, and then by duration.
     *
     * @param obj Object to be compared to this activity.
     * @return See <code>Comparable.compareTo</code>.
     */
    public int compareTo(Object obj) {
        // Purposely fail with exception on wrong type
        Activity activity = (Activity) obj;

        int dateCompare = this.executionDate.compareTo(activity.getExecutionDate());
        int durationCompare = this.executionTime.compareTo(activity.getExecutionTime());
        if (dateCompare != 0) {
            return dateCompare;
        } else if (durationCompare != 0) {
            return durationCompare;
        } else if (this.equals(activity)) {
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
    @Override
    public abstract Activity clone();

    /**
     * Creates a debug string representation of this activity.
     *
     * @return A debug string representation of this activity.
     */
    @Override
    public abstract String toString();
}
