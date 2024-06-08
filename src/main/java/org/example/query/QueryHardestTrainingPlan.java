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

/** A query that determines the user that has the training plan that burns the most calories. */
public class QueryHardestTrainingPlan implements Consumer<User> {
    /** User whose training plan burns the most calories. */
    private User maxUser;

    /** Calories burned by the hardest training plan. */
    private double maxCalories;

    /** Creates a new query. */
    public QueryHardestTrainingPlan() {
        this.maxUser     = null;
        this.maxCalories = -1;
    }

    /**
     * Copy constructor of a query.
     *
     * @param query Query to be copied.
     */
    public QueryHardestTrainingPlan(QueryHardestTrainingPlan query) {
        this.maxUser     = query.getMaxUser();
        this.maxCalories = query.getMaxCalories();
    }

    /**
     * Gets the user with the training plan that burns the most calories.
     *
     * @return The user with the most calories burned. Can be <code>null</code> if no users were
     *     provided.
     */
    public User getMaxUser() {
        if (this.maxUser != null)
            return this.maxUser.clone();
        else
            return null;
    }

    /**
     * Gets the calories burned by the hardest training plan.
     *
     * @return The calories burned by the hardest training plan. This value is unspecified if no
     *     users were provided.
     */
    public double getMaxCalories() {
        return this.maxCalories;
    }

    /**
     * Consumes a user to gets the information about its training plan.
     *
     * @param user User to be consumed.
     */
    public void accept(User user) {
        double calories = user.getActivities().getTrainingPlan().countCalories(user);
        if (calories > this.maxCalories) {
            this.maxCalories = calories;
            this.maxUser     = user.clone();
        }
    }

    /**
     * Calculates the hash code of this query.
     *
     * @return The hash code of this query.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.maxUser, this.maxCalories);
    }

    /**
     * Checks if this query is equal to another object.
     *
     * @param obj Object to be compared with this query.
     * @return Whether <code>this</code> is equal to <code>obj</code>.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;

        QueryHardestTrainingPlan query = (QueryHardestTrainingPlan) obj;
        return this.maxUser.equals(query.getMaxUser()) &&
            this.maxCalories == query.getMaxCalories();
    }

    /**
     * Creates a deep copy of this query.
     *
     * @return A deep copy of this query.
     */
    @Override
    public QueryHardestTrainingPlan clone() {
        return new QueryHardestTrainingPlan(this);
    }

    /**
     * Creates a debug string representation of this query.
     *
     * @return A debug string representation of this query.
     */
    @Override
    public String toString() {
        return "QueryHardestTrainingPlan()";
    }
}
