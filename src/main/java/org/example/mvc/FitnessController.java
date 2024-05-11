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

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/** Adapter between <code>FitnessView</code> and the <code>FitnessModel</code>. */
public class FitnessController {
    /** Extra fields that may be needed need to construct extended activities. */
    public enum ActivityExtraField {
        /** An integer telling how many times the exercise needs to be executed. */
        REPETITIONS,
        /** The weight (in kg) that needs to be lifted in the exercise. */
        WEIGHT,
        /** The distance traversed in the exercise. */
        DISTANCE,
        /** How variable the altitude is in the exercise. */
        ALTIMETRY
    }

    /** The fitness model this controller interacts with. */
    private FitnessModel model;

    /** Classes of users that can be created. */
    private Map<String, Class<?>> userClasses;

    /** Classes of activities that can be created. */
    private Map<String, Class<?>> activityClasses;

    /** Extra fields that need to provided to create each activity. */
    private Map<String, SortedSet<ActivityExtraField>> activityFields;

    /** Classes of queries that can be run. */
    private Map<String, Class<?>> queryClasses;

    /** Creates a new fitness controller. */
    public FitnessController() {
        this.model = new FitnessModel();

        Class<?>[] userClasses = {BeginnerUser.class, IntermediateUser.class, AdvancedUser.class};
        this.userClasses =
                Arrays.stream(userClasses)
                        .collect(Collectors.toMap(c -> c.getSimpleName(), c -> c));

        Class<?>[] activityClasses = {
            ActivityDiamondPushUp.class,
            ActivityMountainRun.class,
            ActivityPushUp.class,
            ActivityTrackRun.class,
            ActivityWeightLifting.class
        };
        this.activityClasses =
                Arrays.stream(activityClasses)
                        .collect(Collectors.toMap(c -> c.getSimpleName(), c -> c));

        this.activityFields = new HashMap<String, SortedSet<ActivityExtraField>>();
        for (Class c : activityClasses) {
            SortedSet<ActivityExtraField> fields = new TreeSet<ActivityExtraField>();

            if (ActivityRepetition.class.isAssignableFrom(c))
                fields.add(ActivityExtraField.REPETITIONS);
            if (ActivityRepetitionWeighted.class.isAssignableFrom(c))
                fields.add(ActivityExtraField.WEIGHT);
            if (ActivityDistance.class.isAssignableFrom(c)) fields.add(ActivityExtraField.DISTANCE);
            if (ActivityAltimetryDistance.class.isAssignableFrom(c))
                fields.add(ActivityExtraField.ALTIMETRY);

            this.activityFields.put(c.getSimpleName(), fields);
        }

        Class<?>[] queryClasses = {
            QueryDistance.class,
            QueryHardestTrainingPlan.class,
            QueryMostActivities.class,
            QueryMostCalories.class,
            QueryMostCommonActivity.class
        };
        this.queryClasses =
                Arrays.stream(queryClasses)
                        .collect(Collectors.toMap(c -> c.getSimpleName(), c -> c));
    }

    /**
     * Creates a fitness controller from the value of its fields.
     *
     * @param model Fitness model that this controller interacts with.
     */
    public FitnessController(FitnessModel model) {
        this(); // Initialize classes.
        this.model = model;
    }

    /**
     * Copy constructor of a fitness controller.
     *
     * @param controller Fitness controller to be copied.
     */
    public FitnessController(FitnessController controller) {
        this(); // Initialize classes.
        this.model = controller.getModel();
    }

    /**
     * Gets the model that this controller interacts with.
     *
     * @return The model that this controller interacts with.
     */
    private FitnessModel getModel() {
        return this.model;
    }

    /**
     * Gets the names of user classes that can be created.
     *
     * @return The names of the user classes that can be created.
     */
    public SortedSet<String> getUserClasses() {
        return new TreeSet<String>(this.userClasses.keySet());
    }

    /**
     * Gets the names of activity classes that can be created.
     *
     * @return The names of the activity classes that can be created.
     */
    public SortedSet<String> getActivityClasses() {
        return new TreeSet<String>(this.activityClasses.keySet());
    }

