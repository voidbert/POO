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

public abstract class User implements Serializable {
    private long code;
    private String name, address, email;
    private int averageBPM;

    public User() {
        this.code = 0;
        this.name = this.address = this.email = "";
        this.averageBPM = 0;
    }

    public User(long code, String name, String address, String email, int averageBPM) {
        this.code = code;
        this.name = name;
        this.address = address;
        this.email = email;
        this.averageBPM = averageBPM;
    }

    public User(User user) {
        this.code = user.getCode();
        this.name = user.getName();
        this.address = user.getAddress();
        this.email = user.getEmail();
        this.averageBPM = user.getAverageBPM();
    }

    public long getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public String getEmail() {
        return this.email;
    }

    public int getAverageBPM() {
        return this.averageBPM;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAverageBPM(int bpm) {
        this.averageBPM = bpm;
    }

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

    public int hashCode() {
        return Long.hashCode(this.code);
    }

    public abstract double getCalorieMultiplier();

    public abstract User clone();

    public abstract String toString();
}
