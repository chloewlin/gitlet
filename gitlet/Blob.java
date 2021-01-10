package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * A Blob class saves a staged file as a byte array.
 *
 * @author Chloe Lin, Christal Huang
 */
public class Blob implements Serializable {

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
        this.blobSHA1 = Utils.sha1(this.fileContent);
    }

    /**
     * Blob Constructor.
     * @param filename the filename
     * @param blobSHA1 the blobSHA1
     * @param fileContent the fileContent
     */

    public Blob(String filename, byte[] fileContent, String blobSHA1) {
        this.fileName = filename;
        this.fileContent = fileContent;
        this.blobSHA1 = blobSHA1;
        //style check error: param name should be different with this.blobSHA1
        //also for fileContent
    }

    /**
     * Save a blob as a byte array. Use SHA1 as its file name.
     */
    public void save() throws IOException {
        Blob blob = new Blob(this.fileName, this.fileContent, this.blobSHA1);
        File blobFile = Utils.join(Main.BLOBS_FOLDER, this.getBlobSHA1());
        blobFile.createNewFile();
        Utils.writeObject(blobFile, blob);
    }

    /**
     * Return a blob object from the byte array.
     * @param blob the blob
     */
    public static Blob load(File blob) {
        return Utils.readObject(blob, Blob.class);
    }

    /**
     * Return SHA1 of a blob.
     */
    public String getBlobSHA1() {
        return this.blobSHA1;
    }

    /**
     * Return the file name of a blob.
     */
    public String getFileName() {
        return this.fileName;
    }


    /**
     * Return the file content byte array of a blob.
     */
    public byte[] getFileContent() {
        return this.fileContent;
    }
}
