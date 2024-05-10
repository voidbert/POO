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

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** An options menu for the application. */
public class Menu {
    /** Entries in this menu. */
    private MenuEntry[] entries;

    /** Creates a new empty menu. */
    public Menu() {
        this.entries = new MenuEntry[0];
    }

    /**
     * Creates a menu from the value of its fields.
     *
     * @param entries Entries in the menu.
     */
    public Menu(MenuEntry[] entries) {
        this.setEntries(entries);
    }

    /**
     * Copy constructor of a menu.
     *
     * @param menu Menu to be copied.
     */
    public Menu(Menu menu) {
        this.entries = menu.getEntries();
    }

    /**
     * Gets this menu's entries.
     *
     * @return This menu's entries.
     */
    public MenuEntry[] getEntries() {
        return Arrays.stream(this.entries).map(MenuEntry::clone).toArray(MenuEntry[]::new);
    }

    /**
     * Sets this menu's entries.
     *
     * @param enries This menu's entries.
     */
    public void setEntries(MenuEntry[] entries) {
        this.entries = Arrays.stream(entries).map(MenuEntry::clone).toArray(MenuEntry[]::new);
    }

    /** Runs this menu. */
    public void run() {
        Scanner input = new Scanner(System.in);

        System.out.println("Choose an option ...\n");
        for (int i = 0; i < this.entries.length; ++i) {
            System.out.println(String.format("  %d -> %s", i + 1, entries[i].getText()));
        }
        System.out.println("");

        boolean success = false;
        do {
            try {
                System.out.print("> ");
                int option = input.nextInt();
                if (option > 0 && option <= this.entries.length) {
                    this.entries[option - 1].getHandler().accept(option - 1);
                    success = true;
                }
            } catch (InputMismatchException e) {
                input.nextLine();
            } catch (NoSuchElementException e) {
                success = true; // User wants to leave (Ctrl + D)
            }

            if (!success) {
                System.err.println("Not an integer not in range!");
            }
        } while (!success);

        input.close();
    }

    /**
     * Calculates the hash code of this menu.
     *
     * @return The hash code of this menu.
     */
    @Override
    public int hashCode() {
        return this.entries.hashCode();
    }

    /**
     * Checks if this menu is equal to another object.
     *
     * @param obj Object to be compared with this menu.
     * @return Whether <code>this</code> is equal to <code>obj</code>.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        Menu menu = (Menu) obj;
        return Arrays.equals(this.entries, menu.getEntries());
    }

    /**
     * Creates a deep copy of this menu.
     *
     * @return A deep copy of this menu.
     */
    @Override
    public Menu clone() {
        return new Menu(this);
    }
}
