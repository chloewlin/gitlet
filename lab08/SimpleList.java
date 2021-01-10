interface SimpleList extends SimpleCollection {

    /** Returns the integer stored at position i. */
    int get(int i);

    /** Adds k into the list at position i. Note that this is different from
     *  the basic Collection add.
     */
    void add(int i, int k);

    /** Removes the item at position i. Note that this is different from the
     *  basic Collection remove. 
     */
    void removeIndex(int i);
}
