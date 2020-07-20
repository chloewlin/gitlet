package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * A Repo class represents a gitlet repository.
 *
 * @author Chloe Lin, Christal Huang
 */
public class Repo {

    static final String INIT_PARENT_SHA1 = "0000000000000000000000000000000000000000";
    static Staging stagingArea = new Staging();
    Head head = new Head();

    /**
     * Create initial commit and set up branch and HEAD pointer.
     */
    public void initialize() throws IOException {
        Commit sentinel = new Commit("sentinel");
        Commit initialCommit = new Commit("initial commit", sentinel.getSHA(), true, new HashMap<>());
        sentinel.save();
        initialCommit.saveInit();

        // Update both global and branch heads
        this.head.setGlobalHEAD("master", initialCommit);
        this.head.setBranchHEAD("master", initialCommit);
//        setHEAD("master", initialCommit);
//        setupGlobalHead("master", initialCommit);

        /** initialize + save initial stage */
        this.stagingArea.save();
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
        blob.save();
        stage(fileName, blob);
    }

    /**
     * Add a file to the staging area.
     * If the current working version of the file is identical
     * to the version in the current commit, do not stage it to
     * be added, and remove it from the staging area if it is
     * already there (as can happen when a file is changed,
     * added, and then changed back).
     * */
    private void stage(String fileName, Blob blob) throws IOException {
        this.stagingArea = this.stagingArea.load();

        if (!isSameVersion(fileName)) {
            this.stagingArea.add(fileName, blob.getBlobSHA1());
            this.stagingArea.save();
            this.stagingArea.printTrackedFiles();
        } else {
            if (this.stagingArea.containsFile(fileName)) {
                this.stagingArea.remove(fileName);
            }
            Main.validateFileToBeStaged();
        }
    }

    /**
     * Checks if the current working version of the file is identical
     * to the version in the current commit.
     */
    public boolean isSameVersion(String currFileName) {
        String CWD = System.getProperty("user.dir");
        File currentFile = new File(CWD, currFileName);
        Commit currCommit = this.head.getGlobalHEAD();
        String blobSHA1 = currCommit.getSnapshot().get(currFileName);
        if (blobSHA1 == null) {
            return false;
        }
        File blobFile = Utils.join(Main.BLOBS_FOLDER, blobSHA1);
        Blob blob = Blob.load(blobFile);
        return hasSameContent(currentFile, blob);
    }

    /**
     * Compares the byte array of the file in CWD and the byte array
     * saved in the last commit/blob.
     * @param currVersion file in CWD
     * @blob blob the blob of the same file saved in current commit
     * */
    public boolean hasSameContent(File currVersion, Blob blob) {
        byte[] versionInCurrCommit = blob.getFileContent();
        byte[] versionInCWD = Utils.readContents(currVersion);
        return Arrays.equals(versionInCurrCommit, versionInCWD);
    }

    /**
     *  serialize added files into blobs, write blobs
     *  into files inside /object directory, add the
     *  file-blob mapping to index(staging), update
     *  HEAD pointer
     */
    public void commit(String[] args) throws IOException {
        Main.validateNumArgs(args);
        String message = args[1];
        Commit parent = this.head.getGlobalHEAD();
        String parentSHA1 = parent.getSHA();

        Staging stage = this.stagingArea.load();
        Map<String, String> snapshot = updateSnapshot();

        Commit commit = new Commit(message, parentSHA1, false, snapshot);
        System.out.println("confirming if commit object is complete....");
        System.out.println("commit SHA: " + commit.getSHA());
        commit.save();

        Branch currBranch = Utils
                .readObject((Utils.join(Main.GITLET_FOLDER, "HEAD")), Branch.class);

        // Update both global and branch heads
        head.setGlobalHEAD(currBranch.getName(), commit);
        head.setBranchHEAD(currBranch.getName(), commit);

        stage.clear();
    }

    /**
     *  By default a commit is the same as its parent. Files staged
     *  for addition and removal are the updates to the commit.
     */
    public Map<String, String> updateSnapshot() {
        // here
        Commit HEAD = this.head.getGlobalHEAD();
        Staging stage = this.stagingArea.load();
        Map<String, String> parentSnapshot = HEAD.getSnapshot();
        Map<String, String> stagedForAdditionFiles = stage.getFilesStagedForAddition();
        stagedForAdditionFiles.forEach((file, SHA1) -> parentSnapshot.put(file,SHA1));
        return parentSnapshot;
    }

