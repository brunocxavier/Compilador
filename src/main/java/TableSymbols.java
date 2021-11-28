import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TableSymbols {
    private List<Item> table = new ArrayList<>();

    public void add(Item item) {
        this.table.add(item);
    }

    public void remove(int level) {
        this.table = this.table.stream().filter(item -> item.getLevel() != level).collect(Collectors.toList());
    }

    public boolean exists(String lexeme, int level) {
        Optional<Item> itemOp = this.table.stream().filter(item -> lexeme.equals(item.getLexeme()) && level == item.getLevel()).findAny();
        return itemOp.isPresent();
    }

    public List<Item> getTable() {
        return table;
    }
}
