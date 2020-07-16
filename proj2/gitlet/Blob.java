package gitlet;

import java.io.File;
import java.io.IOException;

public class Blob {

    private String fileName;
    private byte[] fileContent;
    private String blobSHA1;
    static final File BLOBS_FOLDER = new File(".gitlet/objects/blobs");

    /* Create a blob for a file */
    public Blob(String fileName) {
        this.fileName = fileName;
        this.fileContent = Utils.readContents(Utils.join(".", fileName));
        this.blobSHA1 = Utils.sha1("BLOB", fileContent);
    }

    /* Save blob as a file */
    public void saveBlob() throws IOException {
        File blobFile =  Utils.join(BLOBS_FOLDER, blobSHA1);
        blobFile.createNewFile();
        Utils.writeContents(blobFile, fileContent);
    }

    public String getBlobSHA1() {
        return this.blobSHA1;
    }
}
