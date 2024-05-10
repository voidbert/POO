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
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * A training plan, composed of activities that can be executed multiple times on many days of the
 * week.
 */
public class TrainingPlan implements Serializable {
    /**
     * Activities in the plan, sorted by date. Activities executed multiple times are considered to
     * be done consecutively. The YYYY/MM/DD part of the date of activities will be replaced by
     * 0001/01/01.
     */
    private SortedMap<Activity, Integer> activities;

    /** Days of the week when this training plan is executed. */
    private SortedSet<DayOfWeek> repetitions;

    /** Creates a new empty training plan. */
    public TrainingPlan() {
        this.activities = new TreeMap<Activity, Integer>();
        this.repetitions = new TreeSet<DayOfWeek>();
    }

    /**
     * Creates a training plan from the values of its fields.
     *
     * @param activities Activities in the training plan, associated to the number of times they're
     *     executed. The YYYY/MM/DD part of the date of activities will be ignored.
     * @param repetitions Days of the week when this training plan is executed.
     * @throws ActivityOverlapException There are overlapping <code>activities</code>.
     */
    public TrainingPlan(Map<Activity, Integer> activities, Set<DayOfWeek> repetitions)
            throws ActivityOverlapException {
        this(); // Fix NullPointerException during this.getActivities in this.setActivities.
        this.setActivities(activities);
        this.setRepetitions(repetitions);
    }

    /**
     * Copy constructor of a training plan.
     *
     * @param plan Training plan to be copied.
     */
    public TrainingPlan(TrainingPlan plan) {
        this.activities = plan.getActivities();
        this.repetitions = plan.getRepetitions();
    }

