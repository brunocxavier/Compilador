public class Item {
    private String lexeme;
    private int level;
    private String type;

    public Item(String lexeme, int level, String type) {
        this.lexeme = lexeme;
        this.level = level;
        this.type = type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Item{" +
                "lexeme='" + lexeme + '\'' +
                ", level=" + level +
                getTypeString() +
                '}';
    }

    private String getTypeString() {
        return !type.equals("") ? ", type=" + type : "";
    }
}
