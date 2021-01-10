import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DBTable<T> {
    private List<T> entries;

    public DBTable() {
        this.entries = new ArrayList<>();
    }

    public DBTable(Collection<T> lst) {
        entries = new ArrayList<>(lst);
    }

    public void add(T t) {
        entries.add(t);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DBTable<?> other = (DBTable<?>) o;
        return Objects.equals(entries, other.entries);
    }

    /** Add all items from a collection to the table. */
    public void add(Collection<T> col) {
        col.forEach(this::add);
    }

    /** Returns a copy of the entries in this table. */
    List<T> getEntries() {
        return new ArrayList<>(entries);
    }

    /**
     * Returns a list of entries sorted based on the natural ordering of the
     * results of the getter. Non-destructive.
     */
    public <R extends Comparable<R>> List<T> getOrderedBy(Function<T, R> getter) {
        return entries.stream().sorted(Comparator.comparing(getter::apply)).collect(Collectors.toList());
    }

    /**
     * Returns a list of entries whose value returned from the getter is found
     * in the whitelist. Non-destructive. 
     * Here is an example of the usage:
     *      
     *      List<User> users = Arrays.asList(
                new User(2, "Matt", ""),
                new User(4, "Zoe", ""),
                new User(5, "Alex", ""),
                new User(1, "Shreya", ""),
                new User(1, "Connor", "")
                );
     *      List<String> whiteListedNames = Arrays.asList("Connor", "Shreya");
     *      DBTable<User> t = new DBTable<>(users);
     *      List<User> whiteListedUsers = t.getWhitelisted(User::getName, whiteListedNames); // should contain only Users Connor and Shreya
     * 
     */
    public <R> List<T> getWhitelisted(Function<T, R> getter, Collection<R> whitelist) {
        return entries.stream()
                .filter(e -> whitelist.contains(getter.apply(e)))
                .collect(Collectors.toList());
    }

    /**
     * Returns a new DBTable that contains the elements as obtained by the
     * getter. For example, getting a DBTable of usernames would look like:
     * DBTable<String> names = table.getSubtableOf(User::getUsername);
     */
    public <R> DBTable<R> getSubtableOf(Function<T, R> getter) {
        DBTable table = new DBTable();
        entries.stream().map(getter).forEach(e -> table.add(e));
        return table;
    }

    public static void main(String[] args) {
        List<User> users = Arrays.asList(
                new User(2, "Matt", ""),
                new User(4, "Zoe", ""),
                new User(5, "Alex", ""),
                new User(1, "Shreya", ""),
                new User(1, "Connor", "")
                );
        DBTable<User> t = new DBTable<>(users);
        List<User> l = t.getOrderedBy(User::getName);
        l.forEach(System.out::println);
        DBTable<String> l2 = t.getSubtableOf(User::getName);
        System.out.print(l2.getEntries());
        List<String> whiteListedNames = Arrays.asList("Connor", "Shreya");
        DBTable<User> t2 = new DBTable<>(users);
        List<User> whiteListedUsers = t2.getWhitelisted(User::getName, whiteListedNames);
        whiteListedUsers.forEach(System.out::println);
    }
}
