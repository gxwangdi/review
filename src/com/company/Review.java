package com.company;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Review {
    public static final String[] commands = {"init", "next", "help"};

    public static final String[] initOptions = {"-all", "-new"};

    public static final String OP_SUCCESS = "success";
    public static final String OP_FAILURE = "failure";
    public static final int INVALID_DIFF = -1;

    // File name to persist everything.
    private static final String fileName = "review_problems";

    private int bound = 0;
    private final Map<
            /*Key is in [0...bound] range.*/Integer
            , /*Value is like fileLastModifiedDate@dirName.*/String> problems = new HashMap<>();

    public Review() {}

    /**
     * 1. open file in readonly mode, the file is indicated by fileName;
     * 2. load everything from file into problems Map;
     * 3. if file does not exist or empty, return false;
     * 4. else return true after completion.
     *
     * @return true if there is anything loaded from file.
     */
    private boolean initProblems() {
        // TODO: add implementation based on javadoc.
        file  = new next();//call next function
        problems.put(fileName); // put file into map

        if (file == null || file.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * 1. open file in write, or read/write mode, the file is indicated by fileName;
     * 2. load everything in problems map, write them into the file;
     * @return "success" when all operations are successful, otherwise return exception messages,
     * or "failure" for system error.
     */
    private String persistProblems() {
        // TODO: add implementation based on javadoc.
        return OP_SUCCESS;
    }

    /**
     * @param fileLastModifyDate a date
     * @return the difference between fileLastModifyDate and currentDate in days.
     * */
    private int getRange(Date fileLastModifyDate) {
        // TODO: add implementation based on javadoc.
        return 0;
    }

    /**
     * 1. select one problem randomly based on weight rule;
     * 2. remove the selected problem from repo(repository);
     * 3. if report is null, refill via {@link this.init()}.
     **/
    public String next() {
        System.out.println("next");
        // TODO: implement the below instructions
        // 1. randomly generate a number in [0...bound] range.
        // 2. take the number as a key, get the string from problems.
        // 3. return the string.
        return null;
    }

    enum Option {//数据类型
        ALL, NEW;
    }

    /**
     * @param option
     * @return "success" for success, or error information.
     */
    public String init(Option option) {
        System.out.println("init " + option);
        return OP_SUCCESS;
    }

    /**
     * 1. scan all problems in specified directory;
     * 2. if diff == INVALID_DIFF, clear problems Map and add all scanned problems in problems Map,
     * and persist into a file;
     * 3. if diff == positive number, for problems with last modified Date vs current date difference in days
     * less than diff, add them directly into problems Map;
     * @param diff difference threshold in days, -1 if it is invalid.
     * @return "success" if all operations are done successfully, otherwise return "failure" or exception messages.
     * */
    private String init(int diff) {
        return OP_SUCCESS;
    }

    public static void printHelp() {
        System.out.println(
                "Please enter the right command:\n" +
                        "init -all|-new gets new problems into the repository;\n" +
                        "next gets the problem you will need to review;\n" +
                        "help prints this page."
        );
    }


    public static void main(String[] args) {
        if (args == null || args.length < 1) {
            Review.printHelp();
            return;
        }
        Review review = new Review();
        if (Review.commands[0].equals(args[0])) { // init
            if (args.length < 2 || Review.initOptions[1].equals(args[1])) { // init -new
                review.init(Option.NEW);
                return;
            }
            if (Review.initOptions[0].equals(args[1])) { // init -all
                review.init(Option.ALL);
                return;
            }
        } else if (Review.commands[1].equals(args[0])) { // next
            System.out.println(review.next());
            return;
        } else if (Review.commands[2].equals(args[0])) { // help
            Review.printHelp();
            return;
        }
        // default: for all unhandled cases.
        Review.printHelp();
    }

}

