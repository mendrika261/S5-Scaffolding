package mg.core.scaffolding;

import mg.database.Database;
import mg.database.Table;

public class ScEntity extends ScClass {
    public ScEntity(String language, Table table, Database database) {
        super(language, table, database);
        addImport("$entity_import_annotation");
    }

    @Override
    public String attributeToCode(ScAttribute scAttribute) {
        String primaryKeyAnnotation = "";
        if(scAttribute.isPrimaryKey()) {
            addImport("$entity_id_import_annotation");
            primaryKeyAnnotation = "\t$annotation_start$entity_id_annotation$annotation_end\n";
        }
        return primaryKeyAnnotation + super.attributeToCode(scAttribute);
    }

    @Override
    public String convert(String templateConversion, boolean withGettersAndSetters) {
        return super.convert(templateConversion, withGettersAndSetters);
    }
}
