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
        this.head.setGlobalHEAD("master", initialCommit);
        this.head.setBranchHEAD("master", initialCommit);
        stagingArea.save();
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
        stagingArea = stagingArea.load();

        if (!isSameVersion(fileName)) {
            stagingArea.add(fileName, blob.getBlobSHA1());
            stagingArea.save();
        } else {
            if (stagingArea.containsFile(fileName)) {
                stagingArea.remove(fileName);
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
        Commit currCommit = Head.getGlobalHEAD();
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
        Commit parent = Head.getGlobalHEAD();
        String parentSHA1 = parent.getSHA();

        Staging stage = stagingArea.load();
        Map<String, String> snapshot = updateSnapshot();

        Commit commit = new Commit(message, parentSHA1, false, snapshot);
        commit.save();

        Branch currBranch = Utils
                .readObject((Utils.join(Main.GITLET_FOLDER, "HEAD")), Branch.class);
        head.setGlobalHEAD(currBranch.getName(), commit);
        head.setBranchHEAD(currBranch.getName(), commit);
        stage.clear();
    }

    /**
     *  By default a commit is the same as its parent. Files staged
     *  for addition and removal are the updates to the commit.
     */
    public Map<String, String> updateSnapshot() {
        Commit HEAD = Head.getGlobalHEAD();
        Staging stage = stagingArea.load();
        Map<String, String> parentSnapshot = HEAD.getSnapshot();
        Map<String, String> stagedForAdditionFiles = stage.getFilesStagedForAddition();
        stagedForAdditionFiles.forEach(parentSnapshot::put);
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
        Commit HEAD = Head.getGlobalHEAD();
        return HEAD.getSnapshot().containsKey(fileName);
    }

    /**
     * Print the history of a commit tree.
     * Starting at the current head commit, display information about
     * each commit backwards along the commit tree until the initial commit.
     */
    public void log() {
        Commit commit = Head.getGlobalHEAD();

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
     * @return
     */
    public boolean checkoutFile(String filename) throws IOException {
        Map<String, String> snapshot = Head.getGlobalHEAD().getSnapshot();

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
    public boolean checkoutCommit(String commitId, String fileName) throws IOException {
        Commit commit = Head.getGlobalHEAD();
        String blobSHA1 = "";
        Boolean found = false;

        while (!commit.getFirstParentSHA1().equals(INIT_PARENT_SHA1)) {
            if (findMatchId(commit.getSHA(), commitId)) {
                blobSHA1 = commit.getSnapshot().get(fileName);
                found = true;
                break;
            }
            commit = commit.getParent();
        }

        if (!found) {
            return false;
        }

        File blobFile = Utils.join(Main.BLOBS_FOLDER, blobSHA1);
        Blob blob = Blob.load(blobFile);
        restoreFileInCWD(blob);
        return true;
    }

    /**
     * Checks if a commit contains a given file
     * */
    public boolean containsFile(String commitId, String fileName) {
        Commit commit = Head.getGlobalHEAD();
        Boolean hasFile = false;

        while (!commit.getFirstParentSHA1().equals(INIT_PARENT_SHA1)) {
            if (findMatchId(commit.getSHA(), commitId)) {
                if (commit.getSnapshot().containsKey(fileName)) {
                    hasFile = true;
                    break;
                }
            }
            commit = commit.getParent();
        }

        return hasFile;
    }

    /**
     * Checks if a given commit id, full or abbreviated, matches
     * the SHA1 id of a commit node
     * @param commitSHA1 the SHA1 id of a commit node
     * @param commitId a given commitId we want to search
     * */
    public boolean findMatchId(String commitSHA1, String commitId) {
        return commitSHA1.equals(commitId) ||
                commitSHA1.substring(0, 6).equals(commitId);
    }

    /**
     * Update the global HEAD pointer to point to branch HEAD.
     */
    public void checkoutBranch(String branchName) {
        if (!Branch.hasBranch(branchName)) {
            Main.exitWithError("No such branch exists.");
        }
        Commit branchHEAD = Head.getBranchHEAD(branchName);
        Commit currHEAD = Head.getGlobalHEAD();
        this.head.setGlobalHEAD(branchName, branchHEAD);
        restoreFilesAtBranch(currHEAD, branchHEAD);
        stagingArea.clear();
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
     * TBD: May contain bugs. Write tests.
     *
     * Compare the snapshots hashmaps of currBranch and targetBranch.
     *
     * Any files that are tracked in the current branch but are
     * not present in the checked-out branch are deleted.
     * @param currBranch the commit node at current branch
     * @param checkoutBranch the commit node at checkout branch
     */
    public void restoreFilesAtBranch(Commit currBranch, Commit checkoutBranch) {
        Map<String, String> currSnapshot = currBranch.getSnapshot();
        Map<String, String> checkoutSnapshot = checkoutBranch.getSnapshot();
        Map<String, String> overwrite = new HashMap<>();
        Map<String, String> delete = new HashMap<>();

        currSnapshot.forEach((fileName, blobSHA1) -> {
            if (checkoutSnapshot.containsKey(fileName)) {
                overwrite.put(fileName, checkoutSnapshot.get(fileName));
            } else {
                delete.put(fileName, blobSHA1);
            }
        });

        checkoutSnapshot.forEach((fileName, blobSHA1) -> {
            if (!overwrite.containsKey(fileName)) {
                overwrite.put(fileName, blobSHA1);
            }
        });

        overwrite.forEach((file, blobSHA1) -> {
            File blobFile = Utils.join(Main.BLOBS_FOLDER, blobSHA1);
            Blob blob = Blob.load(blobFile);
            try {
                restoreFileInCWD(blob);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        delete.forEach((file, blobSHA1) -> Utils.restrictedDelete(file));
    }

    /**
     * Create a new reference for current commit node.
     */
    public void branch(String[] args) throws IOException {
        String branchName = args[1];
        if (!Branch.hasBranch(branchName)) {
            Branch branch = new Branch(branchName, Head.getGlobalHEAD());
            branch.create();
        } else {
            Main.exitWithError("A branch with that name already exists.");
        }
    }

    /**
     * Remove the branch reference.
     */
    public void rmBranch(String[] args) {
        String branchNameToRemove = args[1];

        String currBranchName = currentBranchName();

        if (currBranchName.equals(branchNameToRemove)) {
            Main.exitWithError("Cannot remove the current branch.");
        }
        if (Branch.hasBranch(branchNameToRemove)) {
            File branch = Utils.join(Main.HEADS_REFS_FOLDER, branchNameToRemove);
            branch.delete();
        } else {
            Main.exitWithError("A branch with that name does not exist.");
        }
    }

    /**
     * Return the name of the current branch.
     */
    public String currentBranchName() {
        File branchFile = (Utils.join(Main.GITLET_FOLDER, "HEAD"));
        return Utils
                .readObject(branchFile, Branch.class)
                .getName();
    }

    /**
     * TBD.
     *
     * checkout should use abbreviated filename.
     *
     */
    public void reset(String[] args) {
        Commit commit = Head.getGlobalHEAD();
        String commitId = args[1];
        Commit targetCommit = null;

        // to-do: find untracked files in CWD
        if (hasUntrackedFiles()) {
            Main.exitWithError("There is an untracked file in the way;" +
                    "delete it, or add and commit it first.");
        }

        // find commit
        while (!commit.getFirstParentSHA1().equals(INIT_PARENT_SHA1)) {
            if (findMatchId(commit.getSHA(), commitId)) {
                targetCommit = commit;
            } else {
                Main.exitWithError("No commit with that id exists.");
            }
            commit = commit.getParent();
        }

        // checkout to that commit
        Commit currCommit = Head.getGlobalHEAD();
        restoreFilesAtCommit(currCommit, targetCommit);
        // reset global HEAD
        Head.setGlobalHEAD(currentBranchName(), targetCommit);
    }

    /**
     * Returns if there are untracked files in CWD.
     * */
    public boolean hasUntrackedFiles() {
        /**
         * To-do
         */
        return false;
    }

    public void restoreFilesAtCommit(Commit currCommit, Commit checkoutCommit) {
        Map<String, String> currSnapshot = currCommit.getSnapshot();
        Map<String, String> checkoutSnapshot = checkoutCommit.getSnapshot();
        Map<String, String> overwrite = new HashMap<>();
        Map<String, String> delete = new HashMap<>();

        currSnapshot.forEach((fileName, blobSHA1) -> {
            if (checkoutSnapshot.containsKey(fileName)) {
                overwrite.put(fileName, checkoutSnapshot.get(fileName));
            } else {
                delete.put(fileName, blobSHA1);
            }
        });

        checkoutSnapshot.forEach((fileName, blobSHA1) -> {
            if (!overwrite.containsKey(fileName)) {
                overwrite.put(fileName, blobSHA1);
            }
        });

        overwrite.forEach((file, blobSHA1) -> {
            File blobFile = Utils.join(Main.BLOBS_FOLDER, blobSHA1);
            Blob blob = Blob.load(blobFile);
            try {
                restoreFileInCWD(blob);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        delete.forEach((file, blobSHA1) -> Utils.restrictedDelete(file));
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
