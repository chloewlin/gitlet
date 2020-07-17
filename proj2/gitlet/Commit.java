package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Commit implements Serializable {

    private String[] parents = new String[2];
    public String SHA;
    public String timestamp;
    public String message;
    /* mapping between file SHA1 and blob SHA1 */
    Map<String, String> map = new HashMap<>();

    private boolean init = false;

    public Commit(String message, String parent, boolean init, Map<String, String> map) {
        this.message = message;
        this.parents[0] = parent;
        this.SHA = Utils.sha1(message);
        this.timestamp = generateDate(init);
        this.map = map;
    }

    public void saveInit() throws IOException {
        Commit commit = new Commit(this.message, this.parents[0], this.init, null);
        File commitFile = Utils.join(Main.Commits, this.SHA);
        commitFile.createNewFile();
        Utils.writeObject(commitFile, commit);
    }

    public void save() throws IOException {
        Staging stage = Staging.load();
        Commit commit =
                new Commit(this.message, this.parents[0], this.init, stage.getTrackedFiles());
        File commitFile = Utils.join(Main.Commits, this.SHA);
        commitFile.createNewFile();
        Utils.writeObject(commitFile, commit);
    }

    public String generateDate(boolean init) {
        TimeZone tz = TimeZone.getTimeZone("PST");
        Calendar cal = Calendar.getInstance(tz);
        if (init) {
            cal.setTimeInMillis(0);
        }
        DateFormat sdf = new SimpleDateFormat("EEE LLL d HH:mm:ss y Z");
        sdf.setTimeZone(tz);
        return sdf.format(cal.getTime());
    }

    public String getSHA() {
        return this.SHA;
    }
}
