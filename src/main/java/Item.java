public class Item {
    private String lexeme;
    private int level;
    private String value;

    public Item(String lexeme, int level, String value) {
        this.lexeme = lexeme;
        this.level = level;
        this.value = value;
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
        return value;
    }

    public void setType(String type) {
        this.value = type;
    }

    @Override
    public String toString() {
        return "Item{" +
                "lexeme='" + lexeme + '\'' +
                ", level=" + level +
                getValueString() +
                '}';
    }

    private String getValueString() {
        return !value.equals("") ? ", value=" + value : "";
    }
}
