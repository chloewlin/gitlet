package gitlet;

import java.util.HashMap;
import java.util.Map;

public class Staging {
    Map<String, byte[]> trackedFiles;
    Map<String, byte[]> untrackedFiles;

    public Staging() {
        this.trackedFiles = new HashMap<String, byte[]>();
        this.untrackedFiles = new HashMap<String, byte[]>();
    }

    public void add(Blob blob) {
        this.trackedFiles.put(blob.fileName, blob.fileContent);
    }
}