    /**
     * Gets the extra fields that need to be inputted for an activity. Will raise an exception if
     * the activity class doesn't exist.
     *
     * @param className Name of the activity class.
     * @return The extra fields that need to be inputted for an activity.
     */
    public SortedSet<ActivityExtraField> getActivityExtraFields(String className) {
        return new TreeSet<ActivityExtraField>(this.activityFields.get(className));
    }

    /**
     * Gets the names of query classes that can be created.
     *
     * @return The names of the query classes that can be created.
     */
    public SortedSet<String> getQueryClasses() {
        return new TreeSet<String>(this.queryClasses.keySet());
    }

    /**
     * Gets whether an activity is considered to be hard. Will raise an exception if the activity
     * class doesn't exist.
     *
     * @param className Name of the activity class.
     * @return Whether an activity is considered to be hard.
     */
    public boolean activityIsHard(String className) {
        return ActivityHard.class.isAssignableFrom(this.activityClasses.get(className));
    }

    /**
     * Checks if there aren't users in the application.
     *
     * @return Whether there aren't users in the application.
     */
    public boolean isEmpty() {
        return this.model.isEmpty();
    }

    /**
     * Checks if a user with a given identifier exists in the application.
     *
     * @param userCode Identifier code of the user.
     * @return Whether a user with the given identifier exists in the application.
     */
    public boolean userExists(long userCode) {
        return this.model.getUser(userCode) != null;
    }

    /**
     * Gets the list of users in a presentable form.
     *
     * @return A list of presented user field arrays (code, name, class, address, email and bpm).
     */
    public List<String[]> getUsers() {
        return model.getUsers().values().stream()
                .map(
                        u ->
                                new String[] {
                                    Long.toString(u.getCode()),
                                    u.getName(),
                                    u.getClass().getSimpleName(),
                                    u.getAddress(),
                                    u.getEmail(),
                                    Integer.toString(u.getAverageBPM())
                                })
                .collect(Collectors.toList());
    }

    /**
     * Transforms a set of activities into a list of entries that a view can show.
     *
     * @param user User used for calory counting.
     * @param activities Set of activities to be shown.
     * @returns For each activity, its fields (class, duration, date, bpm, reps, weight, distance,
     *     altimetry).
     */
    private List<String[]> showActivities(User user, SortedSet<Activity> activities) {
        List<String[]> ret = new ArrayList<String[]>();
        for (Activity a : activities) {
            String[] fields =
                    new String[] {
                        a.getExecutionDate()
                                .format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")),
                        Long.toString(a.getExecutionTime().toMinutes()),
                        Double.toString(a.countCalories(user)),
                        a.getClass().getSimpleName(),
                        Integer.toString(a.getBPM()),
                        "",
                        "",
                        "",
                        ""
                    };

            SortedSet<ActivityExtraField> extraFields =
                    this.activityFields.get(a.getClass().getSimpleName());
            if (extraFields.contains(ActivityExtraField.REPETITIONS))
                fields[5] = Integer.toString(((ActivityRepetition) a).getNumberOfReps());
            if (extraFields.contains(ActivityExtraField.WEIGHT))
                fields[6] = Double.toString(((ActivityRepetitionWeighted) a).getWeightsHeft());
            if (extraFields.contains(ActivityExtraField.DISTANCE))
                fields[7] = Double.toString(((ActivityDistance) a).getDistanceToTraverse());
            if (extraFields.contains(ActivityExtraField.ALTIMETRY))
                fields[8] = Double.toString(((ActivityAltimetryDistance) a).getAltimetry());

            ret.add(fields);
        }
        return ret;
    }

    /**
     * Gets the activities a user still needs to do.
     *
     * @param userCode Identifier code of the user to get the activities from.
     * @throws FitnessControllerException User not found.
     * @return For each activity, its fields (date, duration, calories, class, bpm, reps, weight,
     *     distance, altimetry).
     */
    public List<String[]> getTodoActivities(long userCode) throws FitnessControllerException {
        User user = this.model.getUser(userCode);
        if (user == null) throw new FitnessControllerException("User doesn't exist!");
        return this.showActivities(user, user.getActivities().getTodo());
    }

    /**
     * Gets the activities a user has completed.
     *
     * @param userCode Identifier code of the user to get the activities from.
     * @throws FitnessControllerException User not found.
     * @return For each activity, its fields (date, duration, calories, class, bpm, reps, weight,
     *     distance, altimetry).
     */
    public List<String[]> getDoneActivities(long userCode) throws FitnessControllerException {
        User user = this.model.getUser(userCode);
        if (user == null) throw new FitnessControllerException("User doesn't exist!");
        return this.showActivities(user, user.getActivities().getDone());
    }

