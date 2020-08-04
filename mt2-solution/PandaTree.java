public class PandaTree {
	private PandaNode root;
	private PandaBear mostAtRisk;
	private PandaBear leastAtRisk;

	public PandaTree(PandaBear bear) {
		this.root = new PandaNode(bear, null, null);
		this.mostAtRisk = bear;
		this.leastAtRisk = bear;
	}

	public PandaBear mostAtRisk() {
		return this.mostAtRisk;
	}

	public PandaBear leastAtRisk() {
		return this.leastAtRisk;
	}

	//adds Panda to tree iff panda not already in tree
	public void add(PandaBear bear) {
		if(this.leastAtRisk.compareTo(bear) < 0) {
			this.leastAtRisk = bear;
		} if (this.mostAtRisk.compareTo(bear) > 0) {
			this.mostAtRisk = bear;
		}
		this.root = add(bear, root);
	}
	
	private PandaNode add(PandaBear bear, PandaNode node) {
		if (node == null) {
			return new PandaNode(bear, null, null);
		}
		if(node.item.compareTo(bear) > 0) {
			node.left = add(bear, node.left);
		} else if (node.item.compareTo(bear) < 0) {
			node.right = add(bear, node.right);
		}
		return node;
	}

	private class PandaNode {
		private PandaBear item;
		private PandaNode left;
		private PandaNode right;
		private PandaNode(PandaBear bear, PandaNode l, PandaNode r) {
			this.item = bear;
			this.left = l;
			this.right = r;
		}
	}

}