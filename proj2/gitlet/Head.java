package gitlet;

import java.io.File;

public class Head {
    /**
     * Set up the global HEAD, default to master
     */
    public void setGlobalHEAD(String branchName, Commit commit) {
        Branch branch = new Branch(branchName, commit);
        File branchFile = Utils.join(Main.GITLET_FOLDER, "HEAD");
        System.out.println("======================== Currently on: " + branchName);
        Utils.writeObject(branchFile, branch);
    }

    /**
     * Return the commit node that the global HEAD pointer points to.
     */
    public static Commit getGlobalHEAD() {
        File HEAD = Utils.join(Main.GITLET_FOLDER, "HEAD");
//        System.out.println("Global HEAD ====> " + Branch.load(HEAD).getHEAD().getSHA());
//        System.out.println("Global HEAD PARENT ====> " + Branch.load(HEAD).getHEAD().getFirstParentSHA1());
        return Branch.load(HEAD).getHEAD();
    }

    /**
     * Update the HEAD pointer of a branch by writing the last
     * commit node into a byte array.
     */
    public void setBranchHEAD(String branchName, Commit commit) {
        Branch branch = new Branch(branchName, commit);
        System.out.println("Current branch ====> " + branchName);
        System.out.println("Branch HEAD ====> " + commit.getSHA());
        System.out.println("Branch HEAD PARENT ====> " + commit.getFirstParentSHA1());
        File branchFile = Utils.join(Main.HEADS_REFS_FOLDER, branchName);
        Utils.writeObject(branchFile, branch);
    }

    /**
     * Return the commit node that the branch HEAD pointer points to.
     */
    public static Commit getBranchHEAD(String branchName) {
        File branch = Utils.join(Main.HEADS_REFS_FOLDER, branchName);
        System.out.println("Getting branch head... ====> " + branchName);
        System.out.println("Branch HEAD ====> " + Branch.load(branch).getHEAD().getSHA());
        System.out.println("Branch HEAD PARENT ====> " + Branch.load(branch).getHEAD().getFirstParentSHA1());
        return Branch.load(branch).getHEAD();
    }
}
