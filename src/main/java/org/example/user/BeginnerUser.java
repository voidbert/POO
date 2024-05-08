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

import org.example.useractivities.UserActivities;

/** A beginner user of the fitness application. */
public class BeginnerUser extends User {
    /** Creates a new beginner intermediate user. */
    public BeginnerUser() {
        super();
    }

    /**
     * Creates a beginner user from the value of its fields.
     *
     * @param code Identifier code of the user.
     * @param name Full name of the user.
     * @param address Street address of the user.
     * @param email Email address of the user.
     * @param averageBPM Average cardiac rhythm of the user when exercising.
     * @param activities Activities the user must still execute and has already executed.
     */
    public BeginnerUser(
            long code,
            String name,
            String address,
            String email,
            int averageBPM,
            UserActivities activities) {
        super(code, name, address, email, averageBPM, activities);
    }

    /**
     * Copy constructor of a beginner user.
     *
     * @param user Beginner user to be copied.
     */
    public BeginnerUser(BeginnerUser user) {
        super(user);
    }

    public double getCalorieMultiplier() {
        return 1.0;
    }

    public BeginnerUser clone() {
        return new BeginnerUser(this);
    }

    public String toString() {
        return String.format(
                "BeginnerUser(code = %d, name = \"%s\", address = \"%s\", email = \"%s\", averageBPM = %d, activities = %s)",
                this.getCode(),
                this.getName(),
                this.getAddress(),
                this.getEmail(),
                this.getAverageBPM(),
                this.getActivities().toString());
    }
}
