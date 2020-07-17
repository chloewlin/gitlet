package gitlet;

import java.io.File;
import java.io.IOException;

public class Repo {
    /** directory for storing commit objects as blobs*/
    static final File OBJECTS_FOLDER = Utils.join(".gitlet", "objects");
    static final File Commits = Utils.join(OBJECTS_FOLDER,  "commits");
    static final File Blobs = Utils.join(OBJECTS_FOLDER,  "blobs");

    static final File GITLET_FOLDER = new File(".gitlet");
    static final File REFS_FOLDER = Utils.join(GITLET_FOLDER, "refs");
    static final File HEADS_REFS_FOLDER = Utils.join(REFS_FOLDER, "heads");

    /** directory for storing all commit logs for HEAD and branches*/
    static final File LOGS_FOLDER = Utils.join(GITLET_FOLDER, "logs");

    static final File CWD = new File(".");
    // create initial commit and set up branch and HEAD pointer
    public Repo() {
        OBJECTS_FOLDER.mkdir();
        Commits.mkdir();
        Blobs.mkdir();
    }

    public void createInitialCommit() throws IOException {
        String initPrevSha1 = "0000000000000000000000000000000000000000";
        Commit initialCommit = new Commit("initial commit", initPrevSha1, true);
        initialCommit.saveCommit();
        saveBranchHead("master", initialCommit.SHA);
        saveLog(initialCommit);
    }

    public void add(String[] args) throws IOException {
        // To-do: lazy loading and caching
        // Main.validateNumArgs(args);
        String fileName = args[1];
        Blob blob = new Blob(fileName);
        stageFile(fileName, blob);
        blob.save();
    }

    private static void stageFile(String fileName, Blob blob) {
        Staging staging = new Staging();
        staging.add(fileName, blob.getBlobSHA1());
        staging.save(staging);
        staging.print();
    }

    public void saveBranchHead(String branchName, String SHA1) {
        Branch branch = new Branch("master", SHA1);
        File branchFile = Utils.join(HEADS_REFS_FOLDER, branchName);
        Utils.writeObject(branchFile, branch);
    }

    private static void saveLog(Commit commit) throws IOException {
        File currLogFile = Utils.join(LOGS_FOLDER, "master");
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

    // remove the added file from the hashmap in index(staging)
    public void remove() {
    }

    // serialize added files into blobs, write blobs into files inside /object directory, add the
    // file-blob mapping to index(staging), update HEAD pointer
    public void commit() {
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
