package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.*;

/**
 * A Commit class represents a commit node and
 * saves a snapshot of staged files, parent SHA1,
 * and metadata including timestamp and commit message.
 *
 * @author Chloe Lin, Christal Huang
 */
public class Commit implements Serializable {

    /**
     * the SHA1s of a commit node's parent nodes.
     */
    private String[] parents = new String[2];
    /**
     * the SHA1 of a commit node.
     */
    private String sha1;
    /**
     * the timestamp when a commit node is instantiated.
     */
    private String timestamp;
    /**
     * the commit message in a commit node.
     */
    private String message;
    /**
     * the snapshot of a commit node, which is stored as
     * a mapping of the SHA1 of a staged file and the SHA1
     * of a blob.
     */
    private Map<String, String> snapshot;
    /**
     * the snapshot of deleted files
     */
    private HashSet<String> deletedSnapshot = new HashSet<>();

    /**
     * a flag for the first commit node.
     */
    private boolean init = false;

    /**
     * Instantiate a commit node.
     * @param msg commit message
     * @param parent the SHA1 of the parent commit nodes
     * @param initial a boolean value to separate initial commit
     * @param map the file-blob hashmap
     */
    public Commit(String msg, String parent, boolean initial, Map<String, String> map) {
        List<String> list = new ArrayList<String>(map.values());
        // create unique sha
        String blobFileNames = "";
        for (String blobFileName : list) {
            blobFileNames += blobFileName;
        }
        this.message = msg;
        this.parents[0] = parent;
        this.sha1 = Utils.sha1("COMMIT" + message + blobFileNames);
        this.timestamp = generateDate(initial);
        this.snapshot = map;
        this.init = initial;
    }

    /** for init only */
    public Commit(String msg, Map<String, String> map) {
        this.message = msg;
        this.parents[0] = Repo.INIT_PARENT_SHA1;
        this.snapshot = map;
        this.sha1 = Utils.sha1("COMMIT" + message);
        this.timestamp = generateDate(false);
    }

    /**
     * Save the first commit node.
     */
    public void saveInit() throws IOException {
        Commit commit = new Commit(this.message, this.parents[0],
                true, this.snapshot);
        File commitFile = Utils.join(Main.COMMITS_FOLDER, this.sha1);
        File commitLogs = Utils.join(Main.LOGS_FOLDER, this.sha1);
        commitFile.createNewFile();
        commitLogs.createNewFile();
        Utils.writeObject(commitFile, commit);
        Utils.writeObject(commitLogs, commit);
    }

    /** for merge commits only */
    public Commit(String msg,
                  String firstParent,
                  String secondParent,
                  boolean initial,
                  Map<String, String> map,
                  HashSet<String> deletedSnapshot) {

        List<String> list = new ArrayList<String>(map.values());
        // create unique sha
        String blobFileNames = "";
        for (String blobFileName : list) {
            blobFileNames += blobFileName;
        }
        this.message = msg;
        this.parents[0] = firstParent;
        this.parents[1] = secondParent;
        this.sha1 = Utils.sha1("MERGE" + message + blobFileNames);
        this.timestamp = generateDate(initial);
        this.init = initial;
        this.snapshot = map; // TODO: files added
        this.deletedSnapshot = deletedSnapshot; // TODO: files deleted at merge
    }

    /**
     * Save a commit node into a byte array.
     */
    public void save() throws IOException {
        Commit commit = new Commit(this.message, this.parents[0],
                this.init, this.snapshot);
        File commitFile = Utils.join(Main.COMMITS_FOLDER, this.sha1);
        commitFile.createNewFile();
        Utils.writeObject(commitFile, commit);
    }

    /**
     * Save a MERGE commit node into a byte array.
     */
    public void saveMergeCommit() throws IOException {
        Commit commit =
                new Commit(this.message,
                        this.parents[0],
                        this.parents[1],
                        this.init,
                        this.snapshot,
                        this.deletedSnapshot);
        File commitFile = Utils.join(Main.COMMITS_FOLDER, this.sha1);
        commitFile.createNewFile();
        Utils.writeObject(commitFile, commit);
    }

    /**
     * Return the parent commit node of a given node.
     */
    public Commit getParent() {
        String parentSha1 = this.parents[0];
        Commit parent = load(parentSha1);
        return parent;
    }

    public Commit getParent2() {
        String parent2Sha1 = this.parents[1];
        Commit parent = load(parent2Sha1);
        return parent;
    }

    /**
     * Return a commit node from a byte array.
     */
    public static Commit load(String sha1) {
        File commitFile = Utils.join(Main.COMMITS_FOLDER, sha1);
        return Utils.readObject(commitFile, Commit.class);
    }

    /**
     * Return the SHA1 of the first parent commit node.
     */
    public String getFirstParentSHA1() {
        return this.parents[0];
    }

    /**
     * Return the SHA1 of the second parent commit node.
     */
    public String getSecondParentSHA1() {
        return this.parents[1];
    }

    /**
     * Return the SHA1 of a commit node.
     */
    public String getSHA() {
        return this.sha1;
    }

    /**
     * Return the commit message of a commit node.
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Return the timestamp of a commit node.
     */
    public String getTimestamp() {
        return this.timestamp;
    }

    /**
     * Return the snapshot hashmap of a commit node.
     */
    public Map<String, String> getSnapshot() {
        return this.snapshot;
    }

    /**
     * Generate a timestamp for a commit node.
     * @param initial returns true if it is the first commit
     */
    public String generateDate(boolean initial) {
        TimeZone tz = TimeZone.getTimeZone("PST");
        Calendar cal = Calendar.getInstance(tz);
        if (initial == true) {
            cal.setTimeInMillis(0);
        }
        DateFormat sdf = new SimpleDateFormat("EEE LLL d HH:mm:ss y Z");
        sdf.setTimeZone(tz);
        return sdf.format(cal.getTime());
    }

    /**
     * Print the snapshot hashmap of a commit node.
     */
    public void printMap() {
        this.snapshot.forEach((key, value)
            -> System.out.println(key + " : " + value));
    }
}
