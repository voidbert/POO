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

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class UserTest {
    @Test
    public void testEquals() {
        final User beginner =
                new BeginnerUser(1, "Humberto Gomes", "UMinho", "a104348@alunos.uminho.pt", 80);
        final User intermediate =
                new IntermediateUser(1, "Humberto Gomes", "UMinho", "a104348@alunos.uminho.pt", 80);
        final User advanced =
                new AdvancedUser(1, "Humberto Gomes", "UMinho", "a104348@alunos.uminho.pt", 80);

        assertNotEquals(beginner, intermediate);
        assertNotEquals(beginner, advanced);
        assertNotEquals(intermediate, advanced);
    }
}
