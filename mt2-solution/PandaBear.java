public class PandaBear extends Bear implements Comparable<PandaBear> {

	public PandaBear(int[] arr, int w) {
		super(arr, w);
	}

	@Override
	public int compareTo(PandaBear other) {
		int min = Math.min(this.foodSupply.length, other.foodSupply.length);
		if (this.weight - other.weight == 0) {
			for(int i = 0; i < min; i++) {
				if (this.foodSupply[i] != other.foodSupply[i]) {
					return Integer.compare(this.foodSupply[i], other.foodSupply[i]);
				}
			}
			return 0;
		}
		return Integer.compare(this.weight, other.weight);
	}
}