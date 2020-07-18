package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A Repo class represents a gitlet repository.
 *
 * @author Chloe Lin, Christal Huang
 */
public class Repo {

    static final String INIT_PARENT_SHA1 = "0000000000000000000000000000000000000000";

    /**
     * Create initial commit and set up branch and HEAD pointer.
     */
    public void createInitialCommit() throws IOException {
        Commit initialCommit = new Commit("initial commit",
                INIT_PARENT_SHA1, true, new HashMap<>());
        initialCommit.saveInit();
        setHEAD("master", initialCommit);
    }

    /**
     * Lazy loading and caching: Let’s say you store the state of which
     * files have been gitlet added to your repo in your filesytem.
     * Lazy loading: The first time you want that list of files when
     * you run your Java program, you need to load it from disk.
     * Caching: The second time you need that list of files in the
     * same run of the Java program, don’t load it from disk again,
     * but use the same list as you loaded before. If you need to,
     * you can then add multiple files to that list object in your
     * Java program. Writing back: When you Java program is finished,
     * at the very end, since you had loaded that list of files and
     * may have modified it, write it back to your file system.
     */
    public void add(String[] args) throws IOException {
        /** To-do: lazy loading and caching */
        Main.validateNumArgs(args);
        String fileName = args[1];
        Blob blob = new Blob(fileName);
        stage(fileName, blob);
        blob.save();
    }

    /**
     * Add a file to the staging area.
     */
    private void stage(String fileName, Blob blob) {
        Staging staging = new Staging();
        if (!isSameVersion(blob)) {
            System.out.println("staging file....");
            staging.add(fileName, blob.getBlobSHA1());
            staging.save(staging);
            staging.print();
        } else {
            System.out.println("unstaging file....");
            /** TO-DO: Unstage file... */

            Main.validateFileToBeStaged();
        }
    }

    /**
     * Remove a file from the staging area (hashmap). Unstage the file
     * if it is currently staged for addition. If the file is tracked in
     * the current commit, stage it for removal and remove the file
     * from the working directory if the user has not already done so
     * (do not remove it unless it is tracked in the current commit).
     */
    public void unstage() {

    }

    /**
     * If the current working version of the file is identical
     * to the version in the current commit, do not stage it to
     * be added, and remove it from the staging area if it is
     * already there (as can happen when a file is changed,
     * added, and then changed back).
     * */
    public boolean isSameVersion(Blob blob) {
        Commit head = getHEAD();
        System.out.println("hasBlob - current blob SHA1: " + blob.getBlobSHA1());
        System.out.println("head commit message: " + head.getMessage());
        System.out.println("head commit SHA: " + head.getSHA());
        System.out.println("head commit MAP: " + head.getSnapshot());
        Map<String, String> lastSnapshot = head.getSnapshot();
        System.out.println("hasBlobInLastCommit? "
                + lastSnapshot.containsValue(blob.getBlobSHA1()));
        return lastSnapshot.containsValue(blob.getBlobSHA1());
    }

    /**
     *  serialize added files into blobs, write blobs
     *  into files inside /object directory, add the
     *  file-blob mapping to index(staging), update
     *  HEAD pointer
     */
    public void commit(String[] args) throws IOException {
        Main.validateNumArgs(args);
        String commitMessage = args[1];

        Commit HEAD = getHEAD();
        String parent = HEAD.getSHA();
        Staging stage = Staging.load();
        Commit commit = new Commit(commitMessage, parent, false, stage.getTrackedFiles());
        System.out.println("saving staged map into commit....");
        System.out.println("print parent commit: " + parent);
        System.out.println("print self commit: " + commit.getSHA());
        stage.getTrackedFiles().forEach((k, v) ->
                System.out.println("copy map from staging to "
                + "commit...." + k + " : " + v));
        System.out.println("confirming if commit object is complete....");
        System.out.println("commit message: " + commit.getMessage());
        System.out.println("commit SHA: " + commit.getSHA());
        System.out.println("commit map: " + commit.getSnapshot());
        commit.save();
        setHEAD("master", commit);
        stage.clear();
    }

    /**
     * Update the HEAD pointer of a branch by writing the last
     * commit node into a byte array.
     */
    public void setHEAD(String branchName, Commit commit) {
        Branch branch = new Branch("master", commit);
        System.out.println("CURRENT HEAD ====> " + commit.getSHA());
        System.out.println("CURRENT HEAD PARENT ====> " + commit.getFirstParentSHA1());
        File branchFile = Utils.join(Main.HEADS_REFS_FOLDER, branchName);
        Utils.writeObject(branchFile, branch);
    }

