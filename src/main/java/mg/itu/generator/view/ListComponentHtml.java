package mg.itu.generator.view;

import mg.itu.database.*;
import mg.itu.generator.Entity;
import mg.itu.utils.Format;

import java.util.HashMap;

public class ListComponentHtml extends Entity {
    public ListComponentHtml(HashMap<String, String> params, HashMap<String, String> languageParams, HashMap<String, Mapping> languageMappings, String packageName, Table table, Provider provider, Database database) {
        super(params, languageParams, languageMappings, packageName, table, provider, database);
    }

    private String processListColumn() {
        StringBuilder sb = new StringBuilder();
        for (Column attribute : getTable().getColumns()) {

            sb.append(getLanguageParams().get("#column#").replace("#name#",
                        Format.toCamelCase(attribute.getName(), true)));


        }
        return sb.toString().strip();
    }

    protected String processAttributes() {
        final StringBuilder attributeCode = new StringBuilder();
        for (Column column : getTable().getColumns()) {
            String attributePart = "";
            if(column.isPrimaryKey()) {
                attributePart += getParams().getOrDefault("#primary_attribute_annotation#", "");
                addImport(getParams().getOrDefault("primary_attribute_import_annotation", null));
                attributePart += getParams().getOrDefault("#primary_auto_generated_annotation#", "");
                addImport(getParams().getOrDefault("primary_auto_generated_import_annotation", null));
            }
            if(column.isForeignKey())
            {
                attributePart += getLanguageParams().get("#attributeFK#")
                        .replace("#FKtablename#",Format.toCamelCase(column.getTableName(),false))
                        .replace("#type#", getMapping(column.getType()).getType())
                        .replace("#name#", Format.toCamelCase(column.getName(), false));
                attributeCode.append(attributePart);
            }
            else
            {
                attributePart += getLanguageParams().get("#attribute#")
                        .replace("#type#", getMapping(column.getType()).getType())
                        .replace("#name#", Format.toCamelCase(column.getName(), false));
                attributeCode.append(attributePart);
            }


            String toImport = getMapping(column.getType()).getToImport();
            if (toImport != null && !toImport.isEmpty())
                addImport(getMapping(column.getType()).getToImport());
        }
        return attributeCode.toString();
    }

    @Override
    protected String process(String code) {
        return super.process(
                code
                        .replace("#list_column#", processListColumn())
        );
    }
}
