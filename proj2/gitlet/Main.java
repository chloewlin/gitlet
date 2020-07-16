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
           if (GITLET_FOLDER.exists()) {
                exitWithError("A Gitlet version-control system"
                    + "already exists in the current directory.");
           }
           if (args.length == 0) {
                exitWithError("Please enter a command.");
           }
           switch (args[0]) {
               case "init":
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
                   exitWithError("No command with that name exists.");
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

    // Adds a copy of the file as it currently exists to the staging area
    // remove it from the staging area if it is already there
    private static void add(String[] args) throws IOException {
        String fileName = args[1];
        Blob blob = new Blob(fileName);
        Staging stagingArea = new Staging();
//         stagingArea.add(blob);
        blob.saveBlob();
    }

    private static void commit(String[] args) throws IOException {
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
