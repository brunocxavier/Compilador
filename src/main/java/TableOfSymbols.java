import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TableOfSymbols {
    private List<Item> table;
    private TableOfSymbols topTable;

    public TableOfSymbols(TableOfSymbols topTable) {
        this.table = new ArrayList<>();
        this.topTable = topTable;
    }

    public TableOfSymbols() {
        this.table = new ArrayList<>();
        this.topTable = null;
    }

    public void add(Item item) {
        this.table.add(item);
    }

    public void remove(int level) {
        this.table = this.table.stream().filter(item -> item.getLevel() != level).collect(Collectors.toList());
    }

    public boolean exists(String lexeme) {
        Optional<Item> itemOp = this.table.stream().filter(item -> lexeme.equals(item.getLexeme())).findAny();
        return itemOp.isPresent() ||  existsInTop(lexeme);
    }

    private boolean existsInTop(String lexeme) {
        return topTable != null && topTable.exists(lexeme);
    }

    public List<Item> getTable() {
        return table;
    }
}
