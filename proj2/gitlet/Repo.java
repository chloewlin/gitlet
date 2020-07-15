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

    // create initial commit and set up branch and HEAD pointer
    public Repo() {
        OBJECTS_FOLDER.mkdir();
        Commits.mkdir();
        Blobs.mkdir();
        createInitialCommit();
    }

    public void createInitialCommit() {
        String initPrevSha1 = "0000000000000000000000000000000000000000";
        Commit initialCommit = new Commit("initial commit", initPrevSha1, true);
        initialCommit.saveCommit();
        saveBranchHead("master", initialCommit);
    }

    public void saveBranchHead(String branchName, Commit commit) {
        Branch branch = new Branch("master", commit);
        File branchFile = Utils.join(HEADS_REFS_FOLDER, branchName);
        try {
            branchFile.createNewFile();
        } catch (IOException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
        Utils.writeObject(branchFile, branch);
    }

    // add one file to the hashmap in index(staging)
    public void add(String fileName) {
         Blob blob = new Blob(fileName);
         System.out.println("Blob " + blob.fileName);
         System.out.println("BlobContent " + blob.fileContent);
         System.out.println("Blob SHA" + blob.fileSHA1);
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
