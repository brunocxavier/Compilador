package com.cefet.compilador;

public class NumInt extends Token{
    public final int value;

    public NumInt(int value){
        super(Tag.INT);
        this.value = value;
    }

    public String toString(){
        return "<" + value + "," + tag + ">";
    }

    public int getValue() {
        return value;
    }
}
