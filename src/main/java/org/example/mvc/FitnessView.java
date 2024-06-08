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

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Consumer;

/** The user interaction layer of the fitness application. */
public class FitnessView {
    /** The controller that this view interacts with. */
    private FitnessController controller;

    /** Creates a new fitness view. */
    public FitnessView() {
        this.controller = new FitnessController();
    }

    /**
     * Creates a fitness view from the value of its fields.
     *
     * @param controller Fitness controller.
     */
    public FitnessView(FitnessController controller) {
        this.controller = controller;
    }

    /**
     * Copy constructor of a fitness view.
     *
     * @param view Fitness view to be copied.
     */
    public FitnessView(FitnessView view) {
        this.controller = view.getController();
    }

    /**
     * Gets the controller that this view interacts with.
     *
     * @return The controller that this view interacts with.
     */
    private FitnessController getController() {
        return this.controller;
    }

    /** Asks the user to input a new user. */
    private void addUser() {
        String[]          classes = this.controller.getUserClasses().toArray(String[] ::new);
        Consumer<Integer> handler = i -> {
            UserInput input   = new UserInput();
            String    name    = input.readString("Name > ");
            String    address = input.readString("Address > ");
            String    email   = input.readString("Email > ");
            int       bpm     = input.readInt("BPM > ", "Must be a positive integer!", b -> b > 0);

            try {
                long code = this.controller.addUser(classes[i], name, address, email, bpm);
                System.out.printf("User %d successfully added!\n", code);
            } catch (FitnessControllerException e) {
                System.err.println(e.getMessage());
            }
        };

        new Menu(
            Arrays.stream(classes).map(n -> new MenuEntry(n, handler)).toArray(MenuEntry[] ::new))
            .run();
    }

    /**
     * Asks the user to input a new activity.
     *
     * @param userCode User to add the inputted activity to.
     * @param toTrainingPlan Whether the activity should be added to the user's training plan.
     */
    private void addActivity(long userCode, boolean toTrainingPlan) {
        String[]          classes = this.controller.getActivityClasses().toArray(String[] ::new);
        Consumer<Integer> handler = i -> {
            UserInput input = new UserInput();
            int       minutes =
                input.readInt("Duration (min) > ", "Must be a positive integer!", m -> m > 0);
            LocalDateTime date =
                input.readDate(!toTrainingPlan,
                               "Invalid date of before curent one!",
                               d -> toTrainingPlan || !d.isBefore(this.controller.getNow()));

            SortedMap<FitnessController.ActivityExtraField, Object> values =
                new TreeMap<FitnessController.ActivityExtraField, Object>();
            SortedSet<FitnessController.ActivityExtraField> fields =
                this.controller.getActivityExtraFields(classes[i]);

            if (fields.contains(FitnessController.ActivityExtraField.REPETITIONS)) {
                values.put(
                    FitnessController.ActivityExtraField.REPETITIONS,
                    input.readInt("Repetitions > ", "Must be a positive integer!", r -> r > 0));
            }
            if (fields.contains(FitnessController.ActivityExtraField.WEIGHT)) {
                values.put(FitnessController.ActivityExtraField.WEIGHT,
                           input.readDouble("Weight (kg) > ",
                                            "Must be a positive decimal!",
                                            w -> w > 0.0));
            }
            if (fields.contains(FitnessController.ActivityExtraField.DISTANCE)) {
                values.put(FitnessController.ActivityExtraField.DISTANCE,
                           input.readDouble("Distance (km) > ",
                                            "Must be a positive decimal!",
                                            d -> d > 0.0));
            }
            if (fields.contains(FitnessController.ActivityExtraField.ALTIMETRY)) {
                values.put(FitnessController.ActivityExtraField.ALTIMETRY,
                           input.readDouble("Altimetry > ",
                                            "Must be a decimal in [0.0; 1.0]!",
                                            a -> a >= 0.0 && a <= 1.0));
            }

            try {
                if (toTrainingPlan) {
                    int repetitions = input.readInt("Plan repetitions > ",
                                                    "Must be a positive integer!",
                                                    r -> r > 0);
                    this.controller.addActivityToTrainingPlan(userCode,
                                                              classes[i],
                                                              minutes,
                                                              date,
                                                              values,
                                                              repetitions);
                } else {
                    this.controller.addActivity(userCode, classes[i], minutes, date, values);
                }
                System.out.println("Activity added with success!");
            } catch (FitnessControllerException e) {
                System.err.println(e.getMessage());
            }
        };

        MenuEntry[] entries = new MenuEntry[classes.length];
        for (int i = 0; i < classes.length; ++i) {
            String hard = this.controller.activityIsHard(classes[i]) ? " (HARD)" : "";
            String name = String.format("%s%s", classes[i], hard);
            entries[i]  = new MenuEntry(name, handler);
        }
        new Menu(entries).run();
    }

