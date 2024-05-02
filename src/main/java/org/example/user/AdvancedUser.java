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

public class AdvancedUser extends User {
    public AdvancedUser() {
        super();
    }

    public AdvancedUser(long code, String name, String address, String email, int averageBPM) {
        super(code, name, address, email, averageBPM);
    }

    public AdvancedUser(AdvancedUser user) {
        super(user);
    }

    public double getCalorieMultiplier() {
        return 1.5;
    }

    public User clone() {
        return new AdvancedUser(this);
    }

    public String toString() {
        return String.format(
                "AdvancedUser(code = %d, name = \"%s\", address = \"%s\", email = \"%s\", averageBPM = %d)",
                this.getCode(),
                this.getName(),
                this.getAddress(),
                this.getEmail(),
                this.getAverageBPM());
    }
}