    /**
     * Gets the days of the week a user executes their training plan.
     *
     * @param userCode Identifier code of the user to get the activities from.
     * @throws FitnessControllerException User not found.
     * @return The days of the week a user executes their training plan.
     */
    public SortedSet<DayOfWeek> getTrainingPlanDays(long userCode)
            throws FitnessControllerException {
        User user = this.model.getUser(userCode);
        if (user == null) throw new FitnessControllerException("User doesn't exist!");
        return user.getActivities().getTrainingPlan().getRepetitions();
    }

    /**
     * Gets the activities a user has in their training plan.
     *
     * @param userCode Identifier code of the user to get the activities from.
     * @throws FitnessControllerException User not found.
     * @return For each activity, its fields (date, duration, plan repetitions, calories, class,
     *     bpm, reps, weight, distance, altimetry).
     */
    public List<String[]> getPlanActivities(long userCode) throws FitnessControllerException {
        User user = this.model.getUser(userCode);
        if (user == null) throw new FitnessControllerException("User doesn't exist!");

        List<String[]> ret = new ArrayList<String[]>();
        Map<Activity, Integer> activities = user.getActivities().getTrainingPlan().getActivities();
        for (Map.Entry<Activity, Integer> entry : activities.entrySet()) {
            SortedSet<Activity> single = new TreeSet<Activity>();
            single.add(entry.getKey());

            List<String> fields =
                    new ArrayList<String>(
                            Arrays.asList(this.showActivities(user, single).iterator().next()));
            fields.set(
                    0,
                    entry.getKey().getExecutionDate().format(DateTimeFormatter.ofPattern("HH:mm")));
            fields.add(3, Integer.toString(entry.getValue()));
            fields.set(2, Double.toString(Double.parseDouble(fields.get(2)) * entry.getValue()));
            ret.add(fields.toArray(String[]::new));
        }

        return ret;
    }

    /**
     * Adds a user from the value of its fields.
     *
     * @param className Name of the user's class.
     * @param name Full name of the user.
     * @param address Street address of the user.
     * @param email Email address of the user.
     * @param averageBPM Average cardiac rhythm of the user when exercising.
     * @return The identifier attributed to the user.
     * @throws FitnessControllerException Programmer error: missing constructor.
     * @throws FitnessControllerException Error instantiating the new user.
     */
    public long addUser(String className, String name, String address, String email, int averageBPM)
            throws FitnessControllerException {

        Class<?> userClass = this.userClasses.get(className);
        try {
            Constructor<?> constructor =
                    userClass.getConstructor(
                            long.class,
                            String.class,
                            String.class,
                            String.class,
                            int.class,
                            UserActivities.class);
            User user =
                    (User)
                            constructor.newInstance(
                                    1, name, address, email, averageBPM, new UserActivities());
            return this.model.addUser(user);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            throw new FitnessControllerException(className + " missing crucial constructor!");
        } catch (InvocationTargetException e) {
            throw new FitnessControllerException(e.getTargetException().getMessage());
        }
    }

    /**
     * Creates a new activity from data gotten from a view.
     *
     * @param className Name of the activity's class. Unchecked.
     * @param duration Duration of the activity in minutes.
     * @param date Execution date of the activity.
     * @param fields Extra fields in the activity.
     * @throws FitnessControllerException Programmer error: missing constructor.
     * @throws FitnessControllerException Error instantiating the new activity.
     * @return The created activity.
     */
    private Activity constructActivity(
            String className,
            int duration,
            LocalDateTime date,
            SortedMap<ActivityExtraField, Object> fields)
            throws FitnessControllerException {

        Class<?> activityClass = this.activityClasses.get(className);
        List<Object> arguments = new ArrayList<Object>();
        List<Class> constructorArgumentTypes = new ArrayList<Class>();

        arguments.add(Duration.ofMinutes(duration));
        constructorArgumentTypes.add(Duration.class);
        arguments.add(date);
        constructorArgumentTypes.add(LocalDateTime.class);
        arguments.add(1);
        constructorArgumentTypes.add(int.class);

        for (Map.Entry<ActivityExtraField, Object> entry : fields.entrySet()) {
            arguments.add(entry.getValue());
            if (entry.getKey() == ActivityExtraField.REPETITIONS)
                constructorArgumentTypes.add(int.class);
            else constructorArgumentTypes.add(double.class);
        }

        try {
            Constructor<?> constructor =
                    activityClass.getConstructor(constructorArgumentTypes.toArray(Class[]::new));
            return (Activity) constructor.newInstance(arguments.toArray());
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            throw new FitnessControllerException(className + " missing crucial constructor!");
        } catch (InvocationTargetException e) {
            throw new FitnessControllerException(e.getTargetException().getMessage());
        }
    }

