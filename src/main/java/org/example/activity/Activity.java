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

package org.example.activity;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import org.example.user.User;

public abstract class Activity implements Serializable {
    private Duration executionTime;
    private LocalDateTime executionDate;
    private int bpm;

    public Activity() {
        this.executionTime = Duration.ZERO;
        this.executionDate = LocalDateTime.MIN;
        this.bpm = 0;
    }

    public Activity(Duration executionTime, LocalDateTime exexutionDate, int bpm) {
        this.setExecutionTime(executionTime);
        this.executionDate = exexutionDate;
        this.setBPM(bpm);
    }

    public Activity(Activity activity) {
        this.executionTime = activity.getExecutionTime();
        this.executionDate = activity.getExecutionDate();
        this.bpm = activity.getBPM();
    }

    public Duration getExecutionTime() {
        return this.executionTime;
    }

    public LocalDateTime getExecutionDate() {
        return this.executionDate;
    }

    public int getBPM() {
        return this.bpm;
    }

    public void setExecutionTime(Duration executionTime) {
        if (executionTime.toSeconds() == 0)
            throw new IllegalArgumentException("An exercise should last at least one second long!");
        this.executionTime = executionTime;
    }

    public void setExecutionDate(LocalDateTime executionDate) {
        this.executionDate = executionDate;
    }

    public void setBPM(int bpm) throws IllegalArgumentException {
        if (bpm <= 0)
            throw new IllegalArgumentException(
                    "The average BPM during exercise must be a positive number!");
        this.bpm = bpm;
    }

    public abstract double countCalories(User user);

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || (this.getClass() != object.getClass())) return false;

        Activity activity = (Activity) object;
        return (this.executionTime.equals(activity.getExecutionTime())
                && this.executionDate.equals(activity.getExecutionDate())
                && this.bpm == activity.getBPM());
    }

    public abstract Activity clone();

    public abstract String toString();
}
