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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.SortedSet;
import org.junit.jupiter.api.Test;

public class QueryMostActivitiesTest {
    @Test
    public void accept() throws UserException, ActivityException {
        UserActivities activities = new UserActivities();
        SortedSet<Activity> done = activities.getDone();
        User user = new BeginnerUser(1, "Eu", "Quarto", "123@abc.xyz", 100, new UserActivities());
        QueryMostActivities query =
                new QueryMostActivities(
                        LocalDateTime.MIN, LocalDateTime.of(2023, 12, 31, 23, 59, 59));

        // 0
        assertEquals(query.getMaxUser(), null);

        // 1
        query.accept(user);
        assertEquals(query.getMaxUser().getCode(), 1);
        assertEquals(query.getMaxActivities(), 0);

        // 2
        user.setCode(2);
        done.add(
                new ActivityPushUp(
                        Duration.ofMinutes(10), LocalDateTime.of(2023, 1, 1, 0, 0, 0), 100, 50));
        activities.setDone(done);
        user.setActivities(activities);

        query.accept(user);
        assertEquals(query.getMaxUser().getCode(), 2);
        assertEquals(query.getMaxActivities(), 1);

        // 3
        user.setCode(3);
        done.add(
                new ActivityPushUp(
                        Duration.ofMinutes(10), LocalDateTime.of(2023, 1, 2, 0, 0, 0), 100, 50));
        activities.setDone(done);
        user.setActivities(activities);

        query.accept(user);
        assertEquals(query.getMaxUser().getCode(), 3);
        assertEquals(query.getMaxActivities(), 2);

        // 4
        user.setCode(4);
        done.add(
                new ActivityPushUp(
                        Duration.ofMinutes(10), LocalDateTime.of(2024, 1, 2, 0, 0, 0), 100, 50));
        activities.setDone(done);
        user.setActivities(activities);

        query.accept(user);
        assertEquals(query.getMaxUser().getCode(), 3);
        assertEquals(query.getMaxActivities(), 2);

        // 5
        user.setCode(5);
        user.setActivities(new UserActivities());

        query.accept(user);
        assertEquals(query.getMaxUser().getCode(), 3);
        assertEquals(query.getMaxActivities(), 2);
    }

    @Test
    public void testToString() {
        LocalDateTime date = LocalDateTime.of(2030, 12, 25, 00, 00);
        assertEquals(
                (new QueryMostActivities(date, date)).toString(),
                "QueryMostActivities(start = \"2030-12-25T00:00\", end = \"2030-12-25T00:00\")");
    }
}
