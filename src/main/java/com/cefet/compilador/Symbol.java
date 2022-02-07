package com.cefet.compilador;

import com.cefet.compilador.enums.Type;

public class Symbol {
    private String lexeme;
    private int level;
    private String value;
    private Type type;

    public Symbol(String lexeme, int level, String value, Type type) {
        this.lexeme = lexeme;
        this.level = level;
        this.value = value;
        this.type = type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Symbol{" +
                "lexeme='" + lexeme + '\'' +
                ", level=" + level +
                ", value='" + value + '\'' +
                ", type=" + type +
                '}';
    }
}
