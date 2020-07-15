package gitlet;

import java.io.File;
import java.io.IOException;

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
    public void saveBlob() throws IOException {
        File blobFile =  Utils.join(BLOBS_FOLDER, fileSHA1);
        blobFile.createNewFile();
        Utils.writeContents(blobFile, fileContent);
    }
}
