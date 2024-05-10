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

import java.util.Objects;
import java.util.function.Consumer;

/** An entry in the menu. */
public class MenuEntry {
    /** Entry text to be displayed. */
    private String text;

    /** Function to be executed when this entry is choosen. */
    private Consumer<Integer> handler;

    /** Creates a new empty menu entry. */
    public MenuEntry() {
        this.text = "";
        this.handler = (i) -> {};
    }

    /**
     * Creates a new menu entry from the value of its fields.
     *
     * @param text Text to be displayed.
     * @param handler Function to be executed when this entry is choosen.
     */
    public MenuEntry(String text, Consumer<Integer> handler) {
        this.text = text;
        this.handler = handler;
    }

    /**
     * Copy constructor of a menu entry.
     *
     * @param entry Menu entry to be copied.
     */
    public MenuEntry(MenuEntry entry) {
        this.text = entry.getText();
        this.handler = entry.handler;
    }

    /**
     * Gets this entry's display text.
     *
     * @return This entry's display text.
     */
    public String getText() {
        return this.text;
    }

    /**
     * Gets this entry's handler method.
     *
     * @return This entry's handler method.
     */
    public Consumer<Integer> getHandler() {
        return this.handler;
    }

    /**
     * Sets this menu entry's display text.
     *
     * @param text Display text of this menu entry.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Sets this menu entry's handler.
     *
     * @param handler Handler of this menu entry.
     */
    public void setHandler(Consumer<Integer> handler) {
        this.handler = handler;
    }

    /**
     * Calculates the hash code of this menu entry.
     *
     * @return The hash code of this menu entry.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.text, this.handler);
    }

    /**
     * Checks if this menu entry is equal to another object.
     *
     * @param obj Object to be compared with this menu entry.
     * @return Whether <code>this</code> is equal to <code>obj</code>.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        MenuEntry entry = (MenuEntry) obj;
        return this.text.equals(entry.getText()) && this.handler.equals(entry.getHandler());
    }

    /**
     * Creates a deep copy of this menu entry.
     *
     * @return A deep copy of this menu entry.
     */
    @Override
    public MenuEntry clone() {
        return new MenuEntry(this);
    }

    /**
     * Creates a debug string representation of this menu entry.
     *
     * @return A debug string representation of this menu entry.
     */
    @Override
    public String toString() {
        return String.format(
                "MenuEntry(text = \"%s\", handler = %s)", this.text, this.handler.toString());
    }
}
