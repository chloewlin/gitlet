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

    static Repo repo = new Repo();

	/** Usage: java gitlet.Main ARGS, where ARGS contains
	*  <COMMAND> <OPERAND> .... */
	public static void main(String... args) throws IOException {
        validateNumCommand(args);
        switch (args[0]) {
            case "init":
                validateGitlet();
                setupPersistence();
                repo.createInitialCommit();
                break;
            case "add":
                repo.add(args);
                break;
            case "commit":
                repo.commit(args);
                break;
            case "log":
                repo.printAllLog(args);
                break;
            default:
                validateCommand();
            }
        return;
	}

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

	public static void validateFileToBeStaged() { exitWithError("No changes added to the commit."); }

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

	public static void exitWithError(String message) {
		System.out.println(message);
		System.exit(0);
	}
}