    /**
     * Shows the days a training plan is executed.
     *
     * @param userCode Identifier code of the user that owns the plan.
     */
    private void showPlanDays(long userCode) {
        try {
            SortedSet<DayOfWeek> days = this.controller.getTrainingPlanDays(userCode);
            System.out.printf("Executed on: %s\n", days.toString());
        } catch (FitnessControllerException e) {
            System.err.println(e.getMessage());
        }
    }

    /** Asks the user which entity they'd like to add. */
    private void addEntity() {
        MenuEntry[] entries = {
            new MenuEntry("Add new user", i -> this.addUser()),
            new MenuEntry("Add new activity",
                          i -> {
                              if (this.controller.isEmpty()) {
                                  System.err.println("No users yet!");
                                  return;
                              }

                              long code = (new UserInput())
                                              .readInt("User code > ",
                                                       "User doesn't exist!",
                                                       j -> this.controller.userExists(j));

                              MenuEntry[] activityEntries = {
                                  new MenuEntry("Single activity", j -> addActivity(code, false)),
                                  new MenuEntry("Add to training plan",
                                                j -> addActivity(code, true)),
                                  new MenuEntry("Go back", j -> {}),
                              };
                              new Menu(activityEntries).run();
                          }),
            new MenuEntry("Go back", i -> {}),
        };
        new Menu(entries).run();
    }

    /** Asks the user which entities they wish to list. */
    private void listEntities() {
        Consumer<Integer> doOperation = i -> {
            String[]       header;
            String         format;
            List<String[]> entities = null;
            if (i == 0) {
                header   = new String[] { "Code", "Name", "Class", "Address", "Email", "BPM" };
                format   = "%5s %-25s %-25s %-25s %-25s %5s\n";
                entities = this.controller.getUsers();
            } else {
                long code = (new UserInput())
                                .readInt("User code > ",
                                         "User doesn't exist!",
                                         j -> this.controller.userExists(j));

                header = new String[] { "Date",        "Duration", "Calories", "Class",    "BPM",
                                        "Repetitions", "Weight",   "Distance", "Altimetry" };
                format = "%-20s %8s %8s %-25s %5s %11s %6s %8s %9s\n";

                try {
                    if (i == 1)
                        entities = this.controller.getTodoActivities(code);
                    else if (i == 2)
                        entities = this.controller.getDoneActivities(code);
                    else if (i == 3) {
                        showPlanDays(code);
                        format = "%-10s %8s %8s %9s %-25s %5s %11s %6s %8s %9s\n";

                        List<String> headerMod = new ArrayList<String>(Arrays.asList(header));
                        headerMod.add(3, "Plan reps");
                        header = headerMod.toArray(String[] ::new);

                        entities = this.controller.getPlanActivities(code);
                    }
                } catch (FitnessControllerException e) {
                    System.err.println(e.getMessage());
                }
            }

            System.out.printf(format, (Object[]) header);
            for (String[] entity : entities) {
                System.out.printf(format, (Object[]) entity);
            }
        };

        MenuEntry[] entries = {
            new MenuEntry("List users", doOperation),
            new MenuEntry("List undone activities", doOperation),
            new MenuEntry("List completed activities", doOperation),
            new MenuEntry("List activities in training plan", doOperation),
            new MenuEntry("Go back", i -> {}),
        };
        new Menu(entries).run();
    }

    /** Asks the user about what modification / removal they wish to perform. */
    private void modifyRemove() {
        if (this.controller.isEmpty()) {
            System.err.println("No users yet!");
            return;
        }

        Consumer<Integer> doOperation = i -> {
            UserInput input = new UserInput();
            long      code  = input.readInt("User code > ",
                                      "User doesn't exist!",
                                      j -> this.controller.userExists(j));

            if (i == 1) {
                SortedSet<DayOfWeek> days = new TreeSet<DayOfWeek>();
                for (DayOfWeek day : DayOfWeek.values()) {
                    if ((boolean) input.read(" " + day.toString() + " (y/n) >",
                                             "Must be y/n!",
                                             s
                                             -> s.equals("y") || s.equals("n"),
                                             s -> s.equals("y") ? true : false))
                        days.add(day);
                }

                try {
                    this.controller.setTrainingPlanDays(code, days);
                    System.out.println("Successful operation!");
                } catch (FitnessControllerException e) {
                    System.err.println(e.getMessage());
                }
            } else {
                this.controller.removeUser(code);
                System.out.println("Successful operation!");
            }
        };

        MenuEntry[] entries = {
            new MenuEntry("Remove user", doOperation),
            new MenuEntry("Edit training plan days", doOperation),
            new MenuEntry("Go back", i -> {}),
        };
        new Menu(entries).run();
    }

