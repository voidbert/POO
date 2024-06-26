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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueryMostCommonActivityTest {
    @Test
    public void accept() throws UserException, ActivityException {
        UserActivities      activities = new UserActivities();
        SortedSet<Activity> done       = new TreeSet<Activity>();
        User user = new BeginnerUser(1, "Eu", "Quarto", "123@abc.xyz", 100, new UserActivities());
        QueryMostCommonActivity query = new QueryMostCommonActivity();

        // 0
        assertEquals(query.getTopActivity(), null);

        // 1
        query.accept(user);
        assertEquals(query.getTopActivity(), null);

        // 2
        done = new TreeSet<Activity>();
        done.add(new ActivityPushUp(Duration.ofMinutes(10), LocalDateTime.now(), 100, 50));
        activities.setDone(done);
        user.setActivities(activities);

        query.accept(user);
        assertEquals(query.getTopActivity().getKey(), "ActivityPushUp");

        // 3
        done = new TreeSet<Activity>();
        done.add(new ActivityTrackRun(Duration.ofMinutes(50), LocalDateTime.now(), 100, 10));
        activities.setDone(done);
        user.setActivities(activities);

        query.accept(user);
        assertEquals(query.getTopActivity().getKey(), "ActivityPushUp");

        // 4
        done = new TreeSet<Activity>();
        done.add(new ActivityTrackRun(Duration.ofMinutes(50), LocalDateTime.now(), 100, 10));
        activities.setDone(done);
        user.setActivities(activities);

        query.accept(user);
        assertEquals(query.getTopActivity().getKey(), "ActivityTrackRun");

        // 5
        done = new TreeSet<Activity>();
        done.add(new ActivityPushUp(Duration.ofMinutes(10), LocalDateTime.now(), 100, 50));
        activities.setDone(done);
        user.setActivities(activities);

        query.accept(user);
        assertEquals(query.getTopActivity().getValue(), Integer.valueOf(2));

        // 6
        done = new TreeSet<Activity>();
        done.add(new ActivityPushUp(Duration.ofMinutes(10), LocalDateTime.now(), 100, 50));
        activities.setDone(done);
        user.setActivities(activities);

        query.accept(user);
        assertEquals(query.getTopActivity().getKey(), "ActivityPushUp");
    }
}
