package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

public class Staging implements Serializable {

    private Map<String, String> stagedForAddition;
    private HashSet<String> stagedForRemoval;

    public Staging() {
        this.stagedForAddition = new TreeMap<String, String>();
        this.stagedForRemoval = new HashSet<String>();
    }

    public Staging(Map<String, String> trackedFiles, HashSet<String> untrackedFiles) {
        this.stagedForAddition = trackedFiles;
        this.stagedForRemoval = untrackedFiles;
    }

    /**
     * Store the mapping of file and blob like so:
     * Hello.txt:$1d229271928d3f9e2bb0375bd6ce5db6c6d348d9
     * Staging an already-staged file overwrites the previous
     * entry in the staging area with the new contents.
     * If a file was removed and added back, remove it from
     * stagedForRemoval.
     * */
    public void add(String fileName, String blobSHA1) {
//        if (this.stagedForRemoval.contains(fileName)) {
//            this.stagedForRemoval.remove(fileName);
//        }
        this.stagedForAddition.put(fileName, blobSHA1);
    }

    public void remove(String fileName) {
        this.stagedForAddition.remove(fileName);
    }

    public void removeFromStagedForRemoval(String fileName) {
        this.stagedForRemoval.remove(fileName);
    }

    public boolean containsFileForAddition(String fileName) {
        return this.stagedForAddition.containsKey(fileName);
    }

    public boolean containsFileForRemoval(String fileName) {
        return this.stagedForRemoval.contains(fileName);
    }

    public void unstage(String fileName) {
        this.stagedForRemoval.add(fileName);
    }

    public boolean isEmpty() {
        return this.stagedForAddition.isEmpty() && this.stagedForRemoval.isEmpty();
    }

    public boolean hasUncommitedChanges() {
        return !this.stagedForAddition.isEmpty() || !this.stagedForRemoval.isEmpty();
    }

    public Blob getBlobOfFile(String fileName) {
        String blobSHA1 = this.stagedForAddition.get(fileName);
        File blobFile = Utils.join(Main.BLOBS_FOLDER, blobSHA1);
        Blob blob = Blob.load(blobFile);
        return blob;
    }

    public void save() {
        Staging stage = new Staging(this.stagedForAddition, this.stagedForRemoval);
        File index = Utils.join(Main.STAGING_FOLDER, "index");
        Utils.writeObject(index, stage);
    }

    public Staging load() {
        File index = Utils.join(Main.STAGING_FOLDER, "index");
        return Utils.readObject(index, Staging.class);
    }

    public Map<String, String> getFilesStagedForAddition() {
        return this.stagedForAddition;
    }

    public HashSet<String> getFilesStagedForRemoval() {
        return this.stagedForRemoval;
    }

    public void printStagedForAddition() {
        System.out.println("Currently added files on Staging....");
        stagedForAddition.forEach((key, value) -> System.out.println(key + " : " + value));
    }

    public void printStagedForRemoval() {
        System.out.println("Currently removed files on Staging....");
        stagedForRemoval.forEach(s -> System.out.println(s));
    }

    /**
     * To-do: Can we also stop tracking the "removed files"?
     * If not, we might not be able to instantiate a new staging obj
     */
//    public Staging clear() {
//        Staging newStagingArea = new Staging();
//        return newStagingArea;
//    }
}
