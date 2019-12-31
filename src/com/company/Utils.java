package com.company;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * All necessary utility functions.
 * */
public class Utils {
    private Utils() {}

    public static final String OP_SUCCESS = "success";
    public static final String OP_FAILURE = "failure";
    public static final int INVALID_DIFF = -1;

    /**
     * @param bar the given system time in ms.
     * @param candidate the given last modified time in ms.
     * @return the difference between bar and candidate in days.
     * */
    public static int getDiffInDays(long bar, long candidate) {
        long diffInMs = bar - candidate;
        int days = (int)TimeUnit.MILLISECONDS.toDays(diffInMs);
        return days;
    }

    /**
     * @return validated dir path, or OP_FAILURE if dir path is invalid.
     * */
    public static String validateDirPath(String dirPath) {
        if (dirPath == null || dirPath.length() < 1) {
            return OP_FAILURE;
        }
        File file = new File(dirPath);
        if (!file.exists() || !file.canRead() || !file.isDirectory()) {
            return OP_FAILURE;
        }
        return dirPath;
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


    // For testing utils.
    public static void main(String[] args) {
        long sys = System.currentTimeMillis();
    }
}
