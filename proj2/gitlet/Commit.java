package gitlet;

import java.util.Date;
import java.util.HashMap;

public class Commit {

    /* arrays for SHA1 parents commits, start with all 0s */
    private String[] parentsArray;

    /* SHA1 for current commit */
    private String curr;

    /* timestamp when the commit is generated */
    private Date timestamp;

    /* commit message */
    private String message;

    /* mapping between file SHA1 and blob SHA1 */
    private HashMap map;

    /* constructor */
    public Commit(String message) {
        this.message = message;
    }

}
