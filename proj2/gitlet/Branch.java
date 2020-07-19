package gitlet;


import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * A Branch class represents a branch object
 * that stores the current branch name and the
 * reference/pointer to its corresponding commit
 * node.
 *
 * @author Chloe Lin, Christal Huang
 */

public class Branch implements Serializable {

    /**
     * Name of the branch.
     */
    private String name;
    /**
     * The last commit node of the current branch.
     */
    private Commit head;

    /**
     * Instantiate a branch.
     * @param branchName the name of saved branch.
     * @param commit the last commit node of the branch.
     */
    public Branch(String branchName, Commit commit) {
        this.name = branchName;
        this.head = commit;
    }

    /**
     * Return the name of the current branch.
     */
    public String getName() {
        return this.name;
    }
  
    /**
     * Return the last commit node of the current branch.
     */
    public Commit getHead() {
        return this.head;
    }

    /**
     * Return the SHA1 of last commit node of the current branch.
     */
    public String getHeadSHA() {
        return this.head.getSHA();
    }

    /**
     * Return a branch object from the byte array.
     * @param branch the byte array file of a branch object.
     */
    public static Branch load(File branch) {
        return Utils.readObject(branch, Branch.class);
    }
}