    /**
     * Print the history of a commit tree.
     * Starting at the current head commit, display information about
     * each commit backwards along the commit tree until the initial commit.
     */
    public static void log() {
        Commit commit = getHEAD();

        while (!commit.getFirstParentSHA1().equals(INIT_PARENT_SHA1)) {
            System.out.print("===" + "\n");
            System.out.print("commit " + commit.getSHA() + "\n");
            System.out.print("Date: " + commit.getTimestamp() + "\n");
            System.out.print(commit.getMessage() + "\n");
            System.out.println("");
            commit = commit.getParent();
        }
    }

    /**
     * Return the commit node that the HEAD reference points to.
     */
    public static Commit getHEAD() {
        File master = Utils.join(Main.HEADS_REFS_FOLDER, "master");
        return Branch.load(master).getHead();
    }

    /**
     * Print print all of the commit metadata.
     */
    public void globalLog() {
        /** To-do: traverse the entire commit tree */
    }

    /**
     * Search for commits that have the given commit message.
     */
    public void find(String commitId) {
        /** To-do: search and traverse the entire commit tree */
    }

    /**
     * prints the status (branch, staged/removed files) of
     * our CWD/commit tree.
     */
    public void status() {
        /** To-do: create helper functions for each state */
        System.out.println("=== Branches ===");
        System.out.println("=== Staged Files ===");
        System.out.println("=== Removed Files ===");
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println("=== Untracked Files ===");
    }

    /**
     * Takes the version of the file as it exists in the head commit,
     * the front of the current branch, and puts it in the working
     * directory, overwriting the version of the file that’s already
     * there if there is one. The new version of the file is not staged.
     * @return
     */
    public boolean checkoutFile(String filename) throws IOException {
        Map<String, String> snapshot = getHEAD().getSnapshot();

        if (snapshot.containsKey(filename)) {
            String blobSHA1 = snapshot.get(filename);
            File blobFile = Utils.join(Main.BLOBS_FOLDER, blobSHA1);
            Blob blob = Blob.load(blobFile);
            restoreFileInCWD(blob);
        }
        return false;
    }

    /**
     * Takes the version of the file as it exists in the commit with the given id,
     * and puts it in the working directory, overwriting the version of the file
     * that’s already there if there is one.
     * The new version of the file is not staged.
     * @return
     */
    public void checkoutCommit(String commitId, String fileName) throws IOException {
        Commit commit = getHEAD();
        String blobSHA1 = "";

        while (!checkoutID(commitId)) {
            if (commit.getSHA().equals(commitId)) {
                blobSHA1 = commit.getSnapshot().get(fileName);
                break;
            }
            commit = commit.getParent();
        }
        File blobFile = Utils.join(Main.BLOBS_FOLDER, blobSHA1);
        Blob blob = Blob.load(blobFile);
        restoreFileInCWD(blob);
    }

    /**
     * Takes the version of the file as it exists in the commit with the given id,
     * and puts it in the working directory, overwriting the version of the file
     * that’s already there if there is one.
     * The new version of the file is not staged.
     * @param commitId
     * @return
     * @throws IOException
     */
    public boolean checkoutID(String commitId) throws IOException {
        Map<String, String> snapshot = getHEAD().getSnapshot();

        if (snapshot.containsKey(commitId)) {
            String blobSHA1 = snapshot.get(commitId);
            File blobFile = Utils.join(Main.BLOBS_FOLDER, blobSHA1);
            Blob blob = Blob.load(blobFile);
            restoreFileInCWD(blob);
        }
        return false;
    }


    /**
     * Restore file from blob, put it in current working directory,
     * and overwriting the version of the file that’s already
     * there if there is one.
     */
    public void restoreFileInCWD(Blob blob) throws IOException {
        String CWD = System.getProperty("user.dir");
        File file = new File(CWD, blob.getFileName());
        file.createNewFile();
        Utils.writeContents(file, blob.getFileContent());
    }

    /**
     * TBD.
     */
    public void checkoutBranch(String branchName) {

    }

    /**
     * Create a new reference for current commit node.
     */
    public void branch(String branchName) {

    }

    /**
     * Remove the branch reference.
     */
    public void rmBranch(String branchName) {

    }

    /**
     * TBD.
     */
    public void reset(String commitId) {

    }

    /**
     * TBD.
     */
    public void merge(String branchName) {

    }

    /**
     * TBD.
     */
    public void rebase(String branchName) {

    }
}
