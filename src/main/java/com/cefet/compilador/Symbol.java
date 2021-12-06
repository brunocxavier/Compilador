package com.cefet.compilador;

public class Symbol {
    private String lexeme;
    private int level;
    private String value;

    public Symbol(String lexeme, int level, String value) {
        this.lexeme = lexeme;
        this.level = level;
        this.value = value;
    }

    public String getLexeme() {
        return lexeme;
    }

    @Override
    public String toString() {
        return "Symbol{" +
                "lexeme='" + lexeme + '\'' +
                ", level=" + level +
                ", value='" + value + '\'' +
                '}';
    }

    private String getValueString() {
        return !value.equals("") ? ", value=" + value : "";
    }
}
