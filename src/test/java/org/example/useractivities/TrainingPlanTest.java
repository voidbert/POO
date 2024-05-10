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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import org.junit.jupiter.api.Test;

public class TrainingPlanTest {
    private TrainingPlan plan;
    private SortedMap<Activity, Integer> activities;
    private Set<DayOfWeek> repetitions;

    public TrainingPlanTest() throws ActivityException, ActivityOverlapException {
        this.activities = new TreeMap<Activity, Integer>();
        this.activities.put(
                new ActivityMountainRun(
                        Duration.ofMinutes(50),
                        LocalDateTime.of(2024, 5, 6, 10, 0, 0),
                        90,
                        8.0,
                        0.3),
                1);
        this.activities.put(
                new ActivityPushUp(
                        Duration.ofMinutes(10), LocalDateTime.of(2024, 5, 6, 11, 0, 0), 100, 20),
                3);

        DayOfWeek[] days = {DayOfWeek.MONDAY, DayOfWeek.FRIDAY};
        this.repetitions = new HashSet<DayOfWeek>(Arrays.asList(days));

        this.plan = new TrainingPlan(activities, this.repetitions);
    }

    @Test
    public void encapsulation() throws ActivityException {
        this.repetitions.add(DayOfWeek.WEDNESDAY);
        assertEquals(this.plan.getRepetitions().size(), 2);

        Activity firstActivities = this.activities.keySet().iterator().next();
        firstActivities.setExecutionTime(Duration.ofMinutes(10));
        Activity firstPlan = this.plan.getActivities().keySet().iterator().next();
        assertEquals(firstPlan.getExecutionTime(), Duration.ofMinutes(50));
    }

    @Test
    void overlaps() throws ActivityException {
        assertFalse(
                this.plan.overlaps(
                        new ActivityPushUp(
                                Duration.ofMinutes(10),
                                LocalDateTime.of(2024, 5, 6, 10, 50, 0),
                                100,
                                20)));
        assertTrue(
                this.plan.overlaps(
                        new ActivityPushUp(
                                Duration.ofMinutes(10),
                                LocalDateTime.of(2024, 5, 6, 10, 51, 0),
                                100,
                                20)));
    }

    @Test
    public void addActivity() throws ActivityException {
        assertDoesNotThrow(
                () -> {
                    this.plan.addActivity(
                            new ActivityPushUp(
                                    Duration.ofMinutes(10),
                                    LocalDateTime.of(2024, 5, 6, 11, 30, 0),
                                    100,
                                    20),
                            3);
                });
        assertThrows(
                ActivityOverlapException.class,
                () -> {
                    this.plan.addActivity(
                            new ActivityPushUp(
                                    Duration.ofMinutes(10),
                                    LocalDateTime.of(2024, 5, 6, 11, 30, 0),
                                    100,
                                    20),
                            -20);
                });

        assertThrows(
                ActivityOverlapException.class,
                () -> {
                    this.plan.addActivity(
                            new ActivityPushUp(
                                    Duration.ofMinutes(10),
                                    LocalDateTime.of(2024, 5, 6, 11, 59, 0),
                                    100,
                                    20),
                            3);
                });
    }

    @Test
    public void removeActivity() throws ActivityDoesntExistException {
        TrainingPlan copy = this.plan.clone();

        assertThrows(
                ActivityDoesntExistException.class,
                () -> {
                    this.plan.removeActivity(-1);
                });

        assertThrows(
                ActivityDoesntExistException.class,
                () -> {
                    this.plan.removeActivity(2);
                });

        this.plan.removeActivity(0);
        assertTrue(this.plan.getActivities().keySet().iterator().next() instanceof ActivityPushUp);

        this.plan.removeActivity(0);
        assertTrue(this.plan.getActivities().isEmpty());

        copy.removeActivity(1);
        assertTrue(copy.getActivities().keySet().iterator().next() instanceof ActivityMountainRun);
    }

    @Test
    public void setActivities() throws ActivityException {
        this.activities.put(
                new ActivityPushUp(
                        Duration.ofMinutes(10), LocalDateTime.of(2024, 5, 6, 11, 30, 0), 100, 20),
                3);
        assertDoesNotThrow(
                () -> {
                    this.plan.setActivities(this.activities);
                });

        this.activities.put(
                new ActivityPushUp(
                        Duration.ofMinutes(10), LocalDateTime.of(2024, 5, 6, 11, 59, 0), 100, 20),
                3);
        assertThrows(
                ActivityOverlapException.class,
                () -> {
                    this.plan.setActivities(this.activities);
                });
    }

    @Test
    public void activitiesBetween() {
        LocalDateTime start = LocalDateTime.of(2024, 5, 7, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 5, 8, 0, 0, 0);
        assertTrue(this.plan.activitiesBetween(start, end).isEmpty());

        LocalDateTime start2 = LocalDateTime.of(2024, 5, 6, 0, 0, 0);
        Activity[] activities = this.plan.activitiesBetween(start2, end).toArray(Activity[]::new);
        assertEquals(activities.length, 4);
        assertEquals(activities[3].getExecutionDate(), LocalDateTime.of(2024, 5, 6, 11, 20, 0));

        LocalDateTime end2 = LocalDateTime.of(2024, 5, 6, 11, 20, 0);
        assertEquals(this.plan.activitiesBetween(start2, end2).size(), 3);
    }

    @Test
    public void countCalories() {
        assertEquals((new TrainingPlan()).countCalories(new BeginnerUser()), 0);
        assertEquals(this.plan.countCalories(new BeginnerUser()), 1486.8, 0.1);
    }

    @Test
    public void testEquals() throws ActivityException, ActivityOverlapException {
        this.activities.put(
                new ActivityMountainRun(
                        Duration.ofMinutes(50),
                        LocalDateTime.of(2024, 5, 6, 8, 0, 0),
                        90,
                        8.0,
                        0.3),
                1);

        TrainingPlan copy = this.plan.clone();
        copy.setActivities(this.activities);
        assertNotEquals(this.plan, copy);
    }

    @Test
    public void testClone() {
        final TrainingPlan copy = plan.clone();
        assertEquals(this.plan.getActivities(), copy.getActivities());
        assertEquals(this.plan.getRepetitions(), copy.getRepetitions());
    }

    @Test
    public void testToString() {
        assertEquals(
                (new TrainingPlan()).toString(), "TrainingPlan(activities = {}, repetitions = [])");
    }

    @Test
    public void serizalize() {
        TestUtils.serialize(this.plan);
    }
}
