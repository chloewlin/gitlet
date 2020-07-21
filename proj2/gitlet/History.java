package gitlet;

public class History {

    public void merge(String branchName) {

        // find split point/latest common ancestor

        // handle criss-cross merge: when you have TWO SP/latest common ancestor
        // find curr head of curr branch
        // go up branch
        // find depth of first parent and second parent
        // use min depth as your split point
        latestCommonAncestor();

        // edge cases:
        // 1. If the split point is the same commit as the given branch, then we
        // do nothing; the merge is complete, and the operation ends with the message
        // Given branch is an ancestor of the current branch.
        branchHeadIsSP();

        // 2. If the split point is the current branch, then the effect is to check
        // out the given branch, and the operation ends after printing the message
        // Current branch fast-forwarded.
        currHeadIsSP();

        // Otherwise, we continue with the steps below.

        Commit SP = latestCommonAncestor();
        Commit currHEAD = Head.getGlobalHEAD();
        Commit branchHEAD = Head.getBranchHEAD(branchName);

        // 1. We need to compare three commit nodes
        // 1. SP (split point)
        // 2. HEAD of current branch
        // 3. HEAD of given branch

        // compare HEAD of given branch with SP, find modified files
        compareBranchHeadWithSP();
        // compare HEAD of given branch with HEAD of curr branch
        // 1. find modified files, stage these files
        // 2. find files in curr HEAD but not in given HEAD, they should stay the way they are
        // 3. find files modified in the same way (both changed to same content, or both removed),
        // merge doesn't change these files
        // (if a file is removed in both, but that file is in CWD, don't remove it)
        compareCurrHeadWithBranchHead();

        // compare HEAD of current branch with SP:
        // 1. file not in SP but in curr branch HEAD should stay the way they are
        // 2. file in SP but not in curr branch HEAD: checked out and staged
        // 3. file in SP, unmodified in given branch HEAD, not in curr branch HEAD, stay the way
        // they are
        compareCurrHeadWithSP();

        // compare HEAD of curr branch with HEAD of given branch:
        // find "conflicts": files modified in different ways in currHEAD and branchHEAD
        // format conflict, stage them!
        // <<<<<<< HEAD
        // contents of file in current branch
        // =======
        // contents of file in given branch
        // >>>>>>>
        findMergeConflicts();

        // After user updated the files, and SP is neither curr branch HEAD or given branch HEAD
        // if we still have conflict:
        // Encountered a merge conflict.
        // if no conflict:
        // save the commit:
        // 1. Save first parent: HEAD of curr branch
        // 2. Save second parent: HEAD of given branch
        // 2. message: Merged [given branch name] into [current branch name].
        commitMerge();
    }

    public Commit latestCommonAncestor() {
        return new Commit("delete me");
    }

    public boolean branchHeadIsSP() {
        return false;
    }

    public boolean currHeadIsSP() {
        return false;
    }

    public void compareBranchHeadWithSP() {

    }

    public void compareCurrHeadWithBranchHead() {

    }

    public void compareCurrHeadWithSP() {

    }

    public void findMergeConflicts() {

    }

    public void commitMerge() {

    }
}
