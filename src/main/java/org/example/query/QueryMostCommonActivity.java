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

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/** A query that determines the activity that was executed the most times. */
public class QueryMostCommonActivity implements Consumer<User> {
    /** Relation between activity class names and their number of occurrences. */
    private Map<String, Integer> activities;

    /** Creates a new query. */
    public QueryMostCommonActivity() {
        this.activities = new HashMap<String, Integer>();
    }

    /**
     * Copy constructor of a query.
     *
     * @param query Query to be copied.
     */
    public QueryMostCommonActivity(QueryMostCommonActivity query) {
        this.activities = query.getActivities();
    }

    /**
     * Gets all the activities registered by this query.
     *
     * @return All the activities registered by this query.
     */
    private Map<String, Integer> getActivities() {
        return new HashMap<String, Integer>(this.activities);
    }

    /**
     * Gets the activity that was executed the most times.
     *
     * @return The activity that was executed the most times, along with the number of executions.
     *     May be <code>null</code> if no activities have been provided.
     */
    public Map.Entry<String, Integer> getTopActivity() {
        if (this.activities.isEmpty())
            return null;
        return new AbstractMap.SimpleEntry<String, Integer>(
            Collections.max(this.activities.entrySet(), Map.Entry.comparingByValue()));
    }

    /**
     * Consumes a user to get the information about its activities.
     *
     * @param user User to be consumed.
     */
    public void accept(User user) {
        int           activityCount = 0;
        Set<Activity> activities    = user.getActivities().getDone();
        for (Activity activity : activities) {
            String name = activity.getClass().getSimpleName();
            this.activities.put(name, this.activities.getOrDefault(name, 0) + 1);
        }
    }

    /**
     * Calculates the hash code of this query.
     *
     * @return The hash code of this query.
     */
    @Override
    public int hashCode() {
        return this.activities.hashCode();
    }

    /**
     * Creates a deep copy of this activity.
     *
     * @return A deep copy of this activity.
     */
    @Override
    public QueryMostCommonActivity clone() {
        return new QueryMostCommonActivity(this);
    }

    /**
     * Creates a debug string representation of this query.
     *
     * @return A debug string representation of this query.
     */
    @Override
    public String toString() {
        return "QueryMostCommonActivity()";
    }
}
