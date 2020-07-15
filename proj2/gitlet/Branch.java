package gitlet;

import java.io.Serializable;

public class Branch implements Serializable {
    private String name;
    private Commit head;

    public Branch(String name, Commit commit) {
        this.name = name;
        this.head = commit;
    }

    public String getName() {
        return this.name;
    }

    public Commit getHead() {
        return this.head;
    }
}
