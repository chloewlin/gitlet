package gitlet;

import java.io.File;
import java.io.IOException;

/**
 * Driver class for Gitlet, the tiny stupid version-control system.
 *
 * @author Chloe Lin, Christal Huang
 */
public class Main {

    /**
     * Current Working Directory.
     */
    static final File CWD = new File(".");
    /**
     * Gitlet directory.
     */
    static final File GITLET_FOLDER = new File(".gitlet");
    /**
     * directory for staging area and stores file/blob mapping.
     */
    static final File STAGING_FOLDER = Utils.join(GITLET_FOLDER, "staging");
    /**
     * file for storing trackedFiles and file/blob mapping.
     */
    static final File TRACKEDFILES = Utils.join(STAGING_FOLDER, "trackedFiles");
    /**
     * directory for storing commits and blobs.
     */
    static final File OBJECTS_FOLDER = Utils.join(".gitlet", "objects");
    /**
     * directory for storing commit objects.
     */
    static final File COMMITS_FOLDER = Utils.join(OBJECTS_FOLDER, "commits");
    /**
     * directory for storing blobs.
     */
    static final File BLOBS_FOLDER = Utils.join(OBJECTS_FOLDER, "blobs");
    /**
     * commit hash current head.
     */
    static final File HEAD = new File("head");
    /**
     * directory for storing branch and related commit has.
     */
    static final File BRANCHES = new File("branch");
    /**
     * directory for storing all commit logs for HEAD and branches.
     */
    static final File LOGS_FOLDER = Utils.join(GITLET_FOLDER, "logs");
    /**
     * directory for storing the most recent commit hash.
     */
    static final File REFS_FOLDER = Utils.join(GITLET_FOLDER, "refs");
    /**
     * directory for storing the HEAD pointers of each branch.
     */
    static final File HEADS_REFS_FOLDER = Utils.join(REFS_FOLDER, "heads");
    /**
     *  Object for a gitlet repository.
     */
    private static Repo repo = new Repo();

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains <COMMAND> <OPERAND>.
     */
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
            repo.log();
            break;
        default:
            validateCommand();
        }
        return;
    }

    /**
     *  Set up folders to persist data.
     */
    private static void setupPersistence() throws IOException {
        GITLET_FOLDER.mkdir();
        REFS_FOLDER.mkdir();
        HEADS_REFS_FOLDER.mkdir();
        STAGING_FOLDER.mkdir();
        TRACKEDFILES.createNewFile();
        LOGS_FOLDER.mkdir();
        OBJECTS_FOLDER.mkdir();
        COMMITS_FOLDER.mkdir();
        BLOBS_FOLDER.mkdir();
    }

    /**
     *  print the message if a user inputs a command that requires
     *  being in an initialized Gitlet working directory (i.e., one
     *  containing a .gitlet subdirectory), but is not in such a
     *  directory.
     *  @param args user's input of commands and operands
     */
    private static void validateInitialization(String[] args) {
        String[] directories = CWD.list();
        for (String d : directories) {
            System.out.println(d);
        }
        exitWithError("Not in an initialized Gitlet directory.");
    }

    /**
     *  print the message if a user doesn’t input any arguments.
     *  @param args user's input of commands and operands
     */
    private static void validateNumCommand(String[] args) {
        if (args.length == 0) {
            exitWithError("Please enter a command.");
        }
    }

    /**
     *  print the message if there is already a Gitlet version-control
     *  system in the current directory.
     */
    private static void validateGitlet() {
        if (GITLET_FOLDER.exists()) {
            exitWithError("A Gitlet version-control system"
                    + "already exists in the current directory.");
        }
    }

    /**
     *  print the message if a user inputs a command that doesn’t exist.
     */
    private static void validateCommand() {
        exitWithError("No command with that name exists.");
    }

    /**
     *  print the message if a commit has a blank message.
     */
    private static void validateCommitMessage() {
        exitWithError("Please enter a commit message.");
    }

    /**
     *  print the message if no files have been staged.
     */
    public static void validateFileToBeStaged() {
        exitWithError("No changes added to the commit.");
    }

    /**
     *  print the message if a user inputs a command with
     *  the wrong number or format of operands.
     *  @param args user's input of commands and operands
     */
    public static void validateNumArgs(String[] args) {
        int n = args.length;
        boolean isValid = false;
        switch (args[0]) {
        case "init":
            if (n == 1) {
                isValid = true;
            }
        case "add":
            if (n == 2) {
                isValid = true;
            }
        case "commit":
            if (n == 1) {
                validateCommitMessage();
            } else if (n == 2) {
                isValid = true;
            }
        case "log":
            if (n == 1) {
                isValid = true;
            }
        default:
            if (n == 2) {
                isValid = true;
            }
        }
        if (!isValid) {
            exitWithError("Incorrect operands.");
        }
        return;
    }

    /**
     *  print error message and abort.
     *  @param message error message to be printed
     */
    public static void exitWithError(String message) {
        System.out.println(message);
        System.exit(0);
    }
}