    /** Asks the user which time operations to perform. */
    private void timeOperations() {
        MenuEntry[] entries = {
            new MenuEntry("Get current time",
                          i -> {
                              System.out.println(this.controller.getNow().format(
                                  DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")));
                          }),
            new MenuEntry("Leap forward to",
                          i -> {
                              LocalDateTime date = new UserInput().readDate(
                                  true,
                                  "Invalid date or not after current one!",
                                  d -> d.isAfter(this.controller.getNow()));
                              try {
                                  this.controller.leapForward(date);
                              } catch (FitnessControllerException e) {
                                  System.err.println(e.getMessage());
                              }
                          }),
            new MenuEntry("Go back", i -> {}),
        };
        new Menu(entries).run();
    }

    /** Asks the user what query they'd like to run. */
    private void runQuery() {
        String[] classes = this.controller.getQueryClasses().toArray(String[] ::new);

        Consumer<Integer> handler = i -> {
            UserInput     input = new UserInput();
            LocalDateTime start = null, end = null;
            long          code          = 0;
            boolean[]     onlyAltimetry = new boolean[] { false }; // Array wrapper

            if (classes[i].equals("QueryDistance")) {
                code = (new UserInput())
                           .readInt("User code > ",
                                    "User doesn't exist!",
                                    j -> this.controller.userExists(j));

                MenuEntry[] queryEntries = {
                    new MenuEntry("All distance", j -> { onlyAltimetry[0]   = false; }),
                    new MenuEntry("Altimetry only", j -> { onlyAltimetry[0] = true; }),
                };
                new Menu(queryEntries).run();
            }

            if (!classes[i].equals("QueryMostCommonActivity") &&
                !classes[i].equals("QueryHardestTrainingPlan")) {

                System.out.println("Input begin date:");
                start = input.readDate(true, "Invalid date!", d -> true);
                System.out.println("Input end date:");
                end = input.readDate(true, "Invalid date!", d -> true);
            }

            try {
                System.out.println(
                    this.controller.runQuery(classes[i], start, end, code, onlyAltimetry[0]));
            } catch (FitnessControllerException e) {
                System.err.println(e.getMessage());
            }
        };

        if (this.controller.isEmpty()) {
            System.err.println("No users yet!");
            return;
        }

        new Menu(
            Arrays.stream(classes).map(n -> new MenuEntry(n, handler)).toArray(MenuEntry[] ::new))
            .run();
    }

    /** Asks the user which file operations to perform. */
    private void fileOperations() {
        Consumer<Integer> doOperation = (i) -> {
            UserInput input = new UserInput();
            String    path  = input.readString("Path > ");
            try {
                if (i == 0) {
                    this.controller.loadFromFile(path);
                } else {
                    this.controller.saveToFile(path);
                }
                System.out.println("Operation successful!");
            } catch (FitnessControllerException e) {
                System.err.println(e.getMessage());
            }
        };

        MenuEntry[] entries = {
            new MenuEntry("Load state from file", doOperation),
            new MenuEntry("Save state to file", doOperation),
            new MenuEntry("Go back", i -> {}),
        };
        new Menu(entries).run();
    }

    /** Runs this view. This method exists when the user does so. */
    public void run() {
        boolean[]   exitRequest = { false }; // Array wrapper to allow for lambda modification
        MenuEntry[] entries     = { new MenuEntry("Add entity", i -> { this.addEntity(); }),
                                new MenuEntry("List entities", i -> { this.listEntities(); }),
                                new MenuEntry("Modify or remove entities",
                                              i -> { this.modifyRemove(); }),
                                new MenuEntry("Run query", i -> { this.runQuery(); }),
                                new MenuEntry("Time operations", i -> { this.timeOperations(); }),
                                new MenuEntry("File operations", i -> { this.fileOperations(); }),
                                new MenuEntry("Exit", i -> { exitRequest[0] = true; }) };

        try {
            do {
                new Menu(entries).run();
            } while (!exitRequest[0]);
        } catch (NoSuchElementException e) {} // System.in closed
    }

    /**
     * Calculates the hash code of this fitness view.
     *
     * @return The hash code of this fitness view.
     */
    @Override
    public int hashCode() {
        return this.controller.hashCode();
    }

    /**
     * Checks if this fitness view is equal to another object.
     *
     * @param obj Object to be compared with this fitness view.
     * @return Whether <code>this</code> is equal to <code>obj</code>.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;

        FitnessView fitness = (FitnessView) obj;
        return this.controller.equals(fitness.getController());
    }

    /**
     * Creates a deep copy of this fitness view.
     *
     * @return A deep copy of this fitness view.
     */
    @Override
    public FitnessView clone() {
        return new FitnessView(this);
    }

    /**
     * Creates a debug string representation of this fitness view.
     *
     * @return A debug string representation of this fitness view.
     */
    @Override
    public String toString() {
        return String.format("FitnessView(controller = %s)", this.controller.toString());
    }
}
