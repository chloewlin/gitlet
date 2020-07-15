package gitlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Chloe Lin, Christal Huang
 */
public class Main {
    /** repo*/
    static Repo repo;

    /** Current Working Directory. */
    static final File CWD = new File(".");

    /** directory for .gitlet */
    static final File GITLET_FOLDER = new File(".gitlet");

    /** file that represents the staging area and stores file/blob
        Mapping*/
    static final File index = Utils.join(GITLET_FOLDER, "index");

    /** commit hash current head*/
    static final File HEAD = new File("head");

    /** directory for storing branch and related commit has*/
    static final File Branches = new File("branch");

    /** directory for storing all commit logs for HEAD and branches*/
    static final File Logs = new File("log");

    /** directory for storing the most recent commit hash*/
    static final File REFS_FOLDER = Utils.join(GITLET_FOLDER, "refs");

    static final File HEADS_REFS_FOLDER = Utils.join(REFS_FOLDER, "heads");

    /* directory for the staging area */
    static final File STAGING_FOLDER = Utils.join(GITLET_FOLDER, "staging");
    private static Staging stagingArea = new Staging();

     /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */

    public static void main(String... args) throws IOException {
           if (args.length == 0) {
                exitWithError("Please enter a command.");
           }
           setupPersistence();
           switch (args[0]) {
               case "init":
                   initialize();
                   break;
               case "add":
                   add(args);
                   break;
               case "commit":
                    commit(args);
                    break;
               default:
                   exitWithError("No command with that name exists.");
               }
           return;
    }

    private static void initialize() throws IOException {
        Repo Repo = new Repo();
    }

    // Adds a copy of the file as it currently exists to the staging area
    // remove it from the staging area if it is already there
    private static void add(String[] args) throws IOException {
        String fileName = args[1];
        Blob blob = new Blob(fileName);
        stagingArea.add(blob);
        blob.saveBlob();
    }

    private static void commit(String[] args) throws IOException {
        String commitMessage = args[1];
        File branchFile = Utils.join(HEADS_REFS_FOLDER, "master");
        Branch parentCommit = Utils.readObject(branchFile, Branch.class);
        //  System.out.println("parentCommit: " + parentCommit.getHead());
        Commit commit = new Commit(commitMessage, parentCommit.getHead(), false);
        commit.saveCommit();
        saveBranchHead("master", commit.SHA);
    }

    public static void saveBranchHead(String branchName, String SHA1) {
        Branch branch = new Branch("master", SHA1);
        File branchFile = Utils.join(HEADS_REFS_FOLDER, branchName);
        Utils.writeObject(branchFile, branch);
    }

    /** set up all directories and files we need*/
    private static void setupPersistence() {
        GITLET_FOLDER.mkdir();
        REFS_FOLDER.mkdir();
        HEADS_REFS_FOLDER.mkdir();
        STAGING_FOLDER.mkdir();
        try {
            index.createNewFile();
        } catch(IOException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }

    public static void exitWithError(String message) {
        System.out.println(message);
        System.exit(0);
    }
}
