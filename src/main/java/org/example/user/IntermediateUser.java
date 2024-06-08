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

/** An intermediate user of the fitness application. */
public class IntermediateUser extends User {
    /** Creates a new empty intermediate user. */
    public IntermediateUser() {
        super();
    }

    /**
     * Creates an intermediate user from the value of its fields.
     *
     * @param code Identifier code of the user.
     * @param name Full name of the user.
     * @param address Street address of the user.
     * @param email Email address of the user.
     * @param averageBPM Average cardiac rhythm of the user when exercising.
     * @param activities Activities the user must still execute and has already executed.
     * @throws UserException Non-positive <code>averageBPM</code>.
     */
    public IntermediateUser(long           code,
                            String         name,
                            String         address,
                            String         email,
                            int            averageBPM,
                            UserActivities activities) throws UserException {
        super(code, name, address, email, averageBPM, activities);
    }

    /**
     * Copy constructor of an intermediate user.
     *
     * @param user Intermediate user to be copied.
     */
    public IntermediateUser(IntermediateUser user) {
        super(user);
    }

    @Override
    public double getCalorieMultiplier() {
        return 1.25;
    }

    @Override
    public IntermediateUser clone() {
        return new IntermediateUser(this);
    }

    @Override
    public String toString() {
        return String.format(
            "IntermediateUser(code = %d, name = \"%s\", address = \"%s\", email = \"%s\", averageBPM = %d, activities = %s)",
            this.getCode(),
            this.getName(),
            this.getAddress(),
            this.getEmail(),
            this.getAverageBPM(),
            this.getActivities().toString());
    }
}
