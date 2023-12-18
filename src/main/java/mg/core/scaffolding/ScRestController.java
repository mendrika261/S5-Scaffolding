package mg.core.scaffolding;

import mg.database.Database;
import mg.database.Table;

public class ScRestController extends ScClass {
    public ScRestController(String language, Table table, Database database) {
        super(language, table, database);
    }

    @Override
    public String convert(String templateConversion, boolean withGettersAndSetters) {
        return super.convert(templateConversion, withGettersAndSetters)
                .replace("#className#", getName().toLowerCase());
    }

    @Override
    public String getFileName() {
        return getNameCamelCase() + "RestController" + getLangData().getExtension();
    }
}