    /**
     * Adds an activity to a user's collection of isolated activities.
     *
     * @param userCode Identifier code of the user to add the activity to.
     * @param className Name of the activity class to be created. Unchecked.
     * @param duration Duration of the activity in minutes.
     * @param date Execution date of the activity.
     * @param fields Extra fields in the activity.
     * @throws FitnessControllerException Programmer error: missing constructor.
     * @throws FitnessControllerException Error instantiating the new activity.
     * @throws FitnessControllerException User not found.
     * @throws FitnessControllerException Activity starts before current date.
     * @throws FitnessControllerException Activity overlaps with existing activities.
     */
    public void addActivity(
            long userCode,
            String className,
            int duration,
            LocalDateTime date,
            SortedMap<ActivityExtraField, Object> fields)
            throws FitnessControllerException {

        Activity activity = this.constructActivity(className, duration, date, fields);
        try {
            this.model.addActivity(userCode, activity);
        } catch (FitnessModelException | ActivityOverlapException e) {
            throw new FitnessControllerException(e.getMessage());
        }
    }

    /**
     * Adds an activity to a user's training plan.
     *
     * @param userCode Identifier code of the user to add the activity to. Unchecked.
     * @param className Name of the activity class to be created.
     * @param duration Duration of the activity in minutes.
     * @param date Execution date of the activity. Its bpm will be replaced by the user's and its
     *     YYYY/MM/DD part of the date will be ignored.
     * @param fields Extra fields in the activity.
     * @param repetitions Number of activity repetitions in the training plan.
     * @throws FitnessControllerException Programmer error: missing constructor.
     * @throws FitnessControllerException Error instantiating the new activity.
     * @throws FitnessControllerException User not found.
     * @throws FitnessControllerException Activity overlaps with existing activities.
     */
    public void addActivityToTrainingPlan(
            long userCode,
            String className,
            int duration,
            LocalDateTime date,
            SortedMap<ActivityExtraField, Object> fields,
            int repetitions)
            throws FitnessControllerException {

        Activity activity = this.constructActivity(className, duration, date, fields);
        try {
            this.model.addActivityToTrainingPlan(userCode, activity, repetitions);
        } catch (FitnessModelException | ActivityOverlapException e) {
            throw new FitnessControllerException(e.getMessage());
        }
    }

    /**
     * Removes a user from a user from the application if it exists.
     *
     * @param userCode Identifier code of the user to be removed.
     */
    public void removeUser(long userCode) {
        this.model.removeUser(userCode);
    }

    /**
     * Sets the days a training plan of the user is executed.
     *
     * @param userCode Identifier code of the user to be removed.
     * @param days Days of the training plan.
     * @throws FitnessControllerException User doesn't exist.
     */
    public void setTrainingPlanDays(long userCode, SortedSet<DayOfWeek> days)
            throws FitnessControllerException {
        try {
            this.model.setTrainingPlanDays(userCode, days);
        } catch (FitnessModelException | ActivityOverlapException e) {
            throw new FitnessControllerException(e.getMessage());
        }
    }

    /**
     * Gets the current time in this application.
     *
     * @return The current time in this application.
     */
    public LocalDateTime getNow() {
        return this.model.getNow();
    }

