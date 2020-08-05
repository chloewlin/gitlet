import java.util.Arrays;

public abstract class Bear {
    public int[] foodSupply;
    public int weight;
    public Bear(int[] supply, int w) {
    	this.foodSupply = supply;
    	this.weight = w;
    }

    @Override
    public boolean equals(Object o) {
    	boolean check = o instanceof Bear;
    	if(o == null || ! check) {
    		return false;
    	}
    	Bear that = (Bear) o;
    	return that.weight == this.weight && Arrays.equals(this.foodSupply, that.foodSupply);
    }
}