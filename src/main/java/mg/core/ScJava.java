package mg.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import mg.database.Database;
import mg.database.Table;

@Data
@EqualsAndHashCode(callSuper = true)
public class ScJava extends ScClass {
    private final String jsonConfig = "java.json";

    public ScJava(Table table, Database database) {
        super(table, database);
    }

    @Override
    public void generate(String path, String templateConversion) {
        setPackageName(path.replace("/", "."));
        super.generate(path, templateConversion);
    }
}
