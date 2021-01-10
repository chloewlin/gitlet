package gitlet;

import java.io.File;

/**
 * Head Class.
 * @author Chloe Lin, Christal Huang
 */
public class Head {

    /**
     * Set up the global HEAD, default to master.
     * @param branchName the branchName
     * @param commit the commit
     */
    public static void setGlobalHEAD(String branchName, Commit commit) {
        Branch branch = new Branch(branchName, commit);
        File branchFile = Utils.join(Main.GITLET_FOLDER, "HEAD");
        Utils.writeObject(branchFile, branch);
    }

    /**
     * Return the commit node that the global HEAD pointer points to.
     */
    public static Commit getGlobalHEAD() {
        File HEAD = Utils.join(Main.GITLET_FOLDER, "HEAD");
        return Branch.load(HEAD).getHEAD();
    }

    /**
     * Update the HEAD pointer of a branch by writing the last
     * commit node into a byte array.
     * @param branchName the branchName
     * @param commit the commit
     */
    public static void setBranchHEAD(String branchName, Commit commit) {
        Branch branch = new Branch(branchName, commit);
        File branchFile = Utils.join(Main.HEADS_REFS_FOLDER, branchName);
        Utils.writeObject(branchFile, branch);
    }

    /**
     * Return the commit node that the branch HEAD pointer points to.
     * @param branchName the branchName
     */
    public static Commit getBranchHEAD(String branchName) {
        File branch = Utils.join(Main.HEADS_REFS_FOLDER, branchName);
        return Branch.load(branch).getHEAD();
    }
}
