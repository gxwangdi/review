package com.company;

import static com.company.Utils.OP_FAILURE;
import static com.company.Utils.OP_SUCCESS;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Review {
	public static final String[] commands = { "init", "next", "help" };

	public static final String[] initOptions = { "-all", "-new", "-dir" };

	// File name to persist everything.
	private static final String problemFileName = "./review_problems";
	private static final String dirFileName = "./review_problem_dir";

	// The full path where your problems located, like
	// "/Users/$USER/Projects/Leetcode".
	// TODO: implement customize dirFileName
	private String dirPath = "/Users/gxwangdi/Projects/Leetcode";
	// The upper bound of ranges. Instance variable to serve initNew.
	private long bound = 0;
	private final Map<Range, /* Value is like fileLastModifiedDate@dirName. */String> problems = new HashMap<>();

	public Review() {
	}

	/**
	 * 1. clear problems map 2. open file in read-only mode, the file is indicated
	 * by fileName; 3. load everything from file into problems Map; 4. if file does
	 * not exist or empty, return false; 5. else return true after completion.
	 *
	 * @return true if there is anything loaded from file.
	 */
	private boolean initProblems() {
		problems.clear();
		File file = new File(dirPath);
		// TODO: Add handling if dirPath does not exist.
		bound = 0;
		long sys = System.currentTimeMillis();
		for (File f : file.listFiles()) {
			if (!f.isDirectory()) {
				continue;
			}
			int diffInDays = Utils.getDiffInDays(sys, f.lastModified());
			Range range = new Range(bound, bound + diffInDays, f.lastModified());
			bound += diffInDays;
			problems.put(range, f.getName());
		}
		// TODO: Add handling if persistProblems fails.
		persistProblems();
		printProblems(problems);
		return true;
	}

	/**
	 * 1. open file in write, or read/write mode, the file is indicated by fileName;
	 * 2. load everything in problems map, write them into the file; 3. the format
	 * will be lastModified TimeStamp\nProblem Title\n;
	 * 
	 * @return "success" when all operations are successful, otherwise return
	 *         exception messages, or "failure" for system error.
	 */
	private String persistProblems() {
		File pf = new File(problemFileName);
		try {
			if (pf.exists()) {
				pf.delete();
			}
			pf.createNewFile();
			FileWriter fileWriter = new FileWriter(problemFileName);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			for (Map.Entry<Range, String> entry : problems.entrySet()) {
				printWriter.println(entry.getKey().lastModified);
				printWriter.println(entry.getValue());
			}
			printWriter.close();
		} catch (IOException e) {
			return OP_FAILURE;
		}
		return OP_SUCCESS;
	}

	/**
	 * 1. select one problem randomly based on weight rule; 2. remove the selected
	 * problem from repo(repository); 3. if report is null, refill via
	 * {@link this.init()}.
	 **/
	public String next() {
		System.out.println("next");
		// TODO: implement the below instructions
		// 1. randomly generate a number in [0...bound] range.
		// 2. take the number as a key, get the string from problems.
		// 3. return the string.
		return null;
	}

	/**
	 * @param option
	 * @return "success" for success, or error information.
	 */ // TODO: Add Nullable annotation for areas.
	public String init(Option option, String[] args) {
		System.out.println("init " + option);
		switch (option.getValue()) {
		case Option.DIR_VALUE:
			if (args == null || args.length < 3) {
				System.out.println("Unrecognized commands: " + Arrays.toString(args));
				printHelp();
				return OP_FAILURE;
			}
			dirPath = args[2];
			break;
		case Option.ALL_VALUE:
			String path = Utils.validateDirPath(dirPath);
			if (OP_FAILURE == path) {
				System.out.println("Invalidated path: " + dirPath);
				printHelp();
				return OP_FAILURE;
			}
			if (!initProblems()) {
				return OP_FAILURE;
			}
			break;
		case Option.NEW_VALUE:
			break;
		default:
			System.out.println("Unhandled options: " + Arrays.toString(args));
			// handle unexpected cases.
		}
		return OP_SUCCESS;
	}

	// For debugging.
	private static void printProblems(Map<Range, String> problems) {
		System.out.print("problems:");
		for (Map.Entry<Range, String> entry : problems.entrySet()) {
			System.out.println("\n" + entry.getKey() + ":" + entry.getValue() + "\n");
		}
	}

	public static void printHelp() {
		System.out
				.println("Please enter the right command:\n" + "init -all|-new gets new problems into the repository;\n"
						+ "init -dir <full path dir> inputs the dir to your problem repository;\n"
						+ "next gets the problem you will need to review;\n" + "help prints this page.");
	}

	public static void main(String[] args) {
		if (args == null || args.length < 1) {
			Review.printHelp();
			return;
		}
		Review review = new Review();
		if (Review.commands[0].equals(args[0])) { // init
			if (args.length < 2 || Review.initOptions[1].equals(args[1])) { // init -new
				review.init(Option.NEW, /* args= */null);
				return;
			}
			if (Review.initOptions[0].equals(args[1])) { // init -all
				review.init(Option.ALL, /* args= */null);
				return;
			}
			if (Review.initOptions[2].equals(args[1])) { // init -dir
				review.init(Option.DIR, args);
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
		System.out.println("Unrecognized commands.");
		Review.printHelp();
	}

	private static class Range {
		long start;
		long end;
		long lastModified;

		Range(long start, long end, long lastModified) {
			this.start = start;
			this.end = end;
			this.lastModified = lastModified;
		}

		@Override
		public String toString() {
			return "[" + start + "," + end + "," + lastModified + "]";
		}
	}

	enum Option {

		ALL(Option.ALL_VALUE), NEW(Option.NEW_VALUE), DIR(Option.DIR_VALUE);

		public static final int ALL_VALUE = 0;
		public static final int NEW_VALUE = 1;
		public static final int DIR_VALUE = 2;

		private final int value;

		Option(int v) {
			value = v;
		}

		public int getValue() {
			return value;
		}
	}
}