    /**
     * Remove a file from the staging area (hashmap). Unstage the file
     * if it is currently staged for addition. If the file is tracked in
     * the current commit, stage it for removal and remove the file
     * from the working directory if the user has not already done so
     * (do not remove it unless it is tracked in the current commit).
     */
    public void remove(String[] args) {
        String fileName = args[1];
        stagingArea = stagingArea.load();

        if (stagingArea.containsFile(fileName)) {
            stagingArea.remove(fileName);
            stagingArea.unstage(fileName);
        } else if (trackedByCurrCommit(fileName)) {
            stagingArea.unstage(fileName);
            String CWD = System.getProperty("user.dir");
            File file = new File(CWD, fileName);
            Utils.restrictedDelete(file);
        } else {
            Main.exitWithError("No reason to remove the file.");
        }

        stagingArea.save();
    }

    /**
     * Check if a file is tracked by current commit (HEAD)
     * */
    public boolean trackedByCurrCommit(String fileName) {
        // here
        Commit HEAD = this.head.getGlobalHEAD();
        return HEAD.getSnapshot().containsKey(fileName);
    }

    /**
     * Print the history of a commit tree.
     * Starting at the current head commit, display information about
     * each commit backwards along the commit tree until the initial commit.
     */
    public void log() {
        // here
        Commit commit = this.head.getGlobalHEAD();

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
        Status.getGlobalStatus();
    }

    /**
     * Takes the version of the file as it exists in the head commit,
     * the front of the current branch, and puts it in the working
     * directory, overwriting the version of the file that’s already
     * there if there is one. The new version of the file is not staged.
     */
    public void checkoutFile(String filename) throws IOException {
        Map<String, String> snapshot = this.head.getGlobalHEAD().getSnapshot();

        System.out.print(snapshot);

        /**
         * To-do: checkout should use abbreviated filename.
         */
        if (snapshot.containsKey(filename)) {
            String blobSHA1 = snapshot.get(filename);
            File blobFile = Utils.join(Main.BLOBS_FOLDER, blobSHA1);
            Blob blob = Blob.load(blobFile);
            restoreFileInCWD(blob);
        }
    }

    /**
     * Takes the version of the file as it exists in the commit with the given id,
     * and puts it in the working directory, overwriting the version of the file
     * that’s already there if there is one.
     * The new version of the file is not staged.
     */
    public void checkoutCommit(String commitId, String fileName) throws IOException {
        // here
        Commit commit = this.head.getGlobalHEAD();
        String blobSHA1 = "";

        /**
         * To-do: FIX BUG
         */
        while (!commit.getFirstParentSHA1().equals(INIT_PARENT_SHA1)) {
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
     * Update the global HEAD pointer to point to branch HEAD.
     */
    public void checkoutBranch(String branchName) {
        if (!Branch.hasBranch(branchName)) {
            Main.exitWithError("No such branch exists.");
        }
        // get branch head
        Commit branchHEAD = this.head.getBranchHEAD(branchName);

        // restore all the files at that branch head
        restoreFilesAtBranch(branchHEAD.getSnapshot());

        // set the global head to branch head
        this.head.setGlobalHEAD(branchName, branchHEAD);
    }

    public void restoreFilesAtBranch(Map<String, String> snapshot) {
        snapshot.forEach((fileName, blobSHA1) -> {
            File blobFile = Utils.join(Main.BLOBS_FOLDER, blobSHA1);
            Blob blob = Blob.load(blobFile);
            try {
                restoreFileInCWD(blob);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create a new reference for current commit node.
     */
    public void branch(String[] args) throws IOException {
        String branchName = args[1];
        if (!Branch.hasBranch(branchName)) {
            // here
            Branch branch = new Branch(branchName, this.head.getGlobalHEAD());
            branch.create();
        } else {
            Main.exitWithError("A branch with that name already exists.");
        }
    }

    /**
     * Remove the branch reference.
     */
    public void rmBranch(String branchName) {

    }

    /**
     * TBD.
     *
     * checkout should use abbreviated filename.
     *
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
