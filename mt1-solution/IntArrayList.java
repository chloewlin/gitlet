import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class IntArrayList {
    private ArrayList<Integer> innerList;

    public IntArrayList() {
        innerList = new ArrayList<>();
    }

    public void setList(int... nums) {
        innerList = Arrays.stream(nums).boxed().collect(Collectors.toCollection(ArrayList::new));
    }

    public int get(int i) {
        return innerList.get(i);
    }

    public int size() {
        return innerList.size();
    }

    public void add(int e) {
        innerList.add(e);
    }
}
