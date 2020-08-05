public class StaffTree {
	public StaffNode root;
	public PandaBear mostAtRisk;
	public PandaBear leastAtRisk;

	public StaffTree(PandaBear bear) {
		this.root = new StaffNode(bear, null, null);
		this.mostAtRisk = bear;
		this.leastAtRisk = bear;
	}
	public PandaBear leastAtRisk() {
		return this.leastAtRisk;
	}
	public PandaBear mostAtRisk() {
		return this.mostAtRisk;
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
	public StaffNode add(PandaBear bear, StaffNode node) {
		if (node == null || node.item == null) {
			return new StaffNode(bear, null, null);
		}
		if(node.item.compareTo(bear) > 0) {
			node.left = add(bear, node.left);
		} else if (node.item.compareTo(bear) < 0) {
			node.right = add(bear, node.right);
		}
		return node;
	}

	public class StaffNode {
		public PandaBear item;
		public StaffNode left;
		public StaffNode right;
		public StaffNode(PandaBear bear, StaffNode l, StaffNode r) {
			this.item = bear;
			this.left = l;
			this.right = r;
		}
	}

}