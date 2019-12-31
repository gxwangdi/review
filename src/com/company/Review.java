package com.company;

import static com.company.Utils.OP_FAILURE;
import static com.company.Utils.OP_SUCCESS;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Review {
	public static final String[] commands = { "init", "next", "help", "quit" };
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
		if (!OP_SUCCESS.equals(resumeProblems())) {
			initProblems();
		}
	}

	public void onExit() {

		persistProblems();
		System.out.println("onExit()");
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
		FileWriter fileWriter = null;
		PrintWriter printWriter = null;
		try {
			if (pf.exists()) {
				pf.delete();
			}
			pf.createNewFile();
			fileWriter = new FileWriter(problemFileName);
			printWriter = new PrintWriter(fileWriter);
			for (Map.Entry<Range, String> entry : problems.entrySet()) {
				printWriter.println(entry.getKey().lastModified);
				printWriter.println(entry.getValue());
			}
		} catch (IOException e) {
			return e.getMessage();
		} finally {
			if (printWriter != null) {
				printWriter.close();
			}
		}
		return OP_SUCCESS;
	}

	/**
	 * 
	 * @return "success" when all operations are successful, otherwise return
	 *         exception messages, or "failure" for system error.
	 */
	private String resumeProblems() {
		Scanner scanner = null;
		try {
			File pf = new File(problemFileName);
			if (!pf.exists()) {
				return OP_FAILURE;
			}
			scanner = new Scanner(pf);
			problems.clear();
			long sys = System.currentTimeMillis();
			bound = 0;
			while (scanner.hasNext()) {
				long timeStamp = scanner.nextLong();
				scanner.nextLine();
				if (!scanner.hasNext()) {
					break;
				}
				String title = scanner.nextLine();
				int diffInDays = Utils.getDiffInDays(sys, timeStamp);
				problems.put(new Range(bound, bound + diffInDays, timeStamp), title);
				bound += diffInDays;
			}
		} catch (FileNotFoundException e) {
			return e.getMessage();
		} finally {
			if (scanner != null) {
				scanner.close();
			}
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
		long next = Utils.getRandom(bound);
		// TODO: implement binary search here to speedup the process.
		for (Range range : problems.keySet()) {
			if (range.start > next) {
				continue;
			}
			if (range.end < next) {
				continue;
			}
			String res = problems.get(range);
			problems.remove(range);
			return res;
		}
		return "cannot find the problem";
	}

	/**
	 * @param scanner
	 * @param option
	 * @return "success" for success, or error information.
	 */
	public String init(Scanner scanner, Option option, String arg) {
		System.out.println("init " + option);
		if (scanner == null) {
			scanner = new Scanner(System.in);
		}
		switch (option.getValue()) {
		case Option.DIR_VALUE:
			if (!scanner.hasNext()) {
				System.out.print("input problem set full directory(e.g, /Users/gxwangdi/Projects/Leetcode):");
			}
			dirPath = scanner.next();
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
			System.out.println("Unhandled options: " + arg);
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
						+ "next gets the problem you will need to review;\n" + "quit terminates this proram;\n"
						+ "help prints this page.");
	}

	public static void main(String[] args) {
		Review review = new Review();
		while (true) {
			Scanner scanner = new Scanner(System.in);
			System.out.print("Input a command(help for all commands):");
			String cmd = scanner.next();
			if (Review.commands[3].equals(cmd)) {// quit
				review.onExit();
				break;
			}
			if (Review.commands[1].equals(cmd)) { // next
				System.out.println(review.next());
				continue;
			}
			if (Review.commands[2].equals(cmd)) { // help
				Review.printHelp();
				continue;
			}
			if (Review.commands[0].equals(cmd)) { // init
				if (!scanner.hasNext()) {
					review.init(scanner, Option.NEW, /* args= */null);
					continue;
				}
				String arg = scanner.next();
				if (Review.initOptions[0].equals(arg)) { // init -all
					review.init(scanner, Option.ALL, /* args= */null);
					continue;
				}
				if (Review.initOptions[1].equals(arg)) { // init - new
					review.init(scanner, Option.NEW, /* args= */null);
					continue;
				}
				if (Review.initOptions[2].equals(arg)) { // init -dir
					review.init(scanner, Option.DIR, arg);
					continue;
				}
			}
			System.out.println("Unrecognized commands.");
			Review.printHelp();
		}
	}

	private static class Range {
		final long start;
		final long end;
		final long lastModified;

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
