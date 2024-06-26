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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntermediateUserTest {
    private User reference;

    public IntermediateUserTest() throws UserException {
        this.reference = new IntermediateUser(1,
                                              "Humberto Gomes",
                                              "UMinho",
                                              "a104348@alunos.uminho.pt",
                                              80,
                                              new UserActivities());
    }

    @Test
    public void testClone() {
        User copy = this.reference.clone();
        assertEquals(this.reference.getCode(), copy.getCode());
        assertEquals(this.reference.getName(), copy.getName());
        assertEquals(this.reference.getAddress(), (copy.getAddress()));
        assertEquals(this.reference.getEmail(), copy.getEmail());
        assertEquals(this.reference.getAverageBPM(), copy.getAverageBPM());
    }

    @Test
    public void testToString() {
        assertEquals(
            this.reference.toString(),
            "IntermediateUser(code = 1, name = \"Humberto Gomes\", address = \"UMinho\", email = \"a104348@alunos.uminho.pt\", averageBPM = 80, activities = UserActivities(todo = [], done = [], plan = TrainingPlan(activities = {}, repetitions = [])))");
    }

    @Test
    public void testSerialize() {
        TestUtils.serialize(this.reference);
    }
}
