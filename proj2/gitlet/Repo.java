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
     */
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
     * Add a file to the staging area.
     * If the current working version of the file is identical
     * to the version in the current commit, do not stage it to
     * be added, and remove it from the staging area if it is
     * already there (as can happen when a file is changed,
     * added, and then changed back).
     * */
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
     * @blob blob the blob of the same file saved in current commit
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
     * @return
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
     * @return
     */
    public void checkoutCommit(String commitId, String fileName) throws IOException {
        Commit commit = Head.getGlobalHEAD();
        String blobSHA1 = "";
        boolean found = false;

        while (!commit.getFirstParentSHA1().equals(INIT_PARENT_SHA1)) {
            if (findMatchId(commit.getSHA(), commitId)) {
                blobSHA1 = commit.getSnapshot().get(fileName);
                found = true;
                break;
//            } else {
//                Main.exitWithError("No commit with that id exists.");
            }
            commit = commit.getParent();
        }
        if (!found) {
            Main.exitWithError("File does not exist in that commit.");
        }

        File blobFile = Utils.join(Main.BLOBS_FOLDER, blobSHA1);
        Blob blob = Blob.load(blobFile);
        restoreFileInCWD(blob);
    }

    /** check if a commit id exists in our repo */
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
        return commitSHA1.equals(commitId) ||
                commitSHA1.substring(0, commitId.length()).equals(commitId);
    }

    /**
     * Update the global HEAD pointer to point to branch HEAD.
     */
    public void checkoutBranch(String branchName) {

        // TODO: FIND BUG
        String currBranchName = currentBranchName();
        Commit branchHEAD = Head.getBranchHEAD(branchName);
        Commit currHEAD = Head.getGlobalHEAD();

        if (currBranchName.equals(branchName)) {
            Main.exitWithError("No need to checkout the current branch.");
        }
        // TODO: FIX BUG
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


        // TODO: FIX BUGS
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

        // TODO: FIX BUG
//        if (hasUntrackedFilesForCheckoutBranch(commit)) {
//            Main.exitWithError("There is an untracked file in the way;" +
//                    " delete it or add it first.");
//        }
//        if (hasUntrackedFiles()) {
//            Main.exitWithError("There is an untracked file in the way; " +
//                    "delete it, or add and commit it first.");
//        }

        // find commit
        while (!commit.getFirstParentSHA1().equals(INIT_PARENT_SHA1)) {
            if (findMatchId(commit.getSHA(), commitId)) {
                targetCommit = commit;
            }
            commit = commit.getParent();
        }

        if (targetCommit == null) {
            Main.exitWithError("No commit with that id exists.");
        }

        // checkout to that commit
        Commit currCommit = Head.getGlobalHEAD();
        restoreFilesAtCommit(currCommit, targetCommit);
        // reset global HEAD
        Head.setGlobalHEAD(currentBranchName(), targetCommit);

        stagingArea = new Staging();
        stagingArea.save();
    }

    /**
     * Checks if a working file is untracked in the HEAD of current branch
     * and would be overwritten by the checkout.
     * */
    public boolean hasUntrackedFilesForCheckoutBranch(Commit givenBranchHEAD) {
        List<String> untrackedFiles = new ArrayList<String>();
        stagingArea = stagingArea.load();
        List<String> fileInCWD = Utils.plainFilenamesIn("./");

        for (String fileName : fileInCWD) {
            if (!fileName.equals(".DS_Store") && !fileName.equals(".gitignore") && !fileName.equals(
                    "proj2.iml")) {

//            if (!Head.getGlobalHEAD().getSnapshot().containsKey(fileName)
//                    // TODO: CHECK WHICH, OR BOTH, CONDITION IS CORRECT **
//                    && !stagingArea.getFilesStagedForAddition().containsKey(fileName)
//                    && givenBranchHEAD.getSnapshot().containsKey(fileName)) {
                if (!Head.getGlobalHEAD().getSnapshot().containsKey(fileName)
                    && givenBranchHEAD.getSnapshot().containsKey(fileName)) {
                      untrackedFiles.add(fileName);
                }
            }
        }
        return untrackedFiles.size() > 0;
    }

    /**
     * Returns if there are untracked files in CWD.
     * */
    public boolean hasUntrackedFiles() {

        // TODO: HAS BUGS

        stagingArea = stagingArea.load();
        List<String> fileInCWD = Utils.plainFilenamesIn("./");
        for (String fileName : fileInCWD) {
            if (!stagingArea.containsFileForAddition(fileName)
                    && !stagingArea.containsFileForRemoval(fileName)) {
                return true;
            }
        }
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
        delete.forEach((file, blobSHA1) -> Utils.restrictedDelete(file));
    }

    /**
     * Merge given branch into current branch.
     */
    public void merge(String[] args) throws IOException {
        String givenBranch = args[1];
        merge.merge(givenBranch);
    }

    private class Merge {

        public void merge(String branchName) throws IOException {
            Commit currHEAD = Head.getGlobalHEAD();
            Commit givenHEAD = Head.getBranchHEAD(branchName);
            String originalBranchName = Utils.readObject(
                    (Utils.join(Main.GITLET_FOLDER, "HEAD")), Branch.class)
                    .getName();
            // find split point/latest common ancestor

            // handle criss-cross merge: when you have TWO SP/latest common ancestor
            // find curr head of curr branch
            // go up branch
            // find depth of first parent and second parent
            // use min depth as your split point
            Commit SP = latestCommonAncestor(currHEAD, givenHEAD);

            Map<String, String> curr = currHEAD.getSnapshot();
            Map<String, String> given = givenHEAD.getSnapshot();
            Map<String, String> sp = SP.getSnapshot();

            //failure case
            //print error msg and error out
//            if (failureCases(branchName)) {
//                return;
//            }

            // saved to commit (add)
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
            condition8And9(sp,given, curr, deletedAtOne);


//            System.out.println("=========== merge map =========");
//            mergeMap.forEach((k, v) -> {
//                System.out.println(k + " : " + v);
//            });
//            System.out.println();
//
//            System.out.println("=========== both deleted =========");
//            bothDeleted.forEach((k, v) -> {
//                System.out.println(k + " : " + v);
//            });
//
//            System.out.println("=========== deleted at one =========");
//            deletedAtOne.forEach((k, v) -> {
//                System.out.println(k + " : " + v);
//            });

            // compare HEAD of curr branch with HEAD of given branch:
            // find "conflicts": files modified in different ways in currHEAD and branchHEAD
            // format conflict, stage them!
            // <<<<<<< HEAD
            // contents of file in current branch
            // =======
            // contents of file in given branch
            // >>>>>>>
//            findMergeConflicts();

            // After user updated the files, and SP is neither curr branch HEAD or given branch HEAD
            // if we still have conflict:
            // Encountered a merge conflict.
            // if no conflict:
            // save the commit:
            // 1. Save first parent: HEAD of curr branch
            // 2. Save second parent: HEAD of given branch
            // 2. message: Merged [given branch name] into [current branch name].
            // TODO: Create a custom commit to store mergeMap and delete and deleteAtOne
            commitMerge(branchName, originalBranchName);
            restoreFilesAtMerge(mergeMap, deletedAtOne);
        }

        public void restoreFilesAtMerge(Map<String, String> mergeMap,
                                        Map<String, String> deleteAtOne) {

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
//            delete.forEach((file, blobSHA1) -> {
//                Utils.restrictedDelete(file);
//            });
        }

        // case 3:
        // givenBranch: modified && currBranch: unmodified (=SP)
        // -> copy givenBranch key-value pair, add into hashmap (stageToBeAdded), and auto staged
        public void condition3(Map<String, String> SP,
                                                  Map<String, String> given,
                                                  Map<String, String> curr,
                                                  Map<String, String> mergeMap) {
            stagingArea = stagingArea.load();
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

            stagingArea.save();
        }

        // case 4. currBranch: modified && givenBranch: unmodified (=SP)
        // -> stay the same (continue?)
        public void condition4(Map<String, String> SP,
                               Map<String, String> given,
                               Map<String, String> curr,
                               Map<String, String> mergeMap) {
            stagingArea = stagingArea.load();
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

//            System.out.println("====== sameOnGivenAndSP ===== ");
//            sameOnGivenAndSP.forEach((k, v) -> {
//                System.out.println(k + " : " + v);
//            });

            // compare SP and Curr, find same file name with different content
            SP.forEach((spFileName, spBlob) -> {
                curr.forEach((currFileName, currBlob) -> {
                    if (spFileName.equals(currFileName) && !spBlob.equals(currBlob)) {
                        diffOnCurrAndSP.put(currFileName, currBlob);
                    }
                });
            });

//            System.out.println("====== diffOnCurrAndSP ===== ");
//            diffOnCurrAndSP.forEach((k, v) -> {
//                System.out.println(k + " : " + v);
//            });

            // find files modified on Curr but not on Given since SP
            sameOnGivenAndSP.forEach((sameFileName, sameBlob) -> {
                diffOnCurrAndSP.forEach((diffFileName, diffBlob) -> {
                    if (sameFileName.equals(diffFileName)) {
                        mergeMap.put(diffFileName, diffBlob);
                        stagingArea.add(diffFileName, diffBlob);
                    }
                });
            });

            stagingArea.save();
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
            stagingArea = stagingArea.load();
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
            stagingArea.save();
        }

        // 6. File not in SP && File in curr
        // -> add curr file and blob into mergeMap
        public void condition6(Map<String, String> SP,
                               Map<String, String> curr,
                               Map<String, String> mergeMap) {
            stagingArea = stagingArea.load();
            for (String currFileName : curr.keySet()) {
                if (!SP.keySet().contains(currFileName)) {
                    String currBlob = curr.get(currFileName);
                    mergeMap.put(currFileName, currBlob);
                    stagingArea.add(currFileName, currBlob);
                }
            }
            stagingArea.save();
        }

        // 7. File not in SP && File in given
        // -> add given file and given blob into merge map
        public void condition7(Map<String, String> SP,
                               Map<String, String> given,
                               Map<String, String> mergeMap) {
            stagingArea = stagingArea.load();
            for (String givenFileName : given.keySet()) {
                if (!SP.containsKey(givenFileName)){
                    String givenBlob = given.get(givenFileName);
                    mergeMap.put(givenFileName, givenBlob);
                    stagingArea.add(givenFileName, givenBlob);
                }
            }
            stagingArea.save();
        }

        // File in SP && current: unmodified && given: absent (treat like modified)
        // -> removed (untracked)
        // File in SP && given: unmodified && curr: absent (treat like modified)
        // -> remain absent (unchanged?)
        public void condition8And9(Map<String, String> SP,
                                   Map<String, String> given,
                                   Map<String, String> curr,
                                   Map<String, String> deletedAtOne) {
            stagingArea = stagingArea.load();
            for (String SPFileName : SP.keySet()) {
                boolean insideCurr = curr.containsKey(SPFileName);
                boolean insideGiven = given.containsKey(SPFileName);
                String SPBlob = SP.get(SPFileName);
                String givenBlob = given.get(SPFileName);
                String currBlob = curr.get(SPFileName);

                // absent at given, curr blob is the same version as SP blob
                if (insideCurr && !insideGiven) {
                    if (currBlob.equals(SPBlob)) {
//                        deletedAtOne.put(SPFileName, givenBlob); // TODO: BECOMES NULL
                        deletedAtOne.put(SPFileName, SPBlob);
                        stagingArea.unstage(SPFileName);
                    }
                }

                // absent at curr, present at given
                if (!insideCurr && insideGiven) {
                    if (givenBlob.equals(SPBlob)) {
                        deletedAtOne.put(SPFileName, currBlob);
                        stagingArea.unstage(SPFileName);
                    }
                }
            }
            stagingArea.save();
        }

        public void commitMerge(String branchName, String originalBranchName) throws IOException {
            String commitMessage = "Merged " + branchName + " into " + originalBranchName + ". ";
            String firstParentSHA1 = Head.getBranchHEAD(originalBranchName).getSHA();
            String secondParentSHA1 = Head.getBranchHEAD(branchName).getSHA();

            // TODO: Do we need to handle deleted files
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
//            ArrayList<Commit> currPath = new ArrayList<>();
            HashSet<String> currPath = new HashSet<>();
            Commit SP = null;

            while (!currHead.getFirstParentSHA1().equals(Repo.INIT_PARENT_SHA1)) {
//                currPath.add(currHead);
                currPath.add(currHead.getSHA());
                currHead = currHead.getParent();
            }

            while (!branchHead.getFirstParentSHA1().equals(Repo.INIT_PARENT_SHA1)) {
//                for (int i = 0; i < currPath.size(); i++) {
//                    if (branchHead.getSHA().equals(currPath.get(i).getSHA())) {
//                        SP = currPath.get(i);
//                        break loop;
//                    }
//                 }
                if (currPath.contains(branchHead.getSHA())) {
                    SP = branchHead;
                    break;
                }

                branchHead = branchHead.getParent();
            }

//            System.out.println("Split point: " + SP.getSHA());
            return SP;
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


        public void findMergeConflicts() {

        }

        public void commitMerge() {

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
        public boolean failureCases(String branchName) {
            stagingArea = stagingArea.load();
            Commit branchHEAD = Head.getBranchHEAD(branchName);

            if (!stagingArea.isEmpty()) {
                exitWithMessage("You have uncommitted changes.");
                return true;
            }
            if (!Branch.hasBranch(branchName)) {
                exitWithMessage("A branch with that name does not exist.");
                return true;
            }
            if (branchHEAD.equals(currentBranchName())) {
                exitWithMessage("Cannot merge a branch with itself.");
                return true;
            }
            // TODO: FIX hasUntrackedFilesForCheckoutBranch
            if (hasUntrackedFilesForCheckoutBranch(branchHEAD)) {
                exitWithMessage("There is an untracked file in the way; delete it, " +
                        "or add and commit it first.");
                return true;
            }
            return false;
        }
    }


    /**
     * TBD.
     */
    public void rebase(String branchName) {

    }
}
