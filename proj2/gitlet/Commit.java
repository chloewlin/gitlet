package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class Commit implements Serializable {

    private String[] parentsArray = new String[2];
    public String SHA;
    public String timestamp;
    public String message;
    /* mapping between file SHA1 and blob SHA1 */
    private HashMap<String, String> map;

    private boolean init = false;

    static final File COMMITS_FOLDER = Utils.join(".gitlet", "objects/commits");

    /* constructor */
    public Commit(String message, String parent, boolean init) {
        this.message = message;
        this.parentsArray[0] = parent;
        this.SHA = Utils.sha1(message);
        this.timestamp = generateDate(init);
        this.map = new HashMap<>();
    }

    public void save() throws IOException {
        Commit commit = new Commit(this.message, this.parentsArray[0], this.init);
        File commitFile = Utils.join(COMMITS_FOLDER, this.SHA);
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
}
