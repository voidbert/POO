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

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;

/** A wrapper of a Scanner for easier user input handling. */
public class UserInput {
    /** Internal scanner used for reading input. */
    private Scanner scanner;

    /** Creates a scanner of the standard input. */
    public UserInput() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Copy constructor of a scanner wrapper. It's impossible to clone scanners. Just create a new
     * one.
     *
     * @param scanner Scanner wrapper to be copied.
     */
    public UserInput(UserInput scanner) {
        this();
    }

    /**
     * Prompts the user for input and reads their input multiple times until it's valid. The final
     * input is converted according to a provided function.
     *
     * @param prompt Text prompt written to <code>System.out</code> before asking for input.
     * @param error Error written to <code>System.err</code> in case the input is invalid. Leave
     *     <code>null</code> for no error.
     * @param validate Input validation predicate.
     * @param convert Final conversion function.
     * @return The user's input.
     */
    public Object read(
            String prompt,
            String error,
            Predicate<String> validate,
            Function<String, Object> convert) {

        String ret = null;
        do {
            System.out.print(prompt);
            String line = scanner.nextLine();
            if (validate.test(line)) ret = line;
            else if (error != null) System.err.println(error);
        } while (ret == null);
        return convert.apply(ret);
    }

    /**
     * Prompts the user for textual input and reads a single line. No errors can occur.
     *
     * @param prompt Text prompt written to <code>System.out</code> before asking for input.
     * @return The user's input.
     */
    public String readString(String prompt) {
        return (String) this.read(prompt, null, s -> true, s -> s);
    }

    /**
     * Prompts the user for textual input and reads their input multiple times until it's valid.
     *
     * @param prompt Text prompt written to <code>System.out</code> before asking for input.
     * @param error Error written to <code>System.err</code> in case the input is invalid.
     * @param validate Input validation predicate.
     * @return The user's input.
     */
    public String readString(String prompt, String error, Predicate<String> validate) {
        return (String) this.read(prompt, error, validate, s -> s);
    }

    /**
     * Prompts the user for integer input and reads their input multiple times until it's valid.
     *
     * @param prompt Text prompt written to <code>System.out</code> before asking for input.
     * @param error Error written to <code>System.err</code> in case the input is invalid.
     * @param validate Input validation predicate.
     * @return The user's input.
     */
    public int readInt(String prompt, String error, Predicate<Integer> validate) {
        return (Integer)
                this.read(
                        prompt,
                        error,
                        s -> {
                            try {
                                int i = Integer.parseInt(s);
                                return validate.test(i);
                            } catch (NumberFormatException e) {
                                return false;
                            }
                        },
                        s -> Integer.parseInt(s));
    }

    /**
     * Prompts the user for decimal input and reads their input multiple times until it's valid.
     *
     * @param prompt Text prompt written to <code>System.out</code> before asking for input.
     * @param error Error written to <code>System.err</code> in case the input is invalid.
     * @param validate Input validation predicate.
     * @return The user's input.
     */
    public double readDouble(String prompt, String error, Predicate<Double> validate) {
        return (Double)
                this.read(
                        prompt,
                        error,
                        s -> {
                            try {
                                double d = Double.parseDouble(s);
                                return validate.test(d);
                            } catch (NumberFormatException e) {
                                return false;
                            }
                        },
                        s -> Double.parseDouble(s));
    }

    /**
     * Prompts the user for date input and reads their input multiple times until it's valid.
     *
     * @param readYMD Whether to read year, month and day, or to leave as 0001/01/01.
     * @param error Error written to <code>System.err</code> in case the input is invalid.
     * @param validate Input validation predicate.
     * @return The user's input.
     */
    public LocalDateTime readDate(
            boolean readYMD, String error, Predicate<LocalDateTime> validate) {
        LocalDateTime d = LocalDateTime.of(1, 1, 1, 0, 0, 0);
        boolean success = false;
        do {
            try {
                if (readYMD) {
                    d = d.withYear(this.readInt("Year > ", "Must be an integer!", y -> true));
                    d = d.withMonth(this.readInt("Month > ", "Must be an integer!", m -> true));
                    d = d.withDayOfMonth(this.readInt("Day > ", "Must be an integer!", i -> true));
                }

                d = d.withHour(this.readInt("Hour > ", "Must be an integer!", h -> true));
                d = d.withMinute(this.readInt("Minute > ", "Must be an integer!", m -> true));

                if (validate.test(d)) success = true;
                else System.err.println(error);
            } catch (DateTimeException e) {
                System.err.println(error);
            }
        } while (!success);
        return d;
    }

    /**
     * Calculates the hash code of this scanner wrapper.
     *
     * @return The hash code of this scanner wrapper.
     */
    @Override
    public int hashCode() {
        return this.scanner.hashCode();
    }

    /**
     * Checks if this scanner wrapper is equal to another object.
     *
     * @param obj Object to be compared with this scanner wrapper.
     * @return Whether <code>this</code> is equal to <code>obj</code>.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        return true; // Consider all scanners to be the same
    }

    /**
     * Creates a deep copy of this scanner wrapper.
     *
     * @return A deep copy of this scanner wrapper.
     */
    @Override
    public UserInput clone() {
        return new UserInput(this);
    }
}
