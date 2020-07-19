package gitlet;

import java.util.List;

public class Status {

    public static void getGlobalStatus() {
        System.out.println("=== Branches ===");
        getBranchStatus();
        System.out.println("=== Staged Files ===");
        getStagedFilesStatus();
        System.out.println("=== Removed Files ===");
        getRemovedFilesStatus();
        System.out.println("=== Modifications Not Staged For Commit ===");
        getModificationsStatus();
        System.out.println("=== Untracked Files ===");
        getUntrackedStatus();
    }

    /**
     * prints the status of branches
     */
    public static void getBranchStatus() {
        /**
         * To-do: handle the situation when having multiple branches.
         * Do this after finishing the method to create branch.
         */
        List<String> branchNames = Utils.plainFilenamesIn(Main.HEADS_REFS_FOLDER);
        Branch currBranch = Utils
                .readObject((Utils.join(Main.GITLET_FOLDER, "HEAD")), Branch.class);

        branchNames.forEach((name) -> {
            if (currBranch.getName().equals(name)) {
                System.out.println("*" + name);
            } else {
                System.out.println(name);
            }
        });
        System.out.println();
    }

    public static void getStagedFilesStatus() {
        Repo.stagingArea
                .load()
                .getFilesStagedForAddition()
                .forEach((name, SHA1) -> {
                    System.out.println(name);
                });
        System.out.println();
    }

    /**
     * To-do: Can we also stop tracking the "removed files"?
     */
    public static void getRemovedFilesStatus() {
        Repo.stagingArea
                .load()
                .getFilesStagedForRemoval()
                .forEach(System.out::println);
        System.out.println();
    }

    /**
     * To-do: Extra credit.
     *
     * There is an empty line between sections. Entries should be listed
     * in lexicographic order, using the Java string-comparison order
     * (the asterisk doesn’t count). A file in the working directory is
     * “modified but not staged” if it is
     *
     * 1. Tracked in the current commit, changed in the working directory, but not staged; or
     * 2. Staged for addition, but with different contents than in the working directory; or
     * 3. Staged for addition, but deleted in the working directory; or
     * 4. Not staged for removal, but tracked in the current commit and deleted
     * from the working directory.
     */
    public static void getModificationsStatus() {
        /** FIX ME */
        System.out.println();
    }

    /**
     * To-do: Extra credit.
     *
     * The final category (“Untracked Files”) is for files present in
     * the working directory but neither staged for addition nor tracked.
     * This includes files that have been staged for removal, but
     * then re-created without Gitlet’s knowledge. Ignore any
     * subdirectories that may have been introduced, since Gitlet
     * does not deal with them.
     */
    public static void getUntrackedStatus() {
        /** FIX ME */
        System.out.println();
    }
}
