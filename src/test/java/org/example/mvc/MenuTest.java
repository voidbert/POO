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

import java.io.ByteArrayInputStream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MenuTest {
    @Test
    public void run() {
        int[]       chosen  = { -1 }; // Array wrapper so that lambdas can modify this integer
        MenuEntry[] entries = { new MenuEntry("Hello", (i) -> { chosen[0]   = i; }),
                                new MenuEntry("Goodbye", (i) -> { chosen[0] = i; }) };
        Menu        menu    = new Menu(entries);

        System.setIn(new ByteArrayInputStream("0\n1\n".getBytes()));
        menu.run();
        assertEquals(chosen[0], 0);

        System.setIn(new ByteArrayInputStream("1\n".getBytes()));
        menu.run();
        assertEquals(chosen[0], 0);

        System.setIn(new ByteArrayInputStream("2\n".getBytes()));
        menu.run();
        assertEquals(chosen[0], 1);

        System.setIn(new ByteArrayInputStream("3\n2\n".getBytes()));
        menu.run();
        assertEquals(chosen[0], 1);
    }
}
