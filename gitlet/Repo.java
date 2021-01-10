package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * A Repo class represents a gitlet repository.
 *
 * @author Chloe Lin
 */
public class Repo {

    static final String SENTINEL_COMMIT_ID = "6cf73ef132f3f89a94f4c73ec879aa79ba529e86";
    static final String INIT_PARENT_SHA1 = "0000000000000000000000000000000000000000";
    static Staging stagingArea = new Staging();
    Head head = new Head();
    Merge merge = new Merge();

    /**
     * Create initial commit and set up branch and HEAD pointer.
     */
    public void initialize() throws IOException {
        Map<String, String> sentinelMap = new HashMap<>();
        Commit sentinel = new Commit("sentinel", sentinelMap);
        Commit initialCommit = new Commit("initial commit", sentinel.getSHA(),
                true, new HashMap<>());
        sentinel.saveInit();
        initialCommit.saveInit();
        Head.setGlobalHEAD("master", initialCommit);
        Head.setBranchHEAD("master", initialCommit);
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
     *
     * Add a file to the staging area.
     * If the current working version of the file is identical
     * to the version in the current commit, do not stage it to
     * be added, and remove it from the staging area if it is
     * already there (as can happen when a file is changed,
     * added, and then changed back).
     * */
    public void add(String[] args) throws IOException {
        Main.validateNumArgs(args);
        String fileName = args[1];

        if (isSameVersionAsLastCommit(fileName)) {
            if (stagingArea.containsFileForRemoval(fileName)) {
                stagingArea.removeFromStagedForRemoval(fileName);
            }
            stagingArea.save();
            return;
        }

        Blob blob = new Blob(fileName);
        blob.save();
        stage(fileName, blob);
    }

    /**
    * Stage a file in the staging area
    */
    private void stage(String fileName, Blob blob) throws IOException {
        stagingArea = stagingArea.load();
        stagingArea.add(fileName, blob.getBlobSHA1());
        stagingArea.save();
    }

    /**
     * Checks if the current working version of the file is identical
     * to the version in the current commit.
     */
    public boolean isSameVersionAsLastCommit(String currFileName) {
        String CWD = System.getProperty("user.dir");
        File currentFile = new File(CWD, currFileName);

        Commit currCommit = Head.getGlobalHEAD();
        String blobSHA1 = currCommit.getSnapshot().get(currFileName);

        if (blobSHA1 == null) {
            return false;
        }

        File blobOfPrevVersion = Utils.join(Main.BLOBS_FOLDER, blobSHA1);

        return hasSameContent(currentFile, blobOfPrevVersion);
    }

    /**
     * Compares the byte array of the file in CWD and the byte array
     * saved in the last commit/blob.
     * @param currVersion file in CWD
     * @param blobOfPrevVersion the blob of the same file saved in current commit
     * */
    public boolean hasSameContent(File currVersion, File blobOfPrevVersion) {
        Blob blob = Blob.load(blobOfPrevVersion);
        byte[] versionInCurrCommit = blob.getFileContent();
        byte[] versionInCWD = Utils.readContents(currVersion);
        return Arrays.equals(versionInCurrCommit, versionInCWD);
    }

    /**
     *  serialize added files into blobs, write blobs
     *  into files inside /object directory, add the
     *  file-blob mapping to index(staging), update
     *  HEAD pointer
     *  @param args user's input of commands and operands
     */
    public void commit(String[] args) throws IOException {
        Main.validateNumArgs(args);
        String message = args[1];

        stagingArea = stagingArea.load();

        if (stagingArea.isEmpty()) {
            Main.exitWithError("No changes added to the commit.");
        }
        if (message.isEmpty() || message.isBlank()) {
            Main.exitWithError("Please enter a commit message.");
        }

        String currHeadSHA1 = Head.getGlobalHEAD().getSHA();

        Map<String, String> snapshot = updateSnapshot();

        Commit commit = new Commit(message, currHeadSHA1, false, snapshot);
        commit.save();

        Branch currBranch = Utils
                .readObject((Utils.join(Main.GITLET_FOLDER, "HEAD")), Branch.class);

        head.setGlobalHEAD(currBranch.getName(), commit);
        head.setBranchHEAD(currBranch.getName(), commit);

        stagingArea = new Staging();
        stagingArea.save();
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
        Set<String> stagedForRemovalFiles = stage.getFilesStagedForRemoval();

        stagedForAdditionFiles.forEach(parentSnapshot::put);

        stagedForRemovalFiles.forEach((file) -> {
            if (parentSnapshot.containsKey(file)) {
                parentSnapshot.remove(file);
            }
        });

        return parentSnapshot;
    }

    /**
     * Remove a file from the staging area (hashmap). Unstage the file
     * if it is currently staged for addition. If the file is tracked in
     * the current commit, stage it for removal and remove the file
     * from the working directory if the user has not already done so
     * (do not remove it unless it is tracked in the current commit).
     * @param args filename
     */
    public void remove(String[] args) {
        String fileName = args[1];
        stagingArea = stagingArea.load();

        if (stagingArea.containsFileForAddition(fileName)) {
            stagingArea.remove(fileName);
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
     * @param args filename
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
            if (isMergeCommit(commit)) {
                String firstParentSHA = commit.getFirstParentSHA1().substring(0, 7);
                String secondParentSHA = commit.getSecondParentSHA1().substring(0, 7);
                System.out.println("Merge: " + firstParentSHA + " " + secondParentSHA);
            }
            System.out.print("Date: " + commit.getTimestamp() + "\n");
            System.out.print(commit.getMessage() + "\n");
            System.out.println("");

            commit = commit.getParent();
        }
    }

    public boolean isMergeCommit(Commit commit) {
        return commit.getMessage().startsWith("Merged");
    }

    /**
     * Print all of the commits in this repo.
     */
    public void globalLog() {
        File commitDir = Utils.join(Main.OBJECTS_FOLDER, "commits");
        String[] commits = commitDir.list();

        for (String commitId : commits) {
            Commit commit = Commit.load(commitId);
            if (!commit.getFirstParentSHA1().equals(INIT_PARENT_SHA1)) {
                System.out.print("===" + "\n");
                System.out.print("commit " + commit.getSHA() + "\n");
                if (isMergeCommit(commit) && commit.getSecondParentSHA1() != null) {
                    String firstParentSHA = commit.getFirstParentSHA1().substring(0, 7);
                    String secondParentSHA = commit.getSecondParentSHA1().substring(0, 7);
                    System.out.println("Merge: " + firstParentSHA + " " + secondParentSHA);
                }
                System.out.print("Date: " + commit.getTimestamp() + "\n");
                System.out.print(commit.getMessage() + "\n");
                System.out.println("");
            }
        }
    }

    /**
     * Search for commits that have the given commit message.
     * @param args commit message
     */
    public void find(String[] args) {
        String commitMessage = args[1];
        File commitDir = Utils.join(Main.OBJECTS_FOLDER, "commits");
        String[] commits = commitDir.list();
        Boolean found = false;

        for (String commitId : commits) {
            Commit commit = Commit.load(commitId);
            if (commit.getMessage().equals(commitMessage)) {
                found = true;
                System.out.println(commit.getSHA());
            }
        }

        if (!found) {
            Main.exitWithError("Found no commit with that message.");
        }
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
     * @param filename filename
     */
    public void checkoutFile(String filename) throws IOException {
        Map<String, String> snapshot = Head.getGlobalHEAD().getSnapshot();

        if (snapshot.containsKey(filename)) {
            String blobSHA1 = snapshot.get(filename);
            File blobFile = Utils.join(Main.BLOBS_FOLDER, blobSHA1);
            Blob blob = Blob.load(blobFile);
            restoreFileInCWD(blob);
        } else {
            Main.exitWithError("File does not exist in that commit.");
        }
    }

    /**
     * Takes the version of the file as it exists in the commit with the given id,
     * and puts it in the working directory, overwriting the version of the file
     * that’s already there if there is one.
     * The new version of the file is not staged.
     * @param commitId the commit id
     * @param filename filename
     */
    public void checkoutCommit(String commitId, String fileName) throws IOException {
        Commit commit = Head.getGlobalHEAD();
        String blobSHA1 = "";

        while (!commit.getFirstParentSHA1().equals(INIT_PARENT_SHA1)) {
            if (findMatchId(commit.getSHA(), commitId)) {
                if (commit.getSnapshot().get(fileName) == null) {
                    Main.exitWithError("File does not exist in that commit.");
                }
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
     * check if a commit id exists in our repo 
     * @param targetCommitId the commit id to search
     */
    public boolean containsCommitId(String targetCommitId) {
        Boolean found = false;
        File commitDir = Utils.join(Main.OBJECTS_FOLDER, "commits");
        String[] commits = commitDir.list();

        for (String commitId : commits) {
            if (findMatchId(commitId, targetCommitId)) {
                found = true;
            }
        }
        return found;
    }

    /**
     * Checks if a given commit id, full or abbreviated, matches
     * the SHA1 id of a commit node
     * @param commitSHA1 the SHA1 id of a commit node
     * @param commitId a given commitId we want to search
     * */
    public boolean findMatchId(String commitSHA1, String commitId) {
        return commitSHA1.substring(0, commitId.length()).equals(commitId);
    }

    /**
     * Update the global HEAD pointer to point to branch HEAD.
     * @param branchName branch to check out to
     */
    public void checkoutBranch(String branchName) {
        String currBranchName = currentBranchName();
        Commit branchHEAD = Head.getBranchHEAD(branchName);
        Commit currHEAD = Head.getGlobalHEAD();

        if (currBranchName.equals(branchName)) {
            Main.exitWithError("No need to checkout the current branch.");
        }
        if (hasUntrackedFilesForCheckoutBranch(branchHEAD)) {
            Main.exitWithError("There is an untracked file in the way; " +
                    "delete it, or add and commit it first.");
        }

        Head.setGlobalHEAD(branchName, branchHEAD);
        restoreFilesAtBranch(currHEAD, branchHEAD);

        stagingArea = new Staging();
        stagingArea.save();
    }

    /**
     * Restore file from blob, put it in current working directory,
     * and overwriting the version of the file that’s already
     * there if there is one.
     * @param blob file blob
     */
    public void restoreFileInCWD(Blob blob) throws IOException {
        String CWD = System.getProperty("user.dir");
        File file = new File(CWD, blob.getFileName());
        file.createNewFile();
        Utils.writeContents(file, blob.getFileContent());
    }

    /**
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

        // Takes all files in the commit at the head of the
        // given branch, and puts them in the working directory,
        // overwriting the versions of the files that are already
        // there if they exist.
        currSnapshot.forEach((fileName, blobSHA1) -> {
            if (checkoutSnapshot.containsKey(fileName)) {
                overwrite.put(fileName, checkoutSnapshot.get(fileName));
            } else {
                delete.put(fileName, blobSHA1);
            }
        });

        // Any files that are tracked in the current branch but
        // are not present in the checked-out branch are deleted.
        checkoutSnapshot.forEach((fileName, blobSHA1) -> {
            if (!overwrite.containsKey(fileName)) {
                overwrite.put(fileName, blobSHA1);
            }
        });

        overwrite.forEach((file, blobSHA1) -> {
            File blobFile = Utils.join(Main.BLOBS_FOLDER, blobSHA1);
            Blob blob = Blob.load(blobFile);

            String CWD = System.getProperty("user.dir");
            File newFile = new File(CWD, file);
            try {
                newFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Utils.writeContents(newFile, blob.getFileContent());
        });

        delete.forEach((file, blobSHA1) -> {
            Utils.restrictedDelete(file);
        });
    }

    /**
     * Create a new reference for current commit node.
     * @param args branch name
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
     * @param args branch name
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
        String commitId = args[1];

        File commitDir = Utils.join(Main.OBJECTS_FOLDER, "commits");
        String[] commits = commitDir.list();

        boolean found = false;

        for (String commit_id : commits) {
            if (commitId.substring(0, commitId.length()).equals(commit_id)) {
                found = true;
            }
        }

        if (!found) {
            Main.exitWithError("No commit with that id exists.");
        }

        Commit targetCommit = null;

        for (String commitFileNames : commits) {
            if (commitId.equals(commitFileNames)) {
                targetCommit = Commit.load(commitFileNames);
            }
        }

        if (hasUntrackedFilesForReset(targetCommit)) {
            Main.exitWithError("There is an untracked file in the way;" +
                    " delete it, or add and commit it first.");
        }

        Map<String, String> checkoutSnapshot = targetCommit.getSnapshot();
        Map<String, String> currSnapshot = Head.getGlobalHEAD().getSnapshot();
        Map<String, String> restore = new HashMap<>();

        checkoutSnapshot.forEach((fileName, blobSHA1) -> {
            restore.put(fileName, blobSHA1);
        });

        currSnapshot.forEach((fileName, blobSHA1) -> {
            if (!checkoutSnapshot.containsKey(fileName)) {
                Utils.restrictedDelete(fileName);
            }
        });

        restore.forEach((file, blobSHA1) -> {
            File blobFile = Utils.join(Main.BLOBS_FOLDER, blobSHA1);
            Blob blob = Blob.load(blobFile);

            String CWD = System.getProperty("user.dir");
            File newFile = new File(CWD, file);
            try {
                newFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Utils.writeContents(newFile, blob.getFileContent());
        });

        Head.setGlobalHEAD(currentBranchName(), targetCommit);
        Head.setBranchHEAD(currentBranchName(), targetCommit);

        stagingArea = new Staging();
        stagingArea.save();
    }

    /**
     * Checks if a working file is untracked in the HEAD of current branch
     * and would be overwritten by the checkout.
     * @param givenBranchHEAD name of the checkout branch
     * */
    public boolean hasUntrackedFilesForCheckoutBranch(Commit givenBranchHEAD) {
        List<String> untrackedFiles = new ArrayList<String>();
        stagingArea = stagingArea.load();
        List<String> fileInCWD = Utils.plainFilenamesIn("./");

        for (String fileName : fileInCWD) {
            if (!fileName.equals(".DS_Store")
                && !fileName.equals(".gitignore")
                && !fileName.equals("proj2.iml")) {
                if (!Head.getGlobalHEAD().getSnapshot().containsKey(fileName)
                        && givenBranchHEAD.getSnapshot().containsKey(fileName)) {
                    untrackedFiles.add(fileName);
                }
            }
        }
        return untrackedFiles.size() > 0;
    }

    /**
     * Find untracked files for reset
     * 1. there exists some file (we’ll call it hi.txt) in the working
     * directory that is untracked in the most recent commit (a file
     * called hi.txt does not exist in the most recent commit’s snapshot)
     * AND
     * 2. The given commit (passed in as sha1) has a file called hi.txt
     *  that is tracked (it exists in that commit’s snapshot) — for checkout,
     *  this condition
     *  could be modified to be in reference to the latest commit on the
     *  given branch (rather than a given commit ID)
     *  @param givenBranchHEAD name of the checkout branch
     * */
    public boolean hasUntrackedFilesForReset(Commit givenBranchHEAD) {
        List<String> untrackedFiles = new ArrayList<String>();
        stagingArea = stagingArea.load();
        List<String> fileInCWD = Utils.plainFilenamesIn("./");

        for (String fileName : fileInCWD) {
            if (!fileName.equals(".DS_Store") && !fileName.equals(".gitignore") && !fileName.equals(
                    "proj2.iml")) {
                if (!Head.getGlobalHEAD().getSnapshot().containsKey(fileName)
                        && givenBranchHEAD.getSnapshot().containsKey(fileName)) {
                    untrackedFiles.add(fileName);
                }
            }
        }

        return untrackedFiles.size() > 0;
    }

    /**
     * Restore files saved at a commit node.
     * @param currCommit current commit node 
     * @param checkoutCommit checkout commit node
     * */
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
     * Print blob
     * @param blobSHA the SHA id of a blob
     * */
    public String printBlob(String blobSHA) {
        File blobDir = Utils.join(Main.OBJECTS_FOLDER, "blobs");
        String[] blobsFileNames = blobDir.list();
        File currBlobFile = null;

        for (String blobFileName : blobsFileNames) {
            if (blobFileName.equals(blobSHA)) {
                currBlobFile = Utils.join(blobDir, blobFileName);
            }
        }

        Blob blobObj = Blob.load(currBlobFile);

        String currContent = new String(blobObj.getFileContent(),
                StandardCharsets.UTF_8);
        return currContent;
    }

    /**
     * Merge given branch into current branch.
     * @param args name of branch to merge into
     */
    public void merge(String[] args) throws IOException {
        String givenBranch = args[1];

        merge.failureCases(givenBranch);
        merge.merge(givenBranch);
    }

    /**
     * A Merge Class for comparing commit nodes and handling merge conflicts.
     * @param branchName the name of branch to merge into
     */
    private class Merge {

        public void merge(String branchName) throws IOException {
            Commit currHEAD = Head.getGlobalHEAD();
            Commit givenHEAD = Head.getBranchHEAD(branchName);
            String originalBranchName = Utils.readObject(
                    (Utils.join(Main.GITLET_FOLDER, "HEAD")), Branch.class)
                    .getName();

            stagingArea = stagingArea.load();

            Commit SP = latestCommonAncestor(currHEAD, givenHEAD);

            Map<String, String> curr = currHEAD.getSnapshot();
            Map<String, String> given = givenHEAD.getSnapshot();
            Map<String, String> sp = SP.getSnapshot();

            Map<String, String> mergeMap = new HashMap<>();
            Map<String, String> bothDeleted = new HashMap<>();
            Map<String, String> deletedAtOne = new HashMap<>();
            boolean withConflict = false;

            // 1. If the split point is the same commit as the given branch, then we
            // do nothing; the merge is complete, and the operation ends with the message
            // Given branch is an ancestor of the current branch.
            if (branchHeadIsSP(SP, givenHEAD)) {
                System.out.println("Given branch is an ancestor of the current branch.");
                return;
            }

            // 2. If the split point is the current branch, then the effect is to check
            // out the given branch, and the operation ends after printing the message
            // Current branch fast-forwarded.
            if (currHeadIsSP(SP)) {
                checkoutBranch(branchName);
                System.out.println("Current branch fast-forwarded.");
                return;
            }

            condition3(sp, given, curr, mergeMap);
            condition4(sp, given, curr, mergeMap);
            condition5(sp, given, curr, mergeMap, bothDeleted);
            condition6(sp, curr, mergeMap);
            condition7(sp, given, mergeMap);

            boolean hasConflict = false;

            if (condition8And9(sp, given, curr, deletedAtOne, mergeMap)
                    || condition10(sp, given, curr, mergeMap)) {
                hasConflict = true;
            }

            stagingArea.save();
            commitMerge(branchName, originalBranchName);
            restoreFilesAtMerge(mergeMap, deletedAtOne, bothDeleted);

            if (hasConflict) {
                Main.exitWithError("Encountered a merge conflict.");
            }
        }

        public void restoreFilesAtMerge(Map<String, String> mergeMap,
                                        Map<String, String> deleteAtOne,
                                        Map<String, String> bothDeleted) {

            mergeMap.forEach((file, blobSHA1) -> {
                File blobFile = Utils.join(Main.BLOBS_FOLDER, blobSHA1);
                Blob blob = Blob.load(blobFile);

                String CWD = System.getProperty("user.dir");
                File newFile = new File(CWD, file);
                try {
                    newFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Utils.writeContents(newFile, blob.getFileContent());
            });


            // TODO: delete files
            deleteAtOne.forEach((file, blobSHA1) -> {
                Utils.restrictedDelete(file);
            });

            bothDeleted.forEach((file, blobSHA1) -> {
                Utils.restrictedDelete(file);
            });
        }

        // case 3:
        // givenBranch: modified && currBranch: unmodified (=SP)
        // -> copy givenBranch key-value pair, add into hashmap (stageToBeAdded), and auto staged
        public void condition3(Map<String, String> SP,
                               Map<String, String> given,
                               Map<String, String> curr,
                               Map<String, String> mergeMap) {
            HashMap<String, String> sameOnCurrAndSP = new HashMap<>();
            HashMap<String, String> diffOnGivenAndSP = new HashMap<>();

            // compare SP and Curr, find same file name with same content
            SP.forEach((spFileName, spBlob) -> {
                curr.forEach((currFileName, currBlob) -> {
                    if (spFileName.equals(currFileName) && spBlob.equals(currBlob)) {
                        sameOnCurrAndSP.put(currFileName, currBlob);
                    }
                });
            });

            // compare SP and Given, find same file name with different content
            SP.forEach((spFileName, spBlob) -> {
                given.forEach((givenFileName, givenBlob) -> {
                    if (spFileName.equals(givenFileName) && !spBlob.equals(givenBlob)) {
                        diffOnGivenAndSP.put(givenFileName, givenBlob);
                    }
                });
            });

            // find files modified on Given but not on Curr since SP
            //            {hi.txt: sdijs[dok[wq} hey **
            sameOnCurrAndSP.forEach((sameFileName, sameBlob) -> {
                diffOnGivenAndSP.forEach((diffFileName, diffBlob) -> {
                    if (sameFileName.equals(diffFileName)) {
                        mergeMap.put(diffFileName, diffBlob);
                        stagingArea.add(diffFileName, diffBlob);
                    }
                });
            });
        }

        // case 4. currBranch: modified && givenBranch: unmodified (=SP)
        // -> stay the same (continue?)
        public void condition4(Map<String, String> SP,
                               Map<String, String> given,
                               Map<String, String> curr,
                               Map<String, String> mergeMap) {

            HashMap<String, String> sameOnGivenAndSP = new HashMap<>();
            HashMap<String, String> diffOnCurrAndSP = new HashMap<>();

            // compare SP and Given, find same file name with same content
            SP.forEach((spFileName, spBlob) -> {
                given.forEach((givenFileName, givenBlob) -> {
                    if (spFileName.equals(givenFileName) && spBlob.equals(givenBlob)) {
                        sameOnGivenAndSP.put(givenFileName, givenBlob);
                    }
                });
            });


            // compare SP and Curr, find same file name with different content
            SP.forEach((spFileName, spBlob) -> {
                curr.forEach((currFileName, currBlob) -> {
                    if (spFileName.equals(currFileName) && !spBlob.equals(currBlob)) {
                        diffOnCurrAndSP.put(currFileName, currBlob);
                    }
                });
            });

            // find files modified on Curr but not on Given since SP
            sameOnGivenAndSP.forEach((sameFileName, sameBlob) -> {
                diffOnCurrAndSP.forEach((diffFileName, diffBlob) -> {
                    if (sameFileName.equals(diffFileName)) {
                        mergeMap.put(diffFileName, diffBlob);
                        stagingArea.add(diffFileName, diffBlob);
                    }
                });
            });

        }

        // 5. Modified: currBranch && givenBranch in the same way
        // both are different from SP, but Curr and given are the same
        // OR, both are deleted on Curr and given branch
        // -> pick currentBranch key-value pair, add into map
        public void condition5(Map<String, String> SP,
                               Map<String, String> given,
                               Map<String, String> curr,
                               Map<String, String> mergeMap,
                               Map<String, String> bothDeleted) {

            for (String SPFileName : SP.keySet()) {
                boolean insideCurr = curr.containsKey(SPFileName);
                boolean insideGiven = given.containsKey(SPFileName);
                String SPBlob = SP.get(SPFileName);
                String givenBlob = given.get(SPFileName);
                String currBlob = curr.get(SPFileName);

                // both are modified in the same way
                if (insideCurr && insideGiven) {
                    // check if give blob is different from SP blob
                    // check if curr blob is different from SP blob
                    // check if given blob and curr blob are the same version
                    if (!givenBlob.equals(SPBlob)
                            && !currBlob.equals(SPBlob)
                            && givenBlob.equals(currBlob)) {
                        mergeMap.put(SPFileName, currBlob);
                        stagingArea.add(SPFileName,currBlob);
                    }
                }

                // both are deleted
                if (!insideCurr && !insideGiven) {
                    bothDeleted.put(SPFileName, SPBlob);
                    stagingArea.unstage(SPFileName); // TODO: do we need to save SPBlob?
                }
            }

        }

        // 6. File not in SP && File in curr
        // -> add curr file and blob into mergeMap
        public void condition6(Map<String, String> SP,
                               Map<String, String> curr,
                               Map<String, String> mergeMap) {

            for (String currFileName : curr.keySet()) {
                if (!SP.keySet().contains(currFileName)) {
                    String currBlob = curr.get(currFileName);
                    mergeMap.put(currFileName, currBlob);
                    stagingArea.add(currFileName, currBlob);
                }
            }

        }

        // 7. File not in SP && File in given
        // -> add given file and given blob into merge map
        public void condition7(Map<String, String> SP,
                               Map<String, String> given,
                               Map<String, String> mergeMap) {

            for (String givenFileName : given.keySet()) {
                if (!SP.containsKey(givenFileName)){
                    String givenBlob = given.get(givenFileName);
                    mergeMap.put(givenFileName, givenBlob);
                    stagingArea.add(givenFileName, givenBlob);
                }
            }

        }

        // 8 & 9. File in SP && current: unmodified && given: absent (treat like modified)
        // -> removed (untracked)
        // File in SP && given: unmodified && curr: absent (treat like modified)
        // -> remain absent (unchanged?)
        public boolean condition8And9(Map<String, String> SP,
                                      Map<String, String> given,
                                      Map<String, String> curr,
                                      Map<String, String> deletedAtOne,
                                      Map<String, String> mergeMap) throws IOException {
            boolean hasConflict = false;
            for (String SPFileName : SP.keySet()) {
                boolean insideCurr = curr.containsKey(SPFileName);
                boolean insideGiven = given.containsKey(SPFileName);
                String SPBlob = SP.get(SPFileName);
                String givenBlob = given.get(SPFileName);
                String currBlob = curr.get(SPFileName);


                // 8. absent at given, curr blob is the same version as SP blob
                if (insideCurr && !insideGiven) {
                    if (currBlob.equals(SPBlob)) {
                        deletedAtOne.put(SPFileName, givenBlob);
                        stagingArea.unstage(SPFileName);
                    } else {
                        // 10.3 Conflict: File in SP && absent: given && modified: current
                        hasConflict = true;
                        createConflictFile(currBlob, givenBlob);
                        if (mergeMap.containsKey(SPFileName)) {
                            mergeMap.remove(SPFileName);
                        }
                    }

                }

                // 9. absent at curr, present at given
                if (!insideCurr && insideGiven) {
                    if (givenBlob.equals(SPBlob)) {
                        deletedAtOne.put(SPFileName, currBlob);
                        stagingArea.unstage(SPFileName);
                    } else {
                        // 10.2 Conflict File in SP && absent: current && modified: given
                        hasConflict = true;
                        createConflictFile(currBlob, givenBlob);
                        if (mergeMap.containsKey(SPFileName)) {
                            mergeMap.remove(SPFileName);
                        }
                    }
                }
                // 10.1 Conflict: SP != curr != given
                if (insideCurr && insideGiven) {
                    if (!givenBlob.equals(SPBlob)
                            && !givenBlob.equals(currBlob)
                            && !currBlob.equals(SPBlob)) {
                        hasConflict = true;
                        createConflictFile(currBlob, givenBlob);
                        if (mergeMap.containsKey(SPFileName)) {
                            mergeMap.remove(SPFileName);
                        }
                    }
                }
            }

            return hasConflict;
        }

        // 10.4 Conflict: File not in SP && Curr != given
        public boolean condition10(Map<String, String> SP,
                                   Map<String, String> given,
                                   Map<String, String> curr,
                                   Map<String, String> mergeMap) throws IOException {
            boolean hasConflict = false;
            for (String givenFileName : given.keySet()) {
                String givenBlob = given.get(givenFileName);
                String currBlob = curr.get(givenFileName);
                if (curr.containsKey(givenFileName) && !SP.containsKey(givenFileName)) {
                    if (!currBlob.equals(givenBlob)) {
                        hasConflict = true;
                        createConflictFile(currBlob, givenBlob);
                        if (mergeMap.containsKey(givenFileName)) {
                            mergeMap.remove(givenFileName);
                        }
                    }
                }
            }
            return hasConflict;
        }

        public void createConflictFile(String currBlob, String givenBlob) throws IOException {
            File blobDir = Utils.join(Main.OBJECTS_FOLDER, "blobs");
            String[] blobsFileNames = blobDir.list();

            File currBlobFile = null;
            File givenBlobFile = null;

            for (String blobFileName : blobsFileNames) {
                if (blobFileName.equals(currBlob)) {
                    currBlobFile = Utils.join(blobDir, blobFileName);
                }
                if (blobFileName.equals(givenBlob)) {
                    givenBlobFile = Utils.join(blobDir, blobFileName);
                }
            }

            if (currBlobFile != null && givenBlobFile != null) {

                Blob currBlobObj = Utils.readObject(currBlobFile, Blob.class);
                Blob givenBlobObj = Utils.readObject(givenBlobFile, Blob.class);

                String CWD = System.getProperty("user.dir");
                File conflictFile = new File(CWD, currBlobObj.getFileName());

                String currContent = new String(currBlobObj.getFileContent(),
                        StandardCharsets.UTF_8);
                String givenContent = new String(givenBlobObj.getFileContent(),
                        StandardCharsets.UTF_8);

                Utils.writeContents(conflictFile,
                        "<<<<<<< HEAD\n",
                        currContent,
                        "=======\n",
                        givenContent,
                        ">>>>>>>",
                        System.lineSeparator());

                Blob conflictFileBlob = new Blob(currBlobObj.getFileName());
                conflictFileBlob.save();
                stagingArea.add(currBlobObj.getFileName(), conflictFileBlob.getBlobSHA1());
                stagingArea.save();
            } else if (currBlobFile == null && givenBlob != null) {
                condition10_2And10_3(givenBlobFile, "curr");
            } else if (currBlobFile != null) {
                condition10_2And10_3(currBlobFile, "given");
            }
        }

        public void condition10_2And10_3(File presentBlobFile, String absentBranch) throws IOException {
            Blob presentBlobObj = Utils.readObject(presentBlobFile, Blob.class);

            String CWD = System.getProperty("user.dir");
            File conflictFile = new File(CWD, presentBlobObj.getFileName());

            String presentContent = new String(presentBlobObj.getFileContent(),
                    StandardCharsets.UTF_8);

            if (absentBranch.equals("curr")) {
                Utils.writeContents(conflictFile,
                        "<<<<<<< HEAD\n",
                        "=======\n",
                        presentContent,
                        ">>>>>>>",
                        System.lineSeparator());
            } else {
                Utils.writeContents(conflictFile,
                        "<<<<<<< HEAD\n" +
                                presentContent +
                                "=======\n" +
                                ">>>>>>>" +
                                System.lineSeparator());
            }

            Blob conflictFileBlob = new Blob(presentBlobObj.getFileName());
            conflictFileBlob.save();
            stagingArea.add(presentBlobObj.getFileName(), conflictFileBlob.getBlobSHA1());
            stagingArea.save();
        }

        public void commitMerge(String branchName, String originalBranchName) throws IOException {
            String commitMessage = "Merged " + branchName + " into " + originalBranchName + ". ";
            String firstParentSHA1 = Head.getBranchHEAD(originalBranchName).getSHA();
            String secondParentSHA1 = Head.getBranchHEAD(branchName).getSHA();

            Commit mergeCommit = new Commit(commitMessage, firstParentSHA1, secondParentSHA1,
                    false, stagingArea.getFilesStagedForAddition(),
                    stagingArea.getFilesStagedForRemoval());

            mergeCommit.saveMergeCommit();

            head.setBranchHEAD(originalBranchName,mergeCommit);
            head.setGlobalHEAD(originalBranchName, mergeCommit);

            stagingArea = new Staging();
            stagingArea.save();
        }

        public Commit latestCommonAncestor(Commit currHead, Commit branchHead) {
            HashSet<String> branchPath = new HashSet<>();
            buildBranchHashSet(branchPath, branchHead);

            HashMap<Commit, Integer> ancestors = new HashMap<>();

            getAncestors(ancestors, branchPath, currHead, 0);
            Commit LCA = null;
            Integer minDepth = -1;

            for (Map.Entry<Commit, Integer> entry : ancestors.entrySet()) {
                Commit node = entry.getKey();
                Integer depth = entry.getValue();
                if (minDepth < 0) {
                    minDepth = depth;
                    LCA = node;
                } else if (depth < minDepth) {
                    LCA = node;
                    minDepth = depth;
                }
            }

            return LCA;
        }

        public void getAncestors(HashMap<Commit, Integer> ancestors,
                                 HashSet<String> branchPath,
                                 Commit currHead,
                                 Integer depth) {
            if (currHead.getFirstParentSHA1().equals(INIT_PARENT_SHA1)) {
                return;
            }
            if (branchPath.contains(currHead.getSHA())) {
                ancestors.put(currHead, depth);
            } else {
                if (currHead.getFirstParentSHA1() != null) {
                    getAncestors(ancestors, branchPath, currHead.getParent(), depth + 1);
                }
                if (currHead.getSecondParentSHA1() != null) {
                    getAncestors(ancestors, branchPath, currHead.getParent2(), depth + 1);
                }
            }

        }

        public void buildBranchHashSet(HashSet<String> branchSet, Commit branchHead) {
            if (branchHead.getFirstParentSHA1().equals(INIT_PARENT_SHA1)) {
                return;
            }
            branchSet.add(branchHead.getSHA());

            if (branchHead.getFirstParentSHA1() != null) {
                buildBranchHashSet(branchSet, branchHead.getParent());
            }
            if (branchHead.getSecondParentSHA1() != null) {
                buildBranchHashSet(branchSet, branchHead.getParent2());
            }
        }


        // edge cases:
        // 1. If the split point is the same commit as the given branch, then we
        // do nothing; the merge is complete, and the operation ends with the message
        // Given branch is an ancestor of the current branch.
        public boolean branchHeadIsSP(Commit SP, Commit branchHead) {
            return SP.getSHA().equals(branchHead.getSHA());
        }

        // 2. If the split point is the current branch, then the effect is to check
        // out the given branch, and the operation ends after printing the message
        // Current branch fast-forwarded.
        public boolean currHeadIsSP(Commit SP) {
            return Head.getGlobalHEAD().getSHA().equals(SP.getSHA());
        }


        public void exitWithMessage(String message) {
            System.out.println(message);
            System.exit(0);
        }

        //failure case
        //1. stagingArea is present
        //2. given branch name does not exit
        //3. attempting to merge the branch it self
        //4. untracked files in the way
        public void failureCases(String branchName) {
            if (stagingArea.load().hasUncommitedChanges()) {
                exitWithMessage("You have uncommitted changes.");
            }

            if (!Branch.hasBranch(branchName)) {
                exitWithMessage("A branch with that name does not exist.");
            }

            if (branchName.equals(currentBranchName())) {
                exitWithMessage("Cannot merge a branch with itself.");
            }

            Commit givenBranchHead = Head.getBranchHEAD(branchName);

            if (hasUntrackedFilesForCheckoutBranch(givenBranchHead)) {
                exitWithMessage("There is an untracked file in the way; delete it, " +
                        "or add and commit it first.");
            }
        }
    }

    /**
     * TBD.
     */
    public void rebase(String branchName) {

    }
}
