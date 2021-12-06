package com.cefet.compilador;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TableOfSymbols {
    private List<Symbol> table;
    private TableOfSymbols topTable;

    public TableOfSymbols(TableOfSymbols topTable) {
        this.table = new ArrayList<>();
        this.topTable = topTable;
    }

    public TableOfSymbols() {
        this.table = new ArrayList<>();
        this.topTable = null;
    }

    public void add(Symbol symbol) {
        this.table.add(symbol);
    }

    public boolean exists(String lexeme) {
        Optional<Symbol> itemOp = this.table.stream().filter(symbol -> lexeme.equals(symbol.getLexeme())).findAny();
        return itemOp.isPresent() || existsInTop(lexeme);
    }

    private boolean existsInTop(String lexeme) {
        return topTable != null && topTable.exists(lexeme);
    }

    public List<Symbol> getTable() {
        return table;
    }
}
