package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

public class CommitsMap implements Serializable {
    private HashMap<String, String> allCommits;

    public CommitsMap() {
        this.allCommits = new HashMap<String, String>();
    }

    public CommitsMap(HashMap<String,String> msg) {
        this.allCommits = msg;
    }

    /**
     * Store the mapping of file and blob like so:
     * Hello.txt:$1d229271928d3f9e2bb0375bd6ce5db6c6d348d9
     * */
    public void add(String fileName, String blobSHA1) {
        this.allCommits.put(fileName, blobSHA1);
    }


}
