public class Node {
    private final String name;
    private Node(String name) {
        this.name = name;
    }

    public static Node valueOf(String name) {
        return new Node(name);
    }

    public String getString() {
        return this.name;
    }
}
