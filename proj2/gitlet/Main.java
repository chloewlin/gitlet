package gitlet;

import java.io.File;
import java.io.IOException;


/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Chloe Lin, Christal Huang
 */
public class Main {
    /** Current Working Directory. */
    static final File CWD = new File(".");

    /** directory for .gitlet */
    static final File GITLET_FOLDER = new File(".gitlet");

    /** commit hash current head*/
    static final File HEAD = new File(".head");

    /** directory for storing commit objects as blobs*/
    static final File Objects = new File(".object");

    /** directory for storing branch and related commit has*/
    static final File Branches = new File(".branch");

    /** directory for storing all commit logs for HEAD and branches*/
    static final File Logs = new File(".log");

    /** directory for storing the most recent commit hash*/
    static final File Refs = new File(".ref");

    /** file that represents the staging area and stores file/blob
     Mapping*/
    static final File index = new File(".index");


     /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */

    public static void main(String... args) {
        // FILL THIS IN
    }

    /** set up all directories and files we need*/
    private static void setupPersistence() {

    }

}
