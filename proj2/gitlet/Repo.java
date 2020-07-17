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

    /**
     * Create initial commit and set up branch and HEAD pointer.
     */
    public void createInitialCommit() throws IOException {
        String initPrevSha1 = "0000000000000000000000000000000000000000";
        Commit initialCommit = new Commit("initial commit",
                initPrevSha1, true, new HashMap<>());
        initialCommit.saveInit();
        updateHead("master", initialCommit);
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
        stageFile(fileName, blob);
        blob.save();
    }

    /**
     * Add a file to the staging area.
     */
    private void stageFile(String fileName, Blob blob) {
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
     * Remove a file from the staging area (hashmap).
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
        File master = Utils.join(Main.HEADS_REFS_FOLDER, "master");
        Commit head = Branch.load(master).getHead();
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
        File branchFile = Utils.join(Main.HEADS_REFS_FOLDER, "master");
        Branch parentCommit = Utils.readObject(branchFile, Branch.class);

        Staging stage = Staging.load();
        Commit commit = new Commit(commitMessage, parentCommit.
                getHeadSHA(), false, stage.getTrackedFiles());
        System.out.println("saving staged map into commit....");
        stage.getTrackedFiles().forEach((k, v) ->
                System.out.println("copy map from staging to "
                + "commit...." + k + " : " + v));
        System.out.println("confirming if commit object is complete....");
        System.out.println("commit message: " + commit.getMessage());
        System.out.println("commit SHA: " + commit.getSHA());
        System.out.println("commit map: " + commit.getSnapshot());

        commit.save();
        updateHead("master", commit);
        stage.clear();
    }

    /**
     * Update the HEAD pointer of a branch by writing the last
     * commit node into a byte array.
     */
    public void updateHead(String branchName, Commit commit) {
        Branch branch = new Branch("master", commit);
        File branchFile = Utils.join(Main.HEADS_REFS_FOLDER, branchName);
        Utils.writeObject(branchFile, branch);
    }

    /**
     * TO BE FIXED.
     */
    private void saveLog(Commit commit) throws IOException {
        File currLogFile = Utils.join(Main.LOGS_FOLDER, "master");
        if (!currLogFile.exists()) {
            currLogFile.createNewFile();
        }
        String divider = new String("===" + "\n");
        String sha1 = new String("commit " + commit.getSHA() + "\n");
        String time = new String("Date: " + commit.getTimestamp() + "\n");
        String message = new String(commit.getMessage() + "\n");

        byte[] log = Utils.readContents(currLogFile);
        Utils.writeContents(currLogFile, log, divider, sha1, time, message);
    }

    /**
     * TO BE FIXED.
     */
    public static void printAllLog(String[] args) throws IOException {
        Main.validateNumArgs(args);
        File currLogFile = Utils.join(Main.LOGS_FOLDER, "master");
        String fullLog = Utils.readContentsAsString(currLogFile);
        System.out.println(fullLog);
    }

    /**
     * Print the commit metadata for current branch.
     */
    public void log() {

    }

    /**
     * Print print all of the commit metadata.
     */
    public void globalLog() {

    }

    /**
     * Search for commits that have the given commit message.
     */
    public void find(String commitId) {

    }

    /**
     * prints the status (branch, staged/removed files) of
     * our CWD/commit tree.
     */
    public void status() {

    }

    /**
     * TBD.
     */
    public void checkout(File file) {

    }

    /**
     * TBD.
     */
    public void checkout(String commitId, File file) {

    }

    /**
     * TBD.
     */
    public void checkout(String branchName) {

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
