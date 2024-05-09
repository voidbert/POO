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

package org.example;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.example.user.User;

/** A fitness application. */
public class Fitness implements Serializable {
    /** Users in the application (each user contains its activities aswell). */
    private SortedMap<Integer, User> users;

    /** Current time in this application. */
    private LocalDateTime now;

    /** Creates a new empty fitness application. */
    public Fitness() {
        this.users = new TreeMap<Integer, User>();
        this.now = LocalDateTime.now();
    }

    /**
     * Creates a new fitness application from the value of its fields.
     *
     * @param users Users in the applications.
     * @param now Current time in this applications.
     */
    public Fitness(Map<Integer, User> users, LocalDateTime now) {
        this.setUsers(users);
        this.now = now;
    }

    /**
     * Copy constructor of a fitness application.
     *
     * @param fitness Fitness application to be copied.
     */
    public Fitness(Fitness fitness) {
        this.users = fitness.getUsers();
        this.now = fitness.getNow();
    }

    /**
     * Gets the users (and their activities) in this fitness application.
     *
     * @return The users (and their activities) in this fitness application.
     */
    public SortedMap<Integer, User> getUsers() {
        return this.users.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                e -> e.getKey(),
                                e -> e.getValue().clone(),
                                (o1, o2) -> o1,
                                TreeMap::new));
    }

    /**
     * Sets the users (and their activities) in this fitness application.
     *
     * @param users The users (and their activities) in this fitness application.
     */
    public void setUsers(Map<Integer, User> users) {
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
     * Gets the current time in this application.
     *
     * @return The current time in this application.
     */
    public LocalDateTime getNow() {
        return this.now;
    }

    /**
     * Loads the data of this application from a file.
     *
     * @param path Path to the file.
     */
    public void loadFromFile(String path)
            throws IOException, ClassNotFoundException, ClassCastException {
        FileInputStream fileStream = new FileInputStream(path);
        ObjectInputStream objectStream = new ObjectInputStream(fileStream);
        Fitness fitness = (Fitness) objectStream.readObject();
        objectStream.close();

        this.users = fitness.users;
        this.now = fitness.now;
    }

    /**
     * Saves the data of this application to a file.
     *
     * @param path Path to the file.
     */
    public void saveToFile(String path) throws IOException, ClassNotFoundException {
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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        Fitness fitness = (Fitness) obj;
        return this.users.equals(fitness.getUsers()) && this.now.equals(fitness.getNow());
    }

    /**
     * Calculates the hash code of this fitness application.
     *
     * @return The hash code of this fitness application.
     */
    public int hashCode() {
        return Objects.hash(this.users, this.now);
    }

    /**
     * Creates a deep copy of this fitness application.
     *
     * @return A deep copy of this fitness application.
     */
    public Fitness clone() {
        return new Fitness(this);
    }

    /**
     * Creates a debug string representation of this fitness applcation.
     *
     * @return A debug string representation of this fitness application.
     */
    public String toString() {
        return "Fitness(...)";
    }
}
