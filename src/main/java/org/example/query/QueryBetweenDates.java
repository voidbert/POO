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

package org.example.query;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Consumer;
import org.example.activity.Activity;
import org.example.user.User;

/** A query that only takes into account activities between two dates. */
public abstract class QueryBetweenDates implements Consumer<User> {
    /** Don't consider activities that ended before this date. */
    private LocalDateTime start;

    /** Don't consider activities that started after this date. */
    private LocalDateTime end;

    /** Creates a new query without date restrictions. */
    public QueryBetweenDates() {
        this.start = LocalDateTime.MIN;
        this.end = LocalDateTime.MAX;
    }

    /**
     * Creates a new query that only considers activities that ended between two dates.
     *
     * @param start Don't consider activities that ended before this date.
     * @param end Don't consider activities that ended after this date.
     */
    public QueryBetweenDates(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Copy constructor of this query.
     *
     * @param query Query to be copied.
     */
    public QueryBetweenDates(QueryBetweenDates query) {
        this.start = query.getStart();
        this.start = query.getEnd();
    }

    /**
     * Gets the date when activities start being considered.
     *
     * @return The date when activities start being considered.
     */
    public LocalDateTime getStart() {
        return this.start;
    }

    /**
     * Gets the date when activities stop being considered.
     *
     * @return The date where activities stop being considered.
     */
    public LocalDateTime getEnd() {
        return this.end;
    }

    /**
     * Calculates the hash code of this query.
     *
     * @return The hash code of this query.
     */
    public int hashCode() {
        return Objects.hash(this.start, this.end);
    }

    /**
     * Checks if this query is equal to another object.
     *
     * @param obj Object to be compared with this query.
     * @return Whether <code>this</code> is equal to <code>obj</code>.
     */
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        QueryBetweenDates query = (QueryBetweenDates) obj;
        return this.start.equals(query.start) && this.end.equals(query.end);
    }

    /**
     * Checks if an activity fits the date criteria for this query.
     *
     * @param activity Activity to be checked
     * @return Whether <code>activity</code> fits the date criteria for this query.
     */
    protected boolean activityFits(Activity activity) {
        LocalDateTime end =
                activity.getExecutionDate().plusSeconds(activity.getExecutionTime().toSeconds());
        return end.isAfter(this.start) && end.isBefore(this.end);
    }

    /**
     * Creates a deep copy of this activity.
     *
     * @return A deep copy of this activity.
     */
    public abstract QueryBetweenDates clone();

    /**
     * Creates a debug string representation of this query.
     *
     * @return A debug string representation of this query.
     */
    public abstract String toString();
}
