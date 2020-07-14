package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class Commit implements Serializable {

    /* arrays for SHA1 parents commits, start with all 0s */
    private String[] parentsArray = new String[2];

    /* SHA1 for current commit */
    public String SHA;

    /* timestamp when the commit is generated */
    public String timestamp;

    /* commit message */
    public String message;

    /* mapping between file SHA1 and blob SHA1 */
    private HashMap map;

    private boolean init = false;

    static final File COMMITS_FOLDER = Utils.join(".gitlet", "objects/commits");

    /* constructor */
    public Commit(String message, String parent, boolean init) {
        this.message = message;
        this.parentsArray[0] = parent;
        this.SHA = Utils.sha1(message);
        this.timestamp = generateDate(init);
    }

    public void saveCommit() {
        Commit commit = new Commit(this.message, this.parentsArray[0], this.init);
        File commmitFile = Utils.join(COMMITS_FOLDER, this.SHA);
        try {
            commmitFile.createNewFile();
        } catch (IOException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
        Utils.writeObject(commmitFile, commit);
    }

    public String generateDate(boolean init) {
        TimeZone tz = TimeZone.getTimeZone("PST");
        Calendar cal = Calendar.getInstance(tz);
        if (init) {
            cal.setTimeInMillis(0);
        }
        DateFormat sdf = new SimpleDateFormat("EEE d MMM HH:mm:ss yyyy Z");
        sdf.setTimeZone(tz);
        return sdf.format(cal.getTime());
    }
}
