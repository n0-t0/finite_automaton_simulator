import java.util.HashMap;
import java.util.Map;

public class Alphabet {
    private Map<String, Char> pool = new HashMap<>();

    private static Alphabet alphabet = new Alphabet();
    private Alphabet() {}

    public static Alphabet getInstance() {
        return alphabet;
    }
    public Char getChar(String string) {
        Char character = (Char)pool.get(string);
        if(character == null) {
            character = Char.valueOf(string);
            pool.put(string, character);
        }
        return character;
    }
}

