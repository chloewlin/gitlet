package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

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
        this.message = msg;
        this.parents[0] = parent;
        this.sha1 = Utils.sha1("COMMIT" + message);
        this.timestamp = generateDate(initial);
        this.snapshot = map;
        this.init = initial;
    }

    public Commit(String msg) {
        this.message = msg;
        this.parents[0] = Repo.INIT_PARENT_SHA1;
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
        commitFile.createNewFile();
        Utils.writeObject(commitFile, commit);
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
     * Return the parent commit node of a given node.
     */
    public Commit getParent() {
        String parentSha1 = this.parents[0];
        Commit parent = load(parentSha1);
        return parent;
    }

    /**
     * Return a commit node from a byte array.
     */
    public Commit load(String sha1) {
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
