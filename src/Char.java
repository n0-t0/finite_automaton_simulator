public class Char {
    private final String string;
    Char(String string) {
        this.string = string;
    }
    public static Char valueOf(String string) {
        return new Char(string);
    }
    public String getString() {
        return this.string;
    }
    
}
