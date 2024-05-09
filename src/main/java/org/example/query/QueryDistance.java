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
import org.example.activity.ActivityDistance;
import org.example.user.User;

/** A query that calculates the distance a single user ran. */
public class QueryDistance extends QueryBetweenDates {
    /** Class of activities to be considered. */
    private Class<? extends ActivityDistance> activityType;

    /** User considered for the query. */
    private User user;

    /** Distance run by the user that was considered. */
    private double distance;

    /** Creates a new query without date or distance activity restrictions. */
    public QueryDistance() {
        super();
        this.activityType = ActivityDistance.class;
        this.distance = -1.0;
        this.user = null;
    }

    /**
     * Creates a new query without date restrictions but that only considers a subclass of distance
     * activities.
     *
     * @param activityType Type of activity to be considered.
     */
    public QueryDistance(Class<? extends ActivityDistance> activityType) {
        this.activityType = activityType; // clone() is protected. Makes sense
        this.user = null;
        this.distance = -1.0;
    }

    /**
     * Creates a new query that only considers activities of a certain class that ended between two
     * dates.
     *
     * @param activityType Type of activity to be considered.
     * @param start Don't consider activities that ended before this date.
     * @param end Don't consider activities that ended after this date.
     */
    public QueryDistance(
            Class<? extends ActivityDistance> activityType,
            LocalDateTime start,
            LocalDateTime end) {
        super(start, end);
        this.activityType = activityType; // clone() is protected. Makes sense
        this.distance = -1.0;
        this.user = null;
    }

    /**
     * Copy constructor of a query.
     *
     * @param query Query to be copied.
     */
    public QueryDistance(QueryDistance query) {
        super(query);
        this.activityType = query.getActivityType();
        this.user = query.getUser();
        this.distance = query.getDistance();
    }

    /**
     * Gets the type of activity this query is concerned with.
     *
     * @return The type of activity this query is concerned with.
     */
    public Class<? extends ActivityDistance> getActivityType() {
        return this.activityType;
    }

    /**
     * Gets the user accounted for by this query.
     *
     * @return The user accounted for by this query. Can be <code>null</code> if no users were
     *     provided.
     */
    public User getUser() {
        if (this.user != null) return this.user.clone();
        else return null;
    }

    /**
     * Gets the distance ran by the user accounted for by this query.
     *
     * @return The distance ran by the user accounted for by this query. This value is unspecified
     *     when no users were provided.
     */
    public double getDistance() {
        return this.distance;
    }

    /**
     * Calculates the hash code of this query.
     *
     * @return The hash code of this query.
     */
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.user, this.distance);
    }

    /**
     * Called to feed information about a user.
     *
     * @param user User to be consumed.
     */
    public void accept(User user) {
        this.user = user.clone();
        this.distance =
                user.getActivities().getDone().stream()
                        .filter(a -> this.activityFits(a) && this.activityType.isInstance(a))
                        .mapToDouble(a -> ((ActivityDistance) a).getDistanceToTraverse())
                        .sum();
    }

    /**
     * Creates a deep copy of this activity.
     *
     * @return A deep copy of this activity.
     */
    public QueryDistance clone() {
        return new QueryDistance(this);
    }

    /**
     * Creates a debug string representation of this query.
     *
     * @return A debug string representation of this query.
     */
    public String toString() {
        return String.format(
                "QueryDistance(activityType = %s, start = %s, end = %s)",
                this.getActivityType().getSimpleName(),
                this.getStart().toString(),
                this.getEnd().toString());
    }
}
