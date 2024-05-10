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

public class QueryDistanceTest {
    @Test
    public void accept() throws UserException, ActivityException {
        UserActivities activities = new UserActivities();
        SortedSet<Activity> done = activities.getDone();
        User user = new BeginnerUser(1, "Eu", "Quarto", "123@abc.xyz", 100, new UserActivities());
        QueryDistance query =
                new QueryDistance(
                        ActivityDistance.class,
                        LocalDateTime.MIN,
                        LocalDateTime.of(2023, 12, 31, 23, 59, 59));

        // 0
        assertEquals(query.getDistance(), -1.0);

        // 1
        query.accept(user);
        assertEquals(query.getDistance(), 0.0);

        // 2
        done.add(
                new ActivityMountainRun(
                        Duration.ofMinutes(50), LocalDateTime.of(2023, 1, 1, 0, 0, 0), 100, 10, 1));
        activities.setDone(done);
        user.setActivities(activities);

        query.accept(user);
        assertEquals(query.getDistance(), 10.0);

        // 3
        done.add(
                new ActivityTrackRun(
                        Duration.ofMinutes(50), LocalDateTime.of(2023, 1, 2, 0, 0, 0), 100, 11));
        activities.setDone(done);
        user.setActivities(activities);

        query.accept(user);
        assertEquals(query.getDistance(), 21.0);

        // 4
        done.add(
                new ActivityPushUp(
                        Duration.ofMinutes(10), LocalDateTime.of(2024, 1, 2, 0, 0, 0), 100, 50));
        activities.setDone(done);
        user.setActivities(activities);

        query.accept(user);
        assertEquals(query.getDistance(), 21.0);

        // 5
        done.add(
                new ActivityTrackRun(
                        Duration.ofMinutes(50), LocalDateTime.of(2024, 1, 2, 0, 0, 0), 100, 11));
        activities.setDone(done);
        user.setActivities(activities);

        query.accept(user);
        assertEquals(query.getDistance(), 21.0);

        // Activity restriction
        QueryDistance queryAltimetry =
                new QueryDistance(
                        ActivityAltimetryDistance.class,
                        LocalDateTime.MIN,
                        LocalDateTime.of(2023, 12, 31, 23, 59, 59));
        queryAltimetry.accept(user);
        assertEquals(queryAltimetry.getDistance(), 10.0);

        // 6
        user.setActivities(new UserActivities());
        query.accept(user);
        assertEquals(query.getDistance(), 0.0);
    }

    @Test
    public void testToString() {
        LocalDateTime date = LocalDateTime.of(2030, 12, 25, 00, 00);
        assertEquals(
                (new QueryDistance(ActivityDistance.class, date, date)).toString(),
                "QueryDistance(activityType = ActivityDistance, start = \"2030-12-25T00:00\", end = \"2030-12-25T00:00\")");
    }
}
