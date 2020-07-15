package gitlet;

import java.io.File;

public class Blob {

    String fileName;
    byte[] fileContent;
    String fileSHA1;
    static final File BLOBS_FOLDER = new File(".gitlet/objects/blobs");

    /* Create a blob */
    public Blob(String fileName) {
        this.fileName = fileName;
        this.fileContent = Utils.readContents(Utils.join(".", fileName));
        this.fileSHA1 = Utils.sha1(fileContent);
    }

    /* Save blob as a file */
    public static void saveBlob() {
    }
}
