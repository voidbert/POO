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

package org.example.useractivities;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.example.activity.Activity;
import org.example.activity.ActivityMountainRun;
import org.example.activity.ActivityPushUp;
import org.junit.jupiter.api.Test;

public class UserActivitiesTest {
    private final UserActivities activities;

    public UserActivitiesTest() {
        SortedSet<Activity> todo = new TreeSet<Activity>();
        todo.add(
                new ActivityPushUp(
                        Duration.ofMinutes(10), LocalDateTime.of(2024, 5, 6, 11, 0, 0), 100, 20));
        todo.add(
                new ActivityPushUp(
                        Duration.ofMinutes(10), LocalDateTime.of(2024, 5, 6, 11, 20, 0), 100, 20));

        DayOfWeek[] planDays = {DayOfWeek.FRIDAY};
        SortedMap<Activity, Integer> planActivities = new TreeMap<Activity, Integer>();
        planActivities.put(
                new ActivityMountainRun(
                        Duration.ofMinutes(50), LocalDateTime.of(1, 1, 1, 8, 0, 0), 90, 8.0, 0.3),
                1);
        TrainingPlan plan =
                new TrainingPlan(
                        planActivities,
                        Arrays.stream(planDays).collect(Collectors.toCollection(TreeSet::new)));

        this.activities = new UserActivities(todo, new TreeSet<Activity>(), plan);
    }

    @Test
    public void setTodo() {
        SortedSet<Activity> todo = this.activities.getTodo();
        assertDoesNotThrow(
                () -> {
                    this.activities.setTodo(todo);
                });

        todo.add(
                new ActivityPushUp(
                        Duration.ofMinutes(10), LocalDateTime.of(2024, 5, 6, 10, 59, 0), 100, 20));
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    this.activities.setTodo(todo);
                });
    }

    @Test
    public void removeActivity() {
        this.activities.removeActivity(1);
        assertEquals(
                this.activities.getTodo().iterator().next().getExecutionDate(),
                LocalDateTime.of(2024, 5, 6, 11, 0, 0));
        this.activities.removeActivity(0);
        assertTrue(this.activities.getTodo().isEmpty());
        assertThrows(
                IndexOutOfBoundsException.class,
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
        assertEquals(this.activities.getDone().stream().count(), 0);

        this.activities.leapForward(d2, d3);
        assertEquals(this.activities.getDone().stream().count(), 1);

        this.activities.leapForward(d3, d4);
        assertEquals(this.activities.getDone().stream().count(), 1);

        this.activities.leapForward(d4, d5);
        assertEquals(this.activities.getDone().stream().count(), 2);

        this.activities.leapForward(d5, d6);
        assertEquals(this.activities.getDone().stream().count(), 2);

        this.activities.leapForward(d6, d7);
        assertEquals(this.activities.getDone().stream().count(), 3);
    }

    @Test
    public void testEquals() {
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
    public void addActivity() {
        assertThrows(
                RuntimeException.class,
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
    public void setTrainingPlan() {
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
                RuntimeException.class,
                () -> {
                    this.activities.setTrainingPlan(plan);
                });
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
        try {
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
            objectOutputStream.writeObject(this.activities);

            byte bytes[] = byteOutputStream.toByteArray();
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);

            UserActivities read = (UserActivities) objectInputStream.readObject();
            assertEquals(read, this.activities);
        } catch (IOException e) {
            assertTrue(false);
        } catch (ClassNotFoundException e) {
            assertTrue(false);
        }
    }
}
