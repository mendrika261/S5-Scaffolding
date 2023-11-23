package mg.database;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Table {
    private String name;
    private List<Column> columns = new ArrayList<>();
    private boolean isMutable = true;
}
