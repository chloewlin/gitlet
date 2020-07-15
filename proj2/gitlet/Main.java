package gitlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Chloe Lin, Christal Huang
 */
public class Main {
    /** repo*/
    static Repo repo = null;

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

     /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */

    public static void main(String... args) {
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
               default:
                   exitWithError("No command with that name exists.");
               }
               return;
    }

    private static void initialize() {
        Repo repo = new Repo();
    }

    //Adds a copy of the file as it currently exists to the staging area
    //remove it from the staging area if it is already there
    private static void add(String[] args) {
        File newFile = new File(args[1]);
        byte[] blob = Utils.serialize(args[1]);
        Utils.writeContents(newFile, blob);
        File blobFile = Utils.join(BLOBS_FOLDER, );
        try {
//            blobFile.createNewFile();
        } catch (IOException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
        Utils.writeObject(blobFile, branch);
    }

    /** set up all directories and files we need*/
    private static void setupPersistence() {
        GITLET_FOLDER.mkdir();
        REFS_FOLDER.mkdir();
        HEADS_REFS_FOLDER.mkdir();
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
