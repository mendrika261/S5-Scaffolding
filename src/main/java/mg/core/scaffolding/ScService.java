package mg.core.scaffolding;

import mg.database.Database;
import mg.database.Table;

public class ScService extends ScClass {
    public ScService(String language, Table table, Database database) {
        super(language, table, database);
    }

    @Override
    public String getFileName() {
        return getNameCamelCase() + "Service" + getLangData().getExtension();
    }
}
