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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/** A fitness application. */
public class FitnessModel implements Serializable {
    /** Users in the application (each user contains its activities as well). */
    private SortedMap<Long, User> users;

    /** Current time in this application. */
    private LocalDateTime now;

    /** The identifier code that will be attributed to the next user added. */
    private long nextUserCode;

    /** Creates a new empty fitness application. */
    public FitnessModel() {
        this.users = new TreeMap<Long, User>();
        this.now = LocalDateTime.now();
        this.nextUserCode = 1;
    }

    /**
     * Creates a new fitness application from the value of its fields.
     *
     * @param users Users in the applications.
     * @param now Current time in this applications.
     */
    public FitnessModel(Map<Long, User> users, LocalDateTime now) {
        this.setUsers(users);
        this.now = now;
        this.nextUserCode = 1;
    }

    /**
     * Copy constructor of a fitness application.
     *
     * @param fitness Fitness application to be copied.
     */
    public FitnessModel(FitnessModel fitness) {
        this.users = fitness.getUsers();
        this.now = fitness.getNow();
        this.nextUserCode = fitness.getNextUserCode();
    }

    /**
     * Gets the users (and their activities) in this fitness application.
     *
     * @return The users (and their activities) in this fitness application.
     */
    public SortedMap<Long, User> getUsers() {
        return this.users.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                e -> e.getKey(),
                                e -> e.getValue().clone(),
                                (o1, o2) -> o1,
                                TreeMap::new));
    }

    /**
     * Gets a user identified by its code.
     *
     * @param userCode Identifier code of the user.
     * @return The user of code <code>userCode</code>, <code>null</code> if not found.
     */
    public User getUser(long userCode) {
        User u = this.users.get(userCode);
        if (u == null) return null;
        else return u.clone();
    }

    /**
     * Gets the current time in this application.
     *
     * @return The current time in this application.
     */
    public LocalDateTime getNow() {
        return this.now;
    }

    /**
     * Gets the identifier code that will be attributed to the next user added.
     *
     * @return The identifier code that will be attributed to the next user added.
     */
    public long getNextUserCode() {
        return this.nextUserCode;
    }

    /**
     * Checks if there aren't users in the application.
     *
     * @return Whether there aren't users in the application.
     */
    public boolean isEmpty() {
        return this.users.isEmpty();
    }

    /**
     * Sets the users (and their activities) in this fitness application.
     *
     * @param users The users (and their activities) in this fitness application.
     */
    public void setUsers(Map<Long, User> users) {
        this.users =
                users.entrySet().stream()
                        .collect(
                                Collectors.toMap(
                                        e -> e.getKey(),
                                        e -> e.getValue().clone(),
                                        (o1, o2) -> o1,
                                        TreeMap::new));
    }

    /**
     * Adds an user to the fitness application.
     *
     * @param user User to be added. Its identifier code will be ignored.
     * @return The identifier code that was attributed to the user.
     */
    public long addUser(User user) {
        User toAdd = user.clone();
        toAdd.setCode(this.nextUserCode);
        this.users.put(this.nextUserCode, toAdd);
        this.nextUserCode++;
        return this.nextUserCode - 1;
    }

    /**
     * Sets the days in which a user's training plan is executed.
     *
     * @param userCode Identifier code of the user to be removed.
     * @param days Days in which the training plan is executed.
     * @throws FitnessModelException User not found.
     * @throws ActivityOverlapException Activity overlap.
     */
    public void setTrainingPlanDays(long userCode, SortedSet<DayOfWeek> days)
            throws FitnessModelException, ActivityOverlapException {
        User user = this.users.get(userCode);
        if (user == null) throw new FitnessModelException("User does not exist!");

        UserActivities activities = user.getActivities();
        TrainingPlan plan = activities.getTrainingPlan();
        plan.setRepetitions(days);
        activities.setTrainingPlan(plan);
        user.setActivities(activities);
    }

    /**
     * Removes a user from from the application if it exists.
     *
     * @param userCode Identifier code of the user to be removed.
     */
    public void removeUser(long userCode) {
        this.users.remove(userCode);
    }

    /**
     * Adds an isolated activity to an user.
     *
     * @param userCode Identifier code of the user to add the activity to.
     * @param activity Activity to be added to the user. Its bpm will be replaced by the user's.
     * @throws FitnessModelException User not found.
     * @throws FitnessModelException Activity starts before current date.
     * @throws ActivityOverlapException Activity overlaps with existing activities.
     */
    public void addActivity(long userCode, Activity activity)
            throws FitnessModelException, ActivityOverlapException {

        if (activity.getExecutionDate().isBefore(this.getNow()))
            throw new FitnessModelException("Activity added starts before current date!");

        User user = this.users.get(userCode);
        if (user == null) throw new FitnessModelException("User does not exist!");

        UserActivities activities = user.getActivities();
        Activity toAdd = activity.clone();
        try {
            toAdd.setBPM(user.getAverageBPM());
        } catch (ActivityException e) {
        } // Can't happen
        activities.addActivity(toAdd);
        user.setActivities(activities);
    }

    /**
     * Adds an isolated activity to an user's training plan.
     *
     * @param userCode Identifier code of the user to add the activity to.
     * @param activity Activity to be added to the user's training plan. Its bpm will be replaced by
     *     the user's and its YYYY/MM/DD part of the date will be ignored.
     * @param times Number of time activity was repeated.
     * @throws FitnessModelException User not found.
     * @throws ActivityOverlapException Activity overlaps with existing activities.
     */
    public void addActivityToTrainingPlan(long userCode, Activity activity, int times)
            throws FitnessModelException, ActivityOverlapException {

        User user = this.users.get(userCode);
        if (user == null) throw new FitnessModelException("User does not exist!");

        UserActivities activities = user.getActivities();
        TrainingPlan plan = activities.getTrainingPlan();
        Activity toAdd = activity.clone();
        try {
            toAdd.setBPM(user.getAverageBPM());
        } catch (ActivityException e) {
        } // Can't happen
        plan.addActivity(toAdd, times);
        activities.setTrainingPlan(plan);
        user.setActivities(activities);
    }

    /**
     * Advances time to another date, updating which activities have been completed.
     *
     * @param date Date to make the new current date.
     * @throws FitnessModelException Date not after current date.
     */
    public void leapForward(LocalDateTime date) throws FitnessModelException {
        if (!date.isAfter(this.now))
            throw new FitnessModelException("Date not after current date!");

        for (User u : this.users.values()) {
            UserActivities activities = u.getActivities();
            activities.leapForward(this.now, date);
            u.setActivities(activities);
        }
        this.now = date;
    }

    /**
     * Runs a query that consumes all users.
     *
     * @param query Query to be run.
     */
    public void runQuery(Consumer<User> query) {
        for (User u : this.users.values()) query.accept(u);
    }

    /**
     * Runs a query that consumes only one user.
     *
     * @param query Query to be run.
     * @param userCode Identifier code of the user to consider.
     * @throws FitnessModelException User not found.
     */
    public void runQuery(Consumer<User> query, long userCode) throws FitnessModelException {
        User user = this.users.get(userCode);
        if (user == null) throw new FitnessModelException("User does not exist!");
        query.accept(user);
    }

    /**
     * Loads the data of this application from a file.
     *
     * @param path Path to the file.
     * @throws IOException Failed to read file.
     * @throws ClassNotFoundException Bad file type.
     * @throws ClassCastException Bad file type.
     */
    public void loadFromFile(String path)
            throws IOException, ClassNotFoundException, ClassCastException {
        FileInputStream fileStream = new FileInputStream(path);
        ObjectInputStream objectStream = new ObjectInputStream(fileStream);
        FitnessModel fitness = (FitnessModel) objectStream.readObject();
        objectStream.close();

        this.users = fitness.getUsers();
        this.now = fitness.getNow();
        this.nextUserCode = fitness.getNextUserCode();
    }

    /**
     * Saves the data of this application to a file.
     *
     * @param path Path to the file.
     * @throws IOException Failed to write to fail.
     */
    public void saveToFile(String path) throws IOException {
        FileOutputStream fileStream = new FileOutputStream(path);
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
        objectStream.writeObject(this);
        objectStream.close();
        fileStream.close();
    }

    /**
     * Checks if this fitness application is equal to another object.
     *
     * @param obj Object to be compared with this fitness application.
     * @return Whether <code>this</code> is equal to <code>obj</code>.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        FitnessModel fitness = (FitnessModel) obj;
        return this.users.equals(fitness.getUsers())
                && this.now.equals(fitness.getNow())
                && this.nextUserCode == fitness.getNextUserCode();
    }

    /**
     * Calculates the hash code of this fitness application.
     *
     * @return The hash code of this fitness application.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.users, this.now, this.nextUserCode);
    }

    /**
     * Creates a deep copy of this fitness application.
     *
     * @return A deep copy of this fitness application.
     */
    @Override
    public FitnessModel clone() {
        return new FitnessModel(this);
    }

    /**
     * Creates a debug string representation of this fitness applcation.
     *
     * @return A debug string representation of this fitness application.
     */
    @Override
    public String toString() {
        return "FitnessModel(...)";
    }
}
