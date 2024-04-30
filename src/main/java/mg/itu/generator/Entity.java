package mg.itu.generator;

import java.util.HashMap;
import mg.itu.database.*;
import mg.itu.utils.Format;

public class Entity extends Scaffolding {
  public Entity(HashMap<String, String> params,
                HashMap<String, String> languageParams,
                HashMap<String, Mapping> languageMappings, String packageName,
                Table table, Provider provider, Database database) {
    super(params, languageParams, languageMappings, packageName, table,
          provider, database);
  }

  protected String processAttributes() {
    final StringBuilder attributeCode = new StringBuilder();
    for (Column column : getTable().getColumns()) {
      String attributePart = "";
      if (column.isPrimaryKey()) {
        attributePart +=
            getParams().getOrDefault("#primary_attribute_annotation#", "");
        addImport(getParams().getOrDefault(
            "primary_attribute_import_annotation", null));
        attributePart +=
            getParams().getOrDefault("#primary_auto_generated_annotation#", "");
        addImport(getParams().getOrDefault(
            "primary_auto_generated_import_annotation", null));
      } else {
        attributePart += getParams().getOrDefault("#attribute_annotation#", "");
        addImport(
            getParams().getOrDefault("attribute_import_annotation", null));
      }
      attributePart += getLanguageParams().get("#attribute#");
      attributePart =
          attributePart
              .replace("#type#", getMapping(column.getType()).getType())
              .replace("#name#", Format.toCamelCase(column.getName(), false))
              .replace("#column_name#", column.getName());
      attributeCode.append(attributePart);

      String toImport = getMapping(column.getType()).getToImport();
      if (toImport != null && !toImport.isEmpty())
        addImport(getMapping(column.getType()).getToImport());
    }
    return attributeCode.toString();
  }

  private String processGetter(Column column) {
    return getLanguageParams()
        .getOrDefault((getStandardType(column.getType()).equals("boolean"))
                          ? "#getter_boolean#"
                          : "#getter#",
                      "")
        .replace("#type#", getMapping(column.getType()).getType())
        .replace("#name#", Format.toCamelCase(column.getName(), false))
        .replace("#Name#", Format.toCamelCase(column.getName(), true));
  }

  private String processSetter(Column column) {
    return getLanguageParams()
        .getOrDefault("#setter#", "")
        .replace("#type#", getMapping(column.getType()).getType())
        .replace("#name#", Format.toCamelCase(column.getName(), false))
        .replace("#Name#", Format.toCamelCase(column.getName(), true));
  }

  private String processGettersAndSetters() {
    StringBuilder code = new StringBuilder();
    for (Column column : getTable().getColumns()) {
      code.append(processGetter(column));
      code.append(processSetter(column));
      code.append("\n");
    }
    return code.toString();
  }

  private String processExtends() {
    addImport(getParams().getOrDefault("extends_import", null));
    return getParams().getOrDefault("#extends#", "");
  }

  @Override
  protected String process(String code) {
    return super.process(
        code.replace("#attributes#", processAttributes())
            .replace("#getters_and_setters#", processGettersAndSetters())
            .replace("#extends#", processExtends()));
  }
}
