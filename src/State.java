import java.util.HashMap;
import java.util.Map;

public class State {
    private Map<String, Node> pool = new HashMap<>();
    
    private static State state = new State();
    private State() {}
    public static State getInstance() {
        return state;
    }
    public Node getNode(String name) {
        Node node = (Node)pool.get(name);
        if(node == null) {
            node = Node.valueOf(name);
            pool.put(name, node);
        }
        return node;
    }
}
