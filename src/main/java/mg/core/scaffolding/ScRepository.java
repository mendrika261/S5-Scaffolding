package mg.core.scaffolding;


import mg.database.Database;
import mg.database.Table;

public class ScRepository extends ScClass {
    public ScRepository(String language, Table table, Database database) {
        super(language, table, database);
    }

    @Override
    public String getFileName() {
        return getNameCamelCase() + "Repository" + getLangData().getExtension();
    }

    @Override
    public String convert(String templateConversion, boolean withGettersAndSetters) {
        return super.convert(templateConversion, withGettersAndSetters)
                .replace("#extends#", "$extends $repository_import");
    }
}
