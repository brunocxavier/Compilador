public class Token {
    public final int tag; //constante que representa o token

    public Token(int tag) {
        this.tag = tag;
    }

    public String toString() {
        return "" + tag;
    }

    public int getTag() {
        return tag;
    }
}