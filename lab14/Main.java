import java.util.LinkedList;

public class Main {
    public static void main(String[] args){
        LinkedList<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(4);
        list.add(5);
        list.add(7);
        list.add(6);
        list.add(3);
        list.add(2);
        BST bst = new BST(list);
//        bst.print();
        // 4
        //    2
        //        1
        //        3
        //    6
        //        5
        //        7
    }
}
