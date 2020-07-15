public class DNASequenceSet {
    private DNANode sentinel;

    public DNASequenceSet() {
        sentinel = new DNANode(-1, false);
    }

    public void add(int[] sequence) {
        DNANode pointer = sentinel;
        for (int i = 0; i < sequence.length; i++) {
            int base = sequence[i];
            if (pointer.nexts[base] == null) {
                pointer.nexts[base] = new DNANode(base, false);
            }
            pointer = pointer.nexts[base];
        }
        pointer.endOfSequence = true;
    }

    public boolean contains(int[] sequence) {
        DNANode pointer = sentinel;
        for (int i = 0; i < sequence.length; i++) {
            int base = sequence[i];
            if (pointer.nexts[base] == null) {
                return false;
            }
            pointer = pointer.nexts[base];
        }
        return pointer.endOfSequence;
    }


    public static class DNANode {
        private int base;
        private boolean endOfSequence;
        private DNANode[] nexts;

        public DNANode(int b, boolean end) {
            this.base = b;
            this.endOfSequence = end;
            this.nexts = new DNANode[4];
        }
    }
}
