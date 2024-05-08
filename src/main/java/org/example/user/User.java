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

package org.example.user;

import java.io.Serializable;

/** A user of the fitness application. */
public abstract class User implements Serializable {
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

    /** Creates a new empty user. */
    public User() {
        this.code = 0;
        this.name = this.address = this.email = "";
        this.averageBPM = 0;
    }

    /**
     * Creates a user from the value of its fields.
     *
     * @param code Identifier code of the user.
     * @param name Full name of the user.
     * @param address Street address of the user.
     * @param email Email address of the user.
     * @param averageBPM Average cardiac rhythm of the user when exercising.
     */
    public User(long code, String name, String address, String email, int averageBPM) {
        this.code = code;
        this.name = name;
        this.address = address;
        this.email = email;
        this.setAverageBPM(averageBPM);
    }

    /**
     * Copy constructor of a user.
     *
     * @param user User to be copied.
     */
    public User(User user) {
        this.code = user.getCode();
        this.name = user.getName();
        this.address = user.getAddress();
        this.email = user.getEmail();
        this.averageBPM = user.getAverageBPM();
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
     * Sets this user's identier code.
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
     */
    public void setAverageBPM(int bpm) {
        if (bpm <= 0)
            throw new IllegalArgumentException(
                    "The average BPM of an user must be a positive number!");
        this.averageBPM = bpm;
    }

    /**
     * Checks if this user is equal to another object.
     *
     * @param obj Object to be compared with this user.
     * @return Whether <code>this</code> is equal to <code>obj</code>.
     */
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        User user = (User) obj;
        return this.code == user.getCode()
                && this.name.equals(user.getName())
                && this.address.equals(user.getAddress())
                && this.email.equals(user.getEmail())
                && this.averageBPM == user.getAverageBPM();
    }

    /**
     * Calculates the hash code of this user.
     *
     * @return The hash code of this user.
     */
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
    public abstract User clone();

    /**
     * Creates a debug string representation of this user.
     *
     * @return A debug string representation of this user.
     */
    public abstract String toString();
}
