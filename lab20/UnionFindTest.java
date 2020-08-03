import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionFindTest {

    @Test
    public void UnionFindTest1() {
        UnionFind set = new UnionFind(5);

        set.union(0, 1);
        set.union(2, 3);

        for (int i = 0; i < set.disjointedSet.length; i++) {
            System.out.print(set.disjointedSet[i] + " | " );
        }

        Assert.assertTrue(set.connected(0, 1));
        Assert.assertTrue(set.connected(2, 3));
    }

    @Test
    public void UnionFindTest2() {
        UnionFind set = new UnionFind(5);

        set.union(0, 1);
        set.union(2, 3);
        set.union(0, 2);

        for (int i = 0; i < set.disjointedSet.length; i++) {
            System.out.print(set.disjointedSet[i] + " | ");
        }
    }

    @Test
    public void UnionFindTest3() {
        UnionFind set = new UnionFind(10);

        set.union(2, 1);
        set.union(3, 1);
        set.union(5, 3);
        set.union(4, 1);
        set.union(6, 4);
        set.union(7, 4);
        set.union(8, 7);
        set.union(9, 10);

        for (int i = 0; i < set.disjointedSet.length; i++) {
            System.out.print(set.disjointedSet[i] +  " | ");
        }
    }
}
