package gitlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Chloe Lin, Christal Huang
 */
public class Main {

	/** Current Working Directory. */
	static final File CWD = new File(".");
	/** directory for .gitlet */
	static final File GITLET_FOLDER = new File(".gitlet");
	/** directory for staging area and stores file/blob mapping */
	static final File STAGING_FOLDER = Utils.join(GITLET_FOLDER, "staging");
	/** file for staging area and stores file/blob mapping */
	HashMap<String, String> trackedFilesMap = new HashMap<String, String>();
	static final File trackedFiles = Utils.join(STAGING_FOLDER, "trackedFiles");
	/** directory for storing commits and blobs */
	static final File OBJECTS_FOLDER = Utils.join(".gitlet", "objects");
	/** directory for storing commit objects */
	static final File Commits = Utils.join(OBJECTS_FOLDER,  "commits");
	/** directory for storing blobs */
	static final File Blobs = Utils.join(OBJECTS_FOLDER,  "blobs");
	/** commit hash current head*/
	static final File HEAD = new File("head");
	/** directory for storing branch and related commit has*/
	static final File Branches = new File("branch");
	/** directory for storing all commit logs for HEAD and branches*/
	static final File LOGS_FOLDER = Utils.join(GITLET_FOLDER, "logs");
	/** directory for storing the most recent commit hash*/
	static final File REFS_FOLDER = Utils.join(GITLET_FOLDER, "refs");
	static final File HEADS_REFS_FOLDER = Utils.join(REFS_FOLDER, "heads");

	/** Usage: java gitlet.Main ARGS, where ARGS contains
	*  <COMMAND> <OPERAND> .... */
	public static void main(String... args) throws IOException {
        validateNumCommand(args);
        switch (args[0]) {
            case "init":
                validateGitlet();
                setupPersistence();
                break;
            case "add":
                add(args);
                break;
            case "commit":
                commit(args);
                break;
            case "log":
                printAllLog(args);
                break;
            default:
                validateCommand();
            }
        return;
	}

	private static void validateNumCommand(String[] args) {
        if (args.length == 0) {
            exitWithError("Please enter a command.");
        }
	}

	private static void validateGitlet() {
        if (GITLET_FOLDER.exists()) {
            exitWithError("A Gitlet version-control system"
               + "already exists in the current directory.");
        }
	}

	private static void validateCommand() {
	    exitWithError("No command with that name exists.");
	}

	private static void validateCommitMessage() {
	    exitWithError("Please enter a commit message.");
	}

    public static void validateNumArgs(String[] args) {
        int n = args.length;
        boolean isValid = false;
        switch (args[0]) {
            case "init":
                if (n == 1) isValid = true;
            case "add":
                if (n == 2) isValid = true;
            case "commit":
                if (n == 1) validateCommitMessage();
                if (n == 2) isValid = true;
            case "log":
                if (n == 1) isValid = true;
            }
        if (!isValid) {
            exitWithError("Incorrect operands.");
        }
        return;
    }

	/** set up all directories and files we need*/
	private static void setupPersistence() throws IOException {
	    GITLET_FOLDER.mkdir();
		REFS_FOLDER.mkdir();
		HEADS_REFS_FOLDER.mkdir();
		STAGING_FOLDER.mkdir();
		trackedFiles.createNewFile();
		LOGS_FOLDER.mkdir();
		OBJECTS_FOLDER.mkdir();
		Commits.mkdir();
		Blobs.mkdir();
		createInitialCommit();
	}

   public static void createInitialCommit() throws IOException {
		String initPrevSha1 = "0000000000000000000000000000000000000000";
		Commit initialCommit = new Commit("initial commit", initPrevSha1, true);
		initialCommit.saveCommit();
		saveBranchHead("master", initialCommit.SHA);
		saveLog(initialCommit);
   }

	/**
	* Lazy loading and caching: Let’s say you store the state of which
	* files have been gitlet added to your repo in your filesytem.
	* Lazy loading: The first time you want that list of files when
	* you run your Java program, you need to load it from disk.
	* Caching: The second time you need that list of files in the
	* same run of the Java program, don’t load it from disk again,
	* but use the same list as you loaded before. If you need to,
	* you can then add multiple files to that list object in your
	* Java program. Writing back: When you Java program is finished,
	* at the very end, since you had loaded that list of files and
	* may have modified it, write it back to your file system.
	*/
	private static void add(String[] args) throws IOException {
	    // To-do: lazy loading and caching
	    validateNumArgs(args);
		String fileName = args[1];
		Blob blob = new Blob(fileName);
        stageFile(fileName, blob);
		blob.save();
	}

	private static void stageFile(String fileName, Blob blob) {
	    Staging staging = new Staging();
        staging.add(fileName, blob.getBlobSHA1());
        staging.save(staging);
        staging.print();
	}

	private static void commit(String[] args) throws IOException {
	    validateNumArgs(args);
		String commitMessage = args[1];
		File branchFile = Utils.join(HEADS_REFS_FOLDER, "master");
		Branch parentCommit = Utils.readObject(branchFile, Branch.class);
		Commit commit = new Commit(commitMessage, parentCommit.getHead(), false);
		commit.saveCommit();
		saveBranchHead("master", commit.SHA);
		saveLog(commit);
	}

	private static void saveLog(Commit commit) throws IOException {
		File currLogFile = Utils.join(LOGS_FOLDER, "master");
		if (!currLogFile.exists()) {
			currLogFile.createNewFile();
		}
		String divider = new String("===\n");
		String SHA = new String("commit " + commit.SHA + "\n");
		String time = new String("Date: " + commit.timestamp + "\n");
		String message = new String(commit.message + "\n");

		byte[] Log = Utils.readContents(currLogFile);
		Utils.writeContents(currLogFile, divider, SHA, time, message, "\n", Log);
	}

	private static void printAllLog(String[] args) throws IOException {
	    validateNumArgs(args);
		File currLogFile = Utils.join(LOGS_FOLDER, "master");
		String fullLog = Utils.readContentsAsString(currLogFile);
		System.out.println(fullLog);
	}

	public static void saveBranchHead(String branchName, String SHA1) {
		Branch branch = new Branch("master", SHA1);
		File branchFile = Utils.join(HEADS_REFS_FOLDER, branchName);
		Utils.writeObject(branchFile, branch);
	}

	public static void exitWithError(String message) {
		System.out.println(message);
		System.exit(0);
	}
}
