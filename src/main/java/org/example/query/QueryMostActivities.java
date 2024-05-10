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

import java.time.LocalDateTime;
import java.util.Objects;

/** A query that determines the user that has executed the most activities. */
public class QueryMostActivities extends QueryBetweenDates {
    /** User with the most activities executed. */
    private User maxUser;

    /** Number of activities executed by the user with the most activities. */
    private long maxActivities;

    /** Creates a new query without date restrictions. */
    public QueryMostActivities() {
        super();
        this.maxUser = null;
        this.maxActivities = -1;
    }

    /**
     * Creates a new query that only considers activities that ended between two dates.
     *
     * @param start Don't consider activities that ended before this date.
     * @param end Don't consider activities that ended after this date.
     */
    public QueryMostActivities(LocalDateTime start, LocalDateTime end) {
        super(start, end);
        this.maxUser = null;
        this.maxActivities = -1;
    }

    /**
     * Copy constructor of a query.
     *
     * @param query Query to be copied.
     */
    public QueryMostActivities(QueryMostActivities query) {
        super(query);
        this.maxUser = query.getMaxUser();
        this.maxActivities = query.getMaxActivities();
    }

    /**
     * Gets the user with the most activities.
     *
     * @return The user with the most activities. Can be <code>null</code> if no users were
     *     provided.
     */
    public User getMaxUser() {
        if (this.maxUser != null) return this.maxUser.clone();
        else return null;
    }

    /**
     * Gets the number of activities of the user with the most activities.
     *
     * @return The number of activities of the user with the most activities. This value is
     *     unspecified when no users were provided.
     */
    public long getMaxActivities() {
        return this.maxActivities;
    }

    /**
     * Consumes a user to get the information about its activities.
     *
     * @param user User to be consumed.
     */
    public void accept(User user) {
        long activityCount =
                user.getActivities().getDone().stream().filter(a -> this.activityFits(a)).count();

        if (activityCount > this.maxActivities) {
            this.maxActivities = activityCount;
            this.maxUser = user.clone();
        }
    }

    /**
     * Calculates the hash code of this query.
     *
     * @return The hash code of this query.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.maxUser, this.maxActivities);
    }

    /**
     * Checks if this query is equal to another object.
     *
     * @param obj Object to be compared with this query.
     * @return Whether <code>this</code> is equal to <code>obj</code>.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        QueryMostActivities query = (QueryMostActivities) obj;
        return super.equals(query)
                && this.maxUser.equals(query.getMaxUser())
                && this.maxActivities == query.getMaxActivities();
    }

    /**
     * Creates a deep copy of this query.
     *
     * @return A deep copy of this query.
     */
    @Override
    public QueryMostActivities clone() {
        return new QueryMostActivities(this);
    }

    /**
     * Creates a debug string representation of this query.
     *
     * @return A debug string representation of this query.
     */
    @Override
    public String toString() {
        return String.format(
                "QueryMostActivities(start = \"%s\", end = \"%s\")",
                this.getStart().toString(), this.getEnd().toString());
    }
}
