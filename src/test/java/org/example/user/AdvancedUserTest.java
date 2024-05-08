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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.junit.jupiter.api.Test;

public class AdvancedUserTest {
    private final User reference =
            new AdvancedUser(1, "Humberto Gomes", "UMinho", "a104348@alunos.uminho.pt", 80);

    @Test
    public void testClone() {
        User copy = this.reference.clone();
        assertTrue(this.reference.getCode() == copy.getCode());
        assertTrue(this.reference.getName().equals(copy.getName()));
        assertTrue(this.reference.getAddress().equals(copy.getAddress()));
        assertTrue(this.reference.getEmail().equals(copy.getEmail()));
        assertTrue(this.reference.getAverageBPM() == copy.getAverageBPM());
    }

    @Test
    public void testEquals() {
        User copy = this.reference.clone();

        assertTrue(copy.equals(this.reference));
        copy.setCode(2);

        assertFalse(copy.equals(this.reference));
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
    public void testToString() {
        assertEquals(
                this.reference.toString(),
                "AdvancedUser(code = 1, name = \"Humberto Gomes\", address = \"UMinho\", email = \"a104348@alunos.uminho.pt\", averageBPM = 80)");
    }

    @Test
    public void testSerialize() {
        try {
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
            objectOutputStream.writeObject(this.reference);

            byte bytes[] = byteOutputStream.toByteArray();
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);

            User read = (User) objectInputStream.readObject();
            assertEquals(read, this.reference);
        } catch (IOException e) {
            assertTrue(false);
        } catch (ClassNotFoundException e) {
            assertTrue(false);
        }
    }
}
