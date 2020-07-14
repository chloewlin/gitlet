package gitlet;

import java.io.File;

public class Repo {

    // create initial commit and set up branch and HEAD pointer
    public void init() {

    }

    // add one file to the hashmap in index(staging)
    public void add(File file) {

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