    /**
     * Runs a query.
     *
     * @param className Name of the activity class to be created.
     * @param start Start of date range inteval. Can be <code>null</code> when not applicable.
     * @param end End of date range inteval. Can be <code>null</code> when not applicable.
     * @param userCode Identifier code of the user to take into account (when applicable).
     * @param altimetryOnly Whether only altimetry activities should be counted for QueryDistance.
     * @throws FitnessControllerException User not found (QueryDistance only).
     * @return Textual output of the query.
     */
    public String runQuery(
            String className,
            LocalDateTime start,
            LocalDateTime end,
            long userCode,
            boolean altimetryOnly)
            throws FitnessControllerException {

        if (className.equals("QueryDistance")) {
            Class<? extends ActivityDistance> filterClass =
                    altimetryOnly ? ActivityAltimetryDistance.class : ActivityDistance.class;
            QueryDistance q = new QueryDistance(filterClass, start, end);

            try {
                this.model.runQuery(q, userCode);
            } catch (FitnessModelException e) {
                throw new FitnessControllerException(e.getMessage());
            }

            return String.format("%f km", q.getDistance());
        } else if (className.equals("QueryHardestTrainingPlan")) {
            QueryHardestTrainingPlan q = new QueryHardestTrainingPlan();
            this.model.runQuery(q);
            User u = q.getMaxUser();

            if (u == null) return "No users!";
            return String.format("(%d) %s - %f kcal", u.getCode(), u.getName(), q.getMaxCalories());
        } else if (className.equals("QueryMostActivities")) {
            QueryMostActivities q = new QueryMostActivities(start, end);
            this.model.runQuery(q);
            User u = q.getMaxUser();

            if (u == null) return "No users!";
            return String.format(
                    "(%d) %s - %d completed activities",
                    u.getCode(), u.getName(), q.getMaxActivities());
        } else if (className.equals("QueryMostCalories")) {
            QueryMostCalories q = new QueryMostCalories(start, end);
            this.model.runQuery(q);
            User u = q.getMaxUser();

            if (u == null) return "No users!";
            return String.format("(%d) %s - %f kcal", u.getCode(), u.getName(), q.getMaxCalories());
        } else if (className.equals("QueryMostCommonActivity")) {
            QueryMostCommonActivity q = new QueryMostCommonActivity();
            this.model.runQuery(q);
            Map.Entry<String, Integer> activity = q.getTopActivity();

            if (activity == null) return "No activities!";
            return String.format("%s - %d executions", activity.getKey(), activity.getValue());
        }

        return "No such query " + className;
    }

    /**
     * Advances time to another date, updating which activities have been completed.
     *
     * @param date Date to make the new current date.
     * @throws FitnessControllerException Date not after current date.
     */
    public void leapForward(LocalDateTime date) throws FitnessControllerException {
        try {
            this.model.leapForward(date);
        } catch (FitnessModelException e) {
            throw new FitnessControllerException(e.getMessage());
        }
    }

    /**
     * Loads the data of the application from a file.
     *
     * @param path Path to the file.
     * @throws FitnessControllerException Failure to load from file.
     */
    public void loadFromFile(String path) throws FitnessControllerException {
        try {
            this.model.loadFromFile(path);
        } catch (IOException e) {
            throw new FitnessControllerException("Failed to read file contents!");
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new FitnessControllerException("Invalid file contents!");
        }
    }

    /**
     * Saves the data of the application to a file.
     *
     * @param path Path to the file.
     * @throws FitnessControllerException Failure to write to file.
     */
    public void saveToFile(String path) throws FitnessControllerException {
        try {
            this.model.saveToFile(path);
        } catch (IOException e) {
            throw new FitnessControllerException("Failed to write to file!");
        }
    }

    /**
     * Calculates the hash code of this fitness controller.
     *
     * @return The hash code of this fitness controller.
     */
    @Override
    public int hashCode() {
        return this.model.hashCode();
    }

    /**
     * Checks if this fitness controller is equal to another object.
     *
     * @param obj Object to be compared with this fitness controller.
     * @return Whether <code>this</code> is equal to <code>obj</code>.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        FitnessController fitness = (FitnessController) obj;
        return this.model.equals(fitness.getModel());
    }

    /**
     * Creates a deep copy of this fitness controller.
     *
     * @return A deep copy of this fitness controller.
     */
    @Override
    public FitnessController clone() {
        return new FitnessController(this);
    }

    /**
     * Creates a debug string representation of this fitness controller.
     *
     * @return A debug string representation of this fitness controller.
     */
    @Override
    public String toString() {
        return String.format("FitnessController(model = %s)", this.model.toString());
    }
}
