package gitlet;


import java.io.File;
import java.io.IOException;
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
    public Commit getHEAD() {
        return this.head;
    }

    /**
     * Return the SHA1 of last commit node of the current branch.
     */
    public String getHeadSHA() {
        return this.head.getSHA();
    }

    /**
     * Create a new Branch reference as a file.
     */
    public void create() throws IOException {
        Branch branch = new Branch(this.name, this.head);
        File file = Utils.join(Main.HEADS_REFS_FOLDER, this.name);
        file.createNewFile();
        save(file, branch);
    }

    /**
     * Return a branch object from the byte array.
     * @param branch the byte array file of a branch object.
     */
    public static Branch load(File branch) {
        return Utils.readObject(branch, Branch.class);
    }

    /**
     * Save a Branch object as a file.
     * @param file the file
     * @param branch the branch
     */
    public void save(File file, Branch branch) {
        Utils.writeObject(file, branch);
    }

    /**
     * check if has this Branch
     * @param name the name
     */
    public static Boolean hasBranch(String name) {
        List<String> branches = Utils.plainFilenamesIn(Main.HEADS_REFS_FOLDER);
        return branches.contains(name);
    }
}
