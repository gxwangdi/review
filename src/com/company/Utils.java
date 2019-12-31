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
	 * @param bound
	 *            the upper bound of the range.
	 * @return a random long between 0 and bound
	 */
	public static long getRandom(long bound) {
		long left = 0L;
		long right = bound;
		long next = left + (long) (Math.random() * (right - left));
		return next;
    }
}
