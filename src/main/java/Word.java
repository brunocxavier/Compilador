public class Word extends Token{
    private String lexeme = "";

    public static final Word and = new Word ("&&", Tag.AND);
    public static final Word or = new Word ("||", Tag.OR);
    public static final Word eq = new Word ("==", Tag.EQ);
    public static final Word ne = new Word ("!=", Tag.NE);
    public static final Word le = new Word ("<=", Tag.LE);
    public static final Word ge = new Word (">=", Tag.GE);

    public Word (String lexeme, int tag){
        super (tag);
        this.lexeme = lexeme;
    }

    public String toString(){
        return "" + lexeme;
    }

    public String getLexeme() {
        return lexeme;
    }
}
