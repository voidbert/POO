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

/** A user of the fitness application. */
public abstract class User implements Comparable, Serializable {
    /** Identifier code of the user. */
    private long code;

    /** Full name of the user. */
    private String name;

    /** Street address of the user. */
    private String address;

    /** Email address of the user. */
    private String email;

    /** Average cardiac rhythm of the user when exercising. */
    private int averageBPM;

    /** Activities the user must still execute and has already executed. */
    private UserActivities activities;

    /** Creates a new empty user. */
    public User() {
        this.code = 0;
        this.name = this.address = this.email = "";
        this.averageBPM                       = 0;
    }

    /**
     * Creates a user from the value of its fields.
     *
     * @param code Identifier code of the user.
     * @param name Full name of the user.
     * @param address Street address of the user.
     * @param email Email address of the user.
     * @param averageBPM Average cardiac rhythm of the user when exercising.
     * @param activities Activities the user must still execute and has already executed.
     * @throws UserException Non-positive <code>averageBPM</code>.
     */
    public User(long           code,
                String         name,
                String         address,
                String         email,
                int            averageBPM,
                UserActivities activities) throws UserException {
        this.code    = code;
        this.name    = name;
        this.address = address;
        this.email   = email;
        this.setAverageBPM(averageBPM);
        this.activities = activities.clone();
    }

    /**
     * Copy constructor of a user.
     *
     * @param user User to be copied.
     */
    public User(User user) {
        this.code       = user.getCode();
        this.name       = user.getName();
        this.address    = user.getAddress();
        this.email      = user.getEmail();
        this.averageBPM = user.getAverageBPM();
        this.activities = user.getActivities();
    }

    /**
     * Gets this user's identifier code.
     *
     * @return This user's identifier code.
     */
    public long getCode() {
        return this.code;
    }

    /**
     * Gets this user's full name.
     *
     * @return This user's full name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets this user's street address.
     *
     * @return This user's street address.
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Gets this user's email address.
     *
     * @return This user's email address.
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Gets this user's average cardiac rhythm while exercising.
     *
     * @return This user's average cardiac rhythm while exercising.
     */
    public int getAverageBPM() {
        return this.averageBPM;
    }

    /**
     * Gets the activities the user must still execute and has already executed.
     *
     * @return The activities the user must still execute and has already executed.
     */
    public UserActivities getActivities() {
        return this.activities.clone();
    }

    /**
     * Sets this user's identifier code.
     *
     * @param code Identifier code of this user.
     */
    public void setCode(long code) {
        this.code = code;
    }

    /**
     * Sets this user's full name.
     *
     * @param name Full name of this user.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets this user's street address.
     *
     * @param address Street address of this user.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Sets this user's email address.
     *
     * @param email Email address of this user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets this user's average cardiac rhythm while exercising.
     *
     * @param bpm This user's average cardiac rhythm while exercising.
     * @throws UserException Non-positive <code>averageBPM</code>.
     */
    public void setAverageBPM(int bpm) throws UserException {
        if (bpm <= 0)
            throw new UserException("The average BPM of an user must be a positive number!");
        this.averageBPM = bpm;
    }

    /**
     * Sets the activities this user must still execute and has already executed.
     *
     * @param activities The activities this user must still execute and has already executed.
     */
    public void setActivities(UserActivities activities) {
        this.activities = activities.clone();
    }

    /**
     * Checks if this user is equal to another object.
     *
     * @param obj Object to be compared with this user.
     * @return Whether <code>this</code> is equal to <code>obj</code>.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;

        User user = (User) obj;
        return this.code == user.getCode() && this.name.equals(user.getName()) &&
            this.address.equals(user.getAddress()) && this.email.equals(user.getEmail()) &&
            this.averageBPM == user.getAverageBPM() && this.activities.equals(user.getActivities());
    }

    /**
     * Compares this user to another one, sorting them by identifier code first and then name.
     *
     * @param obj Object to be compared to this user.
     * @return See <code>Comparable.compareTo</code>.
     */
    public int compareTo(Object obj) {
        // Purposely fail with exception on wrong type
        return Long.compare(this.code, ((User) obj).getCode());
    }

    /**
     * Calculates the hash code of this user. Only the user's identifier code is considered for hash
     * code calculation.
     *
     * @return The hash code of this user.
     */
    @Override
    public int hashCode() {
        return Long.hashCode(this.code);
    }

    /**
     * Gets the multiplier by which activities' calories should be multiplied.
     *
     * @return The multiplier by which which activities' calories should be multiplied.
     */
    public abstract double getCalorieMultiplier();

    /**
     * Creates a deep copy of this user.
     *
     * @return A deep copy of this user.
     */
    @Override
    public abstract User clone();

    /**
     * Creates a debug string representation of this user.
     *
     * @return A debug string representation of this user.
     */
    @Override
    public abstract String toString();
}
