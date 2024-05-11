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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.TreeSet;
import org.junit.jupiter.api.Test;

public class QueryHardestTrainingPlanTest {
    @Test
    public void accept() throws UserException, ActivityException, ActivityOverlapException {
        UserActivities activities = new UserActivities();
        TrainingPlan plan = activities.getTrainingPlan();
        plan.setRepetitions(
                new TreeSet<DayOfWeek>(Arrays.asList(new DayOfWeek[] {DayOfWeek.MONDAY})));
        User user = new BeginnerUser(1, "Eu", "Quarto", "123@abc.xyz", 100, new UserActivities());
        QueryHardestTrainingPlan query = new QueryHardestTrainingPlan();

        // 0
        assertEquals(query.getMaxUser(), null);

        // 1
        query.accept(user);
        assertEquals(query.getMaxUser().getCode(), 1);

        // 2
        user.setCode(2);
        plan.addActivity(
                new ActivityPushUp(
                        Duration.ofMinutes(10), LocalDateTime.of(2023, 1, 1, 0, 0, 0), 100, 50),
                1);
        activities.setTrainingPlan(plan);
        user.setActivities(activities);

        query.accept(user);
        assertEquals(query.getMaxUser().getCode(), 2);

        // 3
        user.setCode(3);
        plan.addActivity(
                new ActivityPushUp(
                        Duration.ofMinutes(10), LocalDateTime.of(2023, 1, 2, 0, 10, 0), 100, 50),
                1);
        activities.setTrainingPlan(plan);
        user.setActivities(activities);

        query.accept(user);
        assertEquals(query.getMaxUser().getCode(), 3);

        // 4
        user.setCode(4);
        user.setActivities(new UserActivities());

        query.accept(user);
        assertEquals(query.getMaxUser().getCode(), 3);
    }
}
