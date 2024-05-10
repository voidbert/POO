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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.junit.jupiter.api.Test;

public class UserActivitiesTest {
    private final UserActivities activities;

    public UserActivitiesTest() throws ActivityException, ActivityOverlapException {
        SortedSet<Activity> todo = new TreeSet<Activity>();
        todo.add(
                new ActivityPushUp(
                        Duration.ofMinutes(10), LocalDateTime.of(2024, 5, 6, 11, 0, 0), 100, 20));
        todo.add(
                new ActivityPushUp(
                        Duration.ofMinutes(10), LocalDateTime.of(2024, 5, 6, 11, 20, 0), 100, 20));

        TreeSet<DayOfWeek> planDays = new TreeSet<DayOfWeek>();
        planDays.add(DayOfWeek.FRIDAY);

        SortedMap<Activity, Integer> planActivities = new TreeMap<Activity, Integer>();
        planActivities.put(
                new ActivityMountainRun(
                        Duration.ofMinutes(50), LocalDateTime.of(1, 1, 1, 8, 0, 0), 90, 8.0, 0.3),
                1);
        TrainingPlan plan = new TrainingPlan(planActivities, planDays);

        this.activities = new UserActivities(todo, new TreeSet<Activity>(), plan);
    }

    @Test
    public void addActivity() {
        assertThrows(
                ActivityOverlapException.class,
                () -> {
                    this.activities.addActivity(
                            new ActivityPushUp(
                                    Duration.ofMinutes(10),
                                    LocalDateTime.of(2024, 5, 3, 8, 10, 0),
                                    100,
                                    20));
                });
        assertDoesNotThrow(
                () -> {
                    this.activities.addActivity(
                            new ActivityPushUp(
                                    Duration.ofMinutes(10),
                                    LocalDateTime.of(2024, 5, 4, 8, 10, 0),
                                    100,
                                    20));
                });
    }

    @Test
    public void setTodo() throws ActivityException {
        SortedSet<Activity> todo = this.activities.getTodo();
        assertDoesNotThrow(
                () -> {
                    this.activities.setTodo(todo);
                });

        todo.add(
                new ActivityPushUp(
                        Duration.ofMinutes(10), LocalDateTime.of(2024, 5, 6, 10, 59, 0), 100, 20));
        assertThrows(
                ActivityOverlapException.class,
                () -> {
                    this.activities.setTodo(todo);
                });
    }

    @Test
    public void setTrainingPlan() throws ActivityException, ActivityOverlapException {
        this.activities.addActivity(
                new ActivityPushUp(
                        Duration.ofMinutes(10), LocalDateTime.of(2024, 5, 9, 8, 10, 0), 100, 20));

        TrainingPlan plan = this.activities.getTrainingPlan();
        assertDoesNotThrow(
                () -> {
                    this.activities.setTrainingPlan(plan);
                });

        Set<DayOfWeek> days = plan.getRepetitions();
        days.add(DayOfWeek.THURSDAY);
        plan.setRepetitions(days);
        assertThrows(
                ActivityOverlapException.class,
                () -> {
                    this.activities.setTrainingPlan(plan);
                });
    }

    @Test
    public void removeActivity() throws ActivityDoesntExistException {
        this.activities.removeActivity(1);
        assertEquals(
                this.activities.getTodo().iterator().next().getExecutionDate(),
                LocalDateTime.of(2024, 5, 6, 11, 0, 0));
        this.activities.removeActivity(0);
        assertTrue(this.activities.getTodo().isEmpty());
        assertThrows(
                ActivityDoesntExistException.class,
                () -> {
                    this.activities.removeActivity(0);
                });
    }

    @Test
    public void leapForward() {
        LocalDateTime d1 = LocalDateTime.of(2024, 5, 5, 0, 0, 0);
        LocalDateTime d2 = LocalDateTime.of(2024, 5, 6, 0, 0, 0);
        LocalDateTime d3 = LocalDateTime.of(2024, 5, 6, 11, 10, 0);
        LocalDateTime d4 = LocalDateTime.of(2024, 5, 6, 11, 19, 0);
        LocalDateTime d5 = LocalDateTime.of(2024, 5, 6, 11, 30, 0);
        LocalDateTime d6 = LocalDateTime.of(2024, 5, 10, 0, 0, 0);
        LocalDateTime d7 = LocalDateTime.of(2024, 5, 10, 8, 50, 0);

        this.activities.leapForward(d1, d2);
        assertEquals(this.activities.getDone().size(), 0);

        this.activities.leapForward(d2, d3);
        assertEquals(this.activities.getDone().size(), 1);

        this.activities.leapForward(d3, d4);
        assertEquals(this.activities.getDone().size(), 1);

        this.activities.leapForward(d4, d5);
        assertEquals(this.activities.getDone().size(), 2);

        this.activities.leapForward(d5, d6);
        assertEquals(this.activities.getDone().size(), 2);

        this.activities.leapForward(d6, d7);
        assertEquals(this.activities.getDone().size(), 3);
    }

    @Test
    public void testEquals() throws ActivityException, ActivityOverlapException {
        UserActivities copy = this.activities.clone();
        assertEquals(this.activities, copy);
        copy.addActivity(
                new ActivityMountainRun(
                        Duration.ofMinutes(50),
                        LocalDateTime.of(2024, 5, 7, 8, 0, 0),
                        90,
                        8.0,
                        0.3));
        assertNotEquals(this.activities, copy);
    }

    @Test
    public void testClone() {
        final UserActivities copy = this.activities.clone();
        assertEquals(this.activities.getTodo(), copy.getTodo());
        assertEquals(this.activities.getDone(), copy.getDone());
        assertEquals(this.activities.getTrainingPlan(), copy.getTrainingPlan());
    }

    @Test
    public void testToString() {
        assertEquals(
                (new UserActivities()).toString(),
                String.format(
                        "UserActivities(todo = [], done = [], plan = %s)",
                        (new TrainingPlan()).toString()));
    }

    @Test
    public void serizalize() {
        TestUtils.serialize(this.activities);
    }
}
