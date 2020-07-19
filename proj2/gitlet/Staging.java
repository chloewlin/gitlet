package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

public class Staging implements Serializable {

    private Map<String, String> trackedFiles;
    private HashSet<String> untrackedFiles;

    public Staging() {
        this.trackedFiles = new TreeMap<String, String>();
        this.untrackedFiles = new HashSet<String>();
    }

    public Staging(Map<String, String> trackedFiles, HashSet<String> untrackedFiles) {
        this.trackedFiles = trackedFiles;
        this.untrackedFiles = untrackedFiles;
    }

    /**
     * Store the mapping of file and blob like so:
     * Hello.txt:$1d229271928d3f9e2bb0375bd6ce5db6c6d348d9
     * Staging an already-staged file overwrites the previous
     * entry in the staging area with the new contents
     * */
    public void add(String fileName, String blobSHA1) {
        this.trackedFiles.put(fileName, blobSHA1);
    }

    public void remove(String fileName) {
        this.trackedFiles.remove(fileName);
    }

    public boolean containsFile(String fileName) {
        return this.trackedFiles.containsKey(fileName);
    }

    public void unstage(String fileName) {
        this.untrackedFiles.add(fileName);
    }

    public Blob getBlobOfFile(String fileName) {
        String blobSHA1 = this.trackedFiles.get(fileName);
        File blobFile = Utils.join(Main.BLOBS_FOLDER, blobSHA1);
        Blob blob = Blob.load(blobFile);
        return blob;
    }

    public void save() {
        Staging stage = new Staging(this.trackedFiles, this.untrackedFiles);
        File currentTrackedFiles = Utils.join(Main.STAGING_FOLDER, "index");
        Utils.writeObject(currentTrackedFiles, stage);
    }

    public Staging load() {
        File currentTrackedFiles = Utils.join(Main.STAGING_FOLDER, "index");
        return Utils.readObject(currentTrackedFiles, Staging.class);
    }

    public Map<String, String> getTrackedFiles() {
        return this.trackedFiles;
    }

    public HashSet<String> getUntrackedFiles() {
        return this.untrackedFiles;
    }

    public void printTrackedFiles() {
        System.out.println("Currently tracked files on Staging....");
        trackedFiles.forEach((key, value) -> System.out.println(key + " : " + value));
    }

    public void printUntrackedFiles() {
        System.out.println("Currently untracked files on Staging....");
        untrackedFiles.forEach(s -> System.out.println(s));
    }

    /**
     * To-do: Can we also stop tracking the "removed files"?
     * If not, we might not be able to instantiate a new staging obj
     */
    public void clear() {
//        Staging newStagingArea = new Staging();
//        newStagingArea.save();
//        Repo.stagingArea.resetStagingArea(new TreeMap<>());
        this.trackedFiles = new TreeMap<>();
        Repo.stagingArea.save();
    }

    public void resetStagingArea(TreeMap<String, String> newTrackedFiles) {
        this.trackedFiles = newTrackedFiles;
        Repo.stagingArea.save();
    }
}
