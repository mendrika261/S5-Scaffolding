package mg.itu.generator.view;

import java.sql.Connection;
import java.util.HashMap;
import mg.itu.database.*;
import mg.itu.generator.Entity;
import mg.itu.utils.Format;

public class FormComponentHtml extends Entity {
  public FormComponentHtml(HashMap<String, String> params,
                           HashMap<String, String> languageParams,
                           HashMap<String, Mapping> languageMappings,
                           String packageName, Table table, Provider provider,
                           Database database) {
    super(params, languageParams, languageMappings, packageName, table,
          provider, database);
  }

  @Override
  protected String processAttributes() {
    final StringBuilder attributeCode = new StringBuilder();
    for (Column column : getTable().getColumns()) {
      if (column.isPrimaryKey())
        continue;
      if (column.isForeignKey()) {
        String attributePart;
        attributePart = getParams().get("#select#");
        StringBuilder optionList = new StringBuilder();
        String options = getParams().get("#options#");

        Connection connection = getDatabase().getConnection(getProvider());
        Table table = getDatabase().getTable(column.getTableName(), connection);
        options =
            options
                .replace("#FKname#",
                         Format.toCamelCase(column.getTableName(), false))
                .replace(
                    "#FKprimaryKey#",
                    Format.toCamelCase(table.getPrimaryKey().getName(), false))
                .replace("#FKdescription#",
                         Format.toCamelCase(table.getColumns().get(1).getName(),
                                            false));

        attributeCode.append(
            attributePart
                .replace("#name#", Format.toCamelCase(column.getName(), false))
                .replace("#Name#", Format.toCamelCase(column.getName(), true))
                .replace("#FKColumnName#",
                         Format.toCamelCase(column.getName(), false))
                .replace("#tabname#", getTable().getName())
                .replace("#options#", options));
      } else {
        String attributePart;
        attributePart = getParams().get("#input#");
        attributeCode.append(
            attributePart
                .replace("#type#", getMapping(column.getType()).getType())
                .replace("#Name#", Format.toCamelCase(column.getName(), true))
                .replace("#name#",
                         Format.toCamelCase(column.getName(), false)));
      }
    }
    return attributeCode.toString().strip();
  }
}
