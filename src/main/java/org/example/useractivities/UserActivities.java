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

package org.example.useractivities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.example.activity.Activity;

/**
 * The collection of activities a user can execute, composed of completed activities, activities to
 * be executed and a training plan.
 */
public class UserActivities implements Serializable {
    /** The activities the user still needs to execute. */
    private SortedSet<Activity> todo;

    /** The activities the user has already completed. */
    private SortedSet<Activity> done;

    /** The training plan the user is executing. */
    private TrainingPlan plan;

    /** Creates a new empty collection of user activities. */
    public UserActivities() {
        this.todo = new TreeSet<Activity>();
        this.done = new TreeSet<Activity>();
        this.plan = new TrainingPlan();
    }

    /**
     * Creates a new collection of user activities from its fields.
     *
     * @param todo The activities the user still needs to execute.
     * @param done The activities the user has already completed.
     * @param plan The training plan the user is executing.
     * @throws IllegalArgumentException There are overlapping in <code>todo</code> and / or <code>
     *     plan</code>. No overlapping checks are performed on <code>done</code>.
     */
    public UserActivities(SortedSet<Activity> todo, SortedSet<Activity> done, TrainingPlan plan) {
        this();
        this.setTodo(todo);
        this.setDone(done);
        this.setTrainingPlan(plan);
    }

    /**
     * Copy constructor of a collection of a user's activities.
     *
     * @param activities Collection of activities to be copied.
     */
    public UserActivities(UserActivities activities) {
        this.todo = activities.getTodo();
        this.done = activities.getDone();
        this.plan = activities.getTrainingPlan();
    }

    /**
     * Gets the activities this user still needs to execute.
     *
     * @return The activities this user still needs to execute.
     */
    public SortedSet<Activity> getTodo() {
        return this.todo.stream()
                .map(Activity::clone)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Gets the activities this user has already completed.
     *
     * @return The activities this user has already completed.
     */
    public SortedSet<Activity> getDone() {
        return this.done.stream()
                .map(Activity::clone)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Gets the training plan this user is currently executing.
     *
     * @return The training plan this user is currently executing.
     */
    public TrainingPlan getTrainingPlan() {
        return this.plan.clone();
    }

    /**
     * Sets the activities this user still has to execute.
     *
     * @param todo The activities this user still has to execute.
     * @throws IllegalArgumentException There are overlapping activities in <code>todo</code>.
     */
    public void setTodo(SortedSet<Activity> todo) {
        SortedSet<Activity> previous = this.getTodo();
        this.todo = new TreeSet<Activity>();

        try {
            for (Activity a : todo) {
                this.addActivity(a);
            }
        } catch (IllegalArgumentException e) {
            this.todo = previous;
            throw new IllegalArgumentException("Overlapping activities!");
        }
    }

    /**
     * Sets the activities this user still has already executed. No overlapping checks are
     * performed.
     *
     * @param done The activities this user has already executed.
     */
    public void setDone(SortedSet<Activity> done) {
        this.done =
                done.stream().map(Activity::clone).collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Sets the training plan this user is currently executing.
     *
     * @param plan The training plan this user is currently executing.
     * @throws IllegalArgumentException There are overlapping activities between <code>todo
     *     </code> and the isolated activities in this collection.
     */
    public void setTrainingPlan(TrainingPlan plan) {
        this.plan = plan.clone();
        this.setTodo(this.todo);
    }

    /**
     * Adds an activity to the collection of activities this user still has to execute.
     *
     * @param activity Activity to be added to the collection of activities this user still has to
     *     execute.
     * @throws IllegalArgumentException <code>activity</code> overlaps another activity in this
     *     collection.
     */
    public void addActivity(Activity activity) {
        if (this.plan.overlaps(activity)) {
            throw new IllegalArgumentException("Overlapping activities!");
        }

        for (Activity a : this.todo) {
            if (activity.overlaps(a)) {
                throw new IllegalArgumentException("Overlapping activities!");
            }
        }

        this.todo.add(activity.clone());
    }

    /**
     * Removes an activity from the collection of activities yet to be completed.
     *
     * @param index Index of the activity from the collection of activities yet to be completed.
     * @throws IndexOutOfBoundsException <code>index</code> out of range.
     */
    public void removeActivity(int index) {
        Activity toRemove = null;
        int count = 0;
        Iterator<Activity> i = this.todo.iterator();
        while (i.hasNext() && toRemove == null) {
            Activity a = i.next();
            if (count == index) {
                toRemove = a;
            }
            count++;
        }

        if (toRemove != null) {
            this.todo.remove(toRemove);
        } else {
            throw new IndexOutOfBoundsException("Activity to remove doesn't exist");
        }
    }

    /**
     * Advances time to a given date and updates which activities have and have not been completed.
     *
     * @param now Current application time.
     * @param goal Timestamp to leap to.
     */
    public void leapForward(LocalDateTime now, LocalDateTime goal) {
        List<Activity> toRemove = new ArrayList<Activity>();
        for (Activity a : this.todo) {
            LocalDateTime end = a.getExecutionDate().plusSeconds(a.getExecutionTime().toSeconds());

            if (end.isBefore(goal) || end.isEqual(goal)) {
                this.done.add(a);
                toRemove.add(a);
            }
        }

        this.todo.removeAll(toRemove);
        this.done.addAll(this.plan.activitiesBetween(now, goal));
    }

    /**
     * Calculates the hash code of this collection of user activities.
     *
     * @return The hash code of this collection of user activities.
     */
    public int hashCode() {
        return Objects.hash(this.todo, this.done, this.plan);
    }

    /**
     * Checks if two collections of user activities are equal.
     *
     * @return Whether two collections of user activities are equal.
     */
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        UserActivities activities = (UserActivities) obj;
        return this.todo.equals(activities.getTodo())
                && this.done.equals(activities.getDone())
                && this.plan.equals(activities.getTrainingPlan());
    }

    /**
     * Creates a deep copy of this collection of user activities.
     *
     * @return A deep copy of this collection of user activities.
     */
    public UserActivities clone() {
        return new UserActivities(this);
    }

    /**
     * Creates a debug string representation of this collection of user activities.
     *
     * @return A debug string representation of this collection of user activities.
     */
    public String toString() {
        return String.format(
                "UserActivities(todo = %s, done = %s, plan = %s)",
                this.todo.toString(), this.done.toString(), this.plan.toString());
    }
}
