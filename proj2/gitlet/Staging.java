package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Staging implements Serializable {

    static final File STAGING_FOLDER = Utils.join(".gitlet", "staging");
    Map<String, String> trackedFiles;
    Map<String, String> untrackedFiles;

    public Staging() {
        this.trackedFiles = new HashMap<String, String>();
        this.untrackedFiles = new HashMap<String, String>();
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

    public void save(Staging stagedFiles) {
        File currentTrackedFiles = Utils.join(STAGING_FOLDER, "trackedFiles");
        Utils.writeObject(currentTrackedFiles, stagedFiles);
    }

    public void print() {
        trackedFiles
                .forEach((key, value) -> System.out.println(key + ":" + value));
    }
}
