package gitlet;

import java.io.File;
import java.io.IOException;

/**
 * A Blob class saves a staged file as a byte array.
 *
 * @author Chloe Lin, Christal Huang
 */
public class Blob {

    /**
     * The name of a staged file.
     */
    private String fileName;
    /**
     * The content of a staged file.
     */
    private byte[] fileContent;
    /**
     * The SHA1 of a blob object.
     */
    private String blobSHA1;
    /**
     * Create a blob for a file.
     * @param filename the name of the staged file.
     */
    public Blob(String  filename) {
        this.fileName = filename;
        this.fileContent = Utils.readContents(Utils.join(".", filename));
        this.blobSHA1 = Utils.sha1("BLOB", fileContent);
    }

    /**
     * Save a blob as a byte array.
     */
    public void save() throws IOException {
        File blobFile =  Utils.join(Main.BLOBS_FOLDER, blobSHA1);
        blobFile.createNewFile();
        Utils.writeObject(blobFile, fileContent);
    }

    /**
     * Return SHA1 of a blob.
     */
    public String getBlobSHA1() {
        return this.blobSHA1;
    }
}
