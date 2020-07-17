package gitlet;

import java.io.File;
import java.io.IOException;

public class Repo {

    // create initial commit and set up branch and HEAD pointer
    public void createInitialCommit() throws IOException {
        String initPrevSha1 = "0000000000000000000000000000000000000000";
        Commit initialCommit = new Commit("initial commit", initPrevSha1, true);
        initialCommit.save();
        updateHead("master", initialCommit.SHA);
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
        // To-do: lazy loading and caching
        Main.validateNumArgs(args);
        String fileName = args[1];
        Blob blob = new Blob(fileName);
        stageFile(fileName, blob);
        blob.save();
    }

    private void stageFile(String fileName, Blob blob) {
        Staging staging = new Staging();
        staging.add(fileName, blob.getBlobSHA1());
        staging.save(staging);
        staging.print();
    }

    /**
     *  serialize added files into blobs, write blobs into files inside /object directory,
     *  add the file-blob mapping to index(staging), update HEAD pointer
     */
    public void commit(String[] args) throws IOException {
        Main.validateNumArgs(args);
        String commitMessage = args[1];
        File branchFile = Utils.join(Main.HEADS_REFS_FOLDER, "master");
        Branch parentCommit = Utils.readObject(branchFile, Branch.class);
        Commit commit = new Commit(commitMessage, parentCommit.getHead(), false);
        commit.save();
        updateHead("master", commit.SHA);
    }

    public void updateHead(String branchName, String SHA1) {
        Branch branch = new Branch("master", SHA1);
        File branchFile = Utils.join(Main.HEADS_REFS_FOLDER, branchName);
        Utils.writeObject(branchFile, branch);
    }

    private void saveLog(Commit commit) throws IOException {
        File currLogFile = Utils.join(Main.LOGS_FOLDER, "master");
        if (!currLogFile.exists()) {
            currLogFile.createNewFile();
        }
        String divider = new String("===" + "\n");
        String SHA = new String("commit " + commit.SHA + "\n");
        String time = new String("Date: " + commit.timestamp + "\n");
        String message = new String(commit.message + "\n");

        byte[] Log = Utils.readContents(currLogFile);
        Utils.writeContents(currLogFile, Log, divider, SHA, time, message);
    }

    public static void printAllLog(String[] args) throws IOException {
        Main.validateNumArgs(args);
        File currLogFile = Utils.join(Main.LOGS_FOLDER, "master");
        String fullLog = Utils.readContentsAsString(currLogFile);
        System.out.println(fullLog);
    }

    // remove the added file from the hashmap in index(staging)
    public void remove() {
    }


    // print the commit metadata for current branch
    public void log() {

    }

    // print all of the commit metadata
    public void globalLog() {

    }

    // search for commits that have the given commit message
    public void find(String commitId) {

    }

    // prints the status (branch, staged/removed files) of our CWD/commit tree
    public void status() {

    }

    public void checkout(File file) {

    }

    public void checkout(String commitId, File file) {

    }

    public void checkout(String branchName) {

    }

    // create a new reference for current commit node
    public void branch(String branchName) {

    }

    // remove the branch reference
    public void rmBranch(String branchName) {

    }

    public void reset(String commitId) {

    }

    public void merge(String branchName) {

    }

    public void rebase(String branchName) {

    }
}
