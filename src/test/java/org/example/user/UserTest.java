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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class UserTest {
    private User reference;

    public UserTest() throws UserException {
        this.reference =
                new IntermediateUser(
                        1,
                        "José Lopes",
                        "UMinho",
                        "a104541@alunos.uminho.pt",
                        80,
                        new UserActivities());
    }

    @Test
    public void testEquals() throws UserException {
        // Test 1
        final User beginner =
                new BeginnerUser(
                        1,
                        "Humberto Gomes",
                        "UMinho",
                        "a104348@alunos.uminho.pt",
                        80,
                        new UserActivities());
        final User intermediate =
                new IntermediateUser(
                        1,
                        "Humberto Gomes",
                        "UMinho",
                        "a104348@alunos.uminho.pt",
                        80,
                        new UserActivities());
        final User advanced =
                new AdvancedUser(
                        1,
                        "Humberto Gomes",
                        "UMinho",
                        "a104348@alunos.uminho.pt",
                        80,
                        new UserActivities());

        assertNotEquals(beginner, intermediate);
        assertNotEquals(beginner, advanced);
        assertNotEquals(intermediate, advanced);

        // Test 2
        User copy = this.reference.clone();
        assertEquals(copy, this.reference);

        copy.setCode(2);
        assertNotEquals(copy, this.reference);
    }

    @Test
    public void testHashCode() {
        User copy = this.reference.clone();

        assertEquals(this.reference.hashCode(), copy.hashCode());

        copy.setName("José Lopes");
        assertEquals(this.reference.hashCode(), copy.hashCode());

        copy.setCode(2);
        assertNotEquals(this.reference.hashCode(), copy.hashCode());
    }

    @Test
    public void setAverageBPM() {
        assertThrows(
                UserException.class,
                () -> {
                    this.reference.setAverageBPM(0);
                });
        assertThrows(
                UserException.class,
                () -> {
                    this.reference.setAverageBPM(-1);
                });
        assertDoesNotThrow(
                () -> {
                    this.reference.setAverageBPM(60);
                });
    }
}