    /**
     * Gets the activities in this training plan.
     *
     * @return The association between activities and the number of times they're executed, sorted
     *     by execution date. The YYYY/MM/DD part of the dates of activities will be 0001/01/01.
     */
    public SortedMap<Activity, Integer> getActivities() {
        return this.activities.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                e -> e.getKey().clone(),
                                e -> e.getValue(),
                                (o1, o2) -> o1,
                                TreeMap::new));
    }

    /**
     * Gets the days of the week when this training plan is executed.
     *
     * @return Set of week days when this training plan is executed.
     */
    public SortedSet<DayOfWeek> getRepetitions() {
        return new TreeSet<DayOfWeek>(this.repetitions);
    }

    /**
     * Sets the days of the week in which this training plan needs to be executed.
     *
     * @param repetitions The days of the week in which this training plan needs to be executed.
     */
    public void setRepetitions(Set<DayOfWeek> repetitions) {
        this.repetitions = new TreeSet<DayOfWeek>(repetitions);
    }

    /**
     * Checks if an internal activity overlaps with an activity already in this training plan.
     *
     * @param activity Activity to be checked for overlapping. Its date won't be checked to see if
     *     it matches any week day in which the training plan is executed.
     * @return Whether <code>activity</code> overlaps with any activity in this training plan.
     */
    private boolean overlapsDaytimeOnly(Activity activity) {
        Activity normalizedDateActivity = activity.clone();
        normalizedDateActivity.setExecutionDate(
                normalizedDateActivity
                        .getExecutionDate()
                        .withDayOfMonth(1)
                        .withMonth(1)
                        .withYear(1));
        Iterator<Map.Entry<Activity, Integer>> i = this.activities.entrySet().iterator();

        boolean overlaps = false;
        while (i.hasNext() && !overlaps) {
            Map.Entry<Activity, Integer> current = i.next();

            Activity repeatActivity = current.getKey().clone();
            Duration repeat =
                    Duration.ofSeconds(
                            repeatActivity.getExecutionTime().toSeconds() * current.getValue());

            try {
                repeatActivity.setExecutionTime(repeat);
            } catch (ActivityException e) {
            } // repeat > 0 because getExecutionTime() > 0

            if (repeatActivity.overlaps(normalizedDateActivity)) {
                overlaps = true;
            }
        }
        return overlaps;
    }

    /**
     * Checks if an external activity overlaps with an activity already in this training plan.
     *
     * @param activity Activity to be checked for overlapping. Its date will be checked to see if it
     *     matches any week day in which the training plan is executed.
     * @return Whether <code>activity</code> overlaps with any activity in this training plan.
     */
    public boolean overlaps(Activity activity) {
        if (!this.repetitions.contains(activity.getExecutionDate().getDayOfWeek())) return false;
        return this.overlapsDaytimeOnly(activity);
    }

    /**
     * Adds an activity to this training plan.
     *
     * @param activity Activity to be added to this training plan. Its YYYY/MM//DD will be ignored,
     *     leaving only the time of day.
     * @param times Number of times <code>activity</code> is executed consecutively.
     * @throws ActivityOverlapException <code>activity</code> overlaps with an activity already
     *     present in this plan.
     * @throws ActivityOverlapException <code>times</code> isn't positive. Negative overlap!
     */
    public void addActivity(Activity activity, Integer times) throws ActivityOverlapException {
        if (times <= 0) {
            throw new ActivityOverlapException(
                    "Non-positive number of activity executions! Negative overlap.");
        }

        Activity repeatActivity = activity.clone();
        Duration repeat = Duration.ofSeconds(activity.getExecutionTime().toSeconds() * times);
        try {
            repeatActivity.setExecutionTime(repeat);
        } catch (ActivityException e) {
        }

        repeatActivity.setExecutionDate(
                repeatActivity.getExecutionDate().withDayOfMonth(1).withMonth(1).withYear(1));
        if (this.overlapsDaytimeOnly(repeatActivity)) {
            throw new ActivityOverlapException();
        }

        try {
            repeatActivity.setExecutionTime(activity.getExecutionTime());
        } catch (ActivityException e) {
        } // getExecutionTime() > 0
        this.activities.put(repeatActivity, times);
    }

    /**
     * Removes an activity from the training plan.
     *
     * @param index Index of the activity in the training plan.
     * @throws ActivityDoesntExistException <code>index</code> out of range.
     */
    public void removeActivity(int index) throws ActivityDoesntExistException {
        boolean removed = false;
        int count = 0;
        Iterator<Activity> i = this.activities.keySet().iterator();
        while (i.hasNext() && !removed) {
            Activity a = i.next();
            if (count == index) {
                i.remove();
                removed = true;
            }
            count++;
        }

        if (!removed) {
            throw new ActivityDoesntExistException();
        }
    }

    /**
     * Sets the activities that compose this training plan.
     *
     * @param activities Activities in the training plan, associated to the number of times they're
     *     executed and sorted by execution date. The YYYY/MM/DD part of the date of activities will
     *     be ignored, leaving only the time of day.
     * @throws ActivityOverlapException Overlapping <code>activities</code>.
     */
    public void setActivities(Map<Activity, Integer> activities) throws ActivityOverlapException {
        SortedMap<Activity, Integer> previous = this.getActivities();
        this.activities = new TreeMap<Activity, Integer>();

        try {
            for (Map.Entry<Activity, Integer> current : activities.entrySet()) {
                this.addActivity(current.getKey(), current.getValue());
            }
        } catch (ActivityOverlapException e) {
            this.activities = previous;
            throw e;
        }
    }

    /**
     * Gets the activities in the training plan finished between two dates.
     *
     * @param now Starting date.
     * @param goal End date.
     * @return The set of activities finished between <code>now</code> and <code>goal</code>.
     */
    public SortedSet<Activity> activitiesBetween(LocalDateTime now, LocalDateTime goal) {
        SortedSet<Activity> ret = new TreeSet<Activity>();
        for (LocalDateTime d = now; d.isBefore(goal.plusDays(1)); d = d.plusDays(1)) {
            if (this.repetitions.contains(d.getDayOfWeek())) {
                for (Map.Entry<Activity, Integer> current : this.activities.entrySet()) {
                    for (int i = 0; i < current.getValue(); ++i) {
                        Activity a = current.getKey().clone();
                        a.setExecutionDate(
                                a.getExecutionDate()
                                        .withDayOfMonth(d.getDayOfMonth())
                                        .withMonth(d.getMonthValue())
                                        .withYear(d.getYear())
                                        .plusSeconds(a.getExecutionTime().toSeconds() * i));

                        LocalDateTime end = a.getEndDate();
                        if (end.isBefore(goal) || end.isEqual(goal)) {
                            ret.add(a);
                        }
                    }
                }
            }
        }
        return ret;
    }

    /**
     * Counts the calories burned by executing this training plan.
     *
     * @param user User executing the training plan.
     * @return The calories burned by executing this training plan.
     */
    public double countCalories(User user) {
        return this.activities.entrySet().stream()
                .mapToDouble(e -> e.getKey().countCalories(user) * e.getValue())
                .sum();
    }

    /**
     * Calculates the hash code of this training plan.
     *
     * @return The hash code of this training plan.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.activities, this.repetitions);
    }

    /**
     * Checks if this training plan is equal to another object.
     *
     * @param obj Object to be compared with this training plan.
     * @return Whether <code>this</code> is equal to <code>obj</code>.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        TrainingPlan plan = (TrainingPlan) obj;
        return this.activities.equals(plan.getActivities())
                && this.repetitions.equals(plan.getRepetitions());
    }

    /**
     * Creates a deep copy of this training plan.
     *
     * @return A deep copy of this training plan.
     */
    @Override
    public TrainingPlan clone() {
        return new TrainingPlan(this);
    }

    /**
     * Creates a debug string representation of this training plan.
     *
     * @return A debug string representation of this training plan.
     */
    @Override
    public String toString() {
        return String.format(
                "TrainingPlan(activities = %s, repetitions = %s)",
                this.activities.toString(), this.repetitions.toString());
    }
}
