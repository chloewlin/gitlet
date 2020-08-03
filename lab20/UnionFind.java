public class UnionFind {
    public int[] disjointedSet;

    /* Creates a UnionFind data structure holding N vertices. Initially, all
       vertices are in disjoint sets. */
    public UnionFind(int N) {
        this.disjointedSet = new int[N + 1];
        for (int i = 0; i < N + 1; i++) {
            this.disjointedSet[i] = -1;
        }
    }

    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        return -this.disjointedSet[find(v)];
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        return this.disjointedSet[v];
    }

    /* Returns true if nodes V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        return find(v1) == find(v2);
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid vertices are passed into this
       function, throw an IllegalArgumentException. */
    public int find(int v) {
        if (v < 0 || v >= this.disjointedSet.length + 1) {
            throw new IllegalArgumentException();
        }

        if (this.disjointedSet[v] <= -1) {
            return v;
        }

        if (this.disjointedSet[parent(v)] > -1) {
            this.disjointedSet[v] = find(parent(v));
        }

        return this.disjointedSet[v];
    }

    /* Connects two elements V1 and V2 together. V1 and V2 can be any element,
       and a union-by-size heuristic is used. If the sizes of the sets are
       equal, tie break by connecting V1's root to V2's root. Union-ing a vertex
       with itself or vertices that are already connected should not change the
       structure. */
    public void union(int v1, int v2) {
        if (connected(v1, v2) || v1 == v2) {
            return;
        }
        if (sizeOf(v1) > sizeOf(v2)) {
            this.disjointedSet[find(v1)] -= sizeOf(v2);
            this.disjointedSet[find(v2)] = find(v1);
        } else {
            this.disjointedSet[find(v2)] -= sizeOf(v1);
            this.disjointedSet[find(v1)] = find(v2);
        }
    }
}
