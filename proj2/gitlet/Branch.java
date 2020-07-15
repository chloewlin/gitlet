package gitlet;

import java.io.Serializable;

public class Branch implements Serializable {
    private String name;
    private String head;

    public Branch(String name, String SHA1) {
        this.name = name;
        this.head = SHA1;
    }

    public String getName() {
        return this.name;
    }

    public String getHead() {
        return this.head;
    }
}
