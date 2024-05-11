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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;
import org.junit.jupiter.api.Test;

public class FitnessModelTest {
    private FitnessModel fitness;

    public FitnessModelTest() throws UserException {
        User u1 =
                new BeginnerUser(
                        1,
                        "Humberto Gomes",
                        "UMinho",
                        "a104348@alunos.uminho.pt",
                        90,
                        new UserActivities());
        User u2 =
                new IntermediateUser(
                        2,
                        "José Lopes",
                        "UMinho",
                        "a104541@alunos.uminho.pt",
                        80,
                        new UserActivities());
        User u3 =
                new AdvancedUser(
                        2,
                        "Diogo Barros",
                        "UMinho",
                        "a10????@alunos.uminho.pt",
                        95,
                        new UserActivities());

        Map<Long, User> users = new TreeMap<Long, User>();
        users.put(1L, u1);
        users.put(2L, u2);
        users.put(3L, u3);
        fitness = new FitnessModel(users, LocalDateTime.of(2024, 1, 1, 0, 0, 0));
    }

    @Test
    public void testClone() {
        FitnessModel copy = this.fitness.clone();
        assertEquals(this.fitness.getUsers(), copy.getUsers());
        assertEquals(this.fitness.getNow(), copy.getNow());
    }

    @Test
    public void serialize() {
        FitnessModel copy = fitness.clone();
        try {
            copy.saveToFile("/tmp/POO.bin");
            copy.loadFromFile("/tmp/POO.bin");
            assertEquals(fitness, copy);
        } catch (Exception e) {
            System.err.println(e.toString());
            assertTrue(false);
        }
    }
}
