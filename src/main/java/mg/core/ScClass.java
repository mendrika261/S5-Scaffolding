package mg.core;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;
import mg.database.Database;
import mg.database.Table;

@Data
@NoArgsConstructor
public abstract class ScClass {
  private String packageName;
  private List<String> imports = new ArrayList<>();
  private String name;
  private List<ScAttributes> attributes = new ArrayList<>();
  private LangData langData;

  public ScClass(Table table, Database database) {
    setName(table.getName());

    for (val column : table.getColumns()) {
      val map = Mapping.getMappedType(column.getType(),
                                      database.getDbData().getMappings());
      ScAttributes scAttributes = new ScAttributes();
      scAttributes.setName(column.getName());
      scAttributes.setType(map);
      scAttributes.setImportName(map.getName());
      addAttributes(scAttributes);
    }
  }

  public void addImport(String importName) {
    if (importName != null && !getImports().contains(importName))
      getImports().add(importName);
  }

  public void addAttributes(ScAttributes scAttributes) {
    getAttributes().add(scAttributes);
    addImport(scAttributes.getImportName());
  }

  public abstract String getJsonConfig();

  public LangData getLangData() {
    if (langData == null)
      return LangData.getLangData(Utils.DATA_LANG_PATH + getJsonConfig());
    return langData;
  }

  public String getNameCamelCase() { return Utils.toCamelCase(getName()); }

  public String importsToString() {
    val imports = new StringBuilder();
    for (val importName : getImports())
      imports.append("$import ").append(importName).append(";\n");
    return imports.toString();
  }

  public String attributesToString() {
    val attributes = new StringBuilder();
    for (val scAttributes : getAttributes())
      attributes.append("\t")
          .append(scAttributes.getType().getSimpleName())
          .append(" ")
          .append(scAttributes.getName())
          .append(";\n");
    return attributes.toString();
  }

  public String convert(String templateConversion) {
    var template =
        Utils.readFile(Utils.DATA_TEMPLATES_PATH +
                       getLangData().getTemplate(templateConversion));

    for (val toTranslate : getLangData().getTraductions().keySet()) {
      template = template.replace(
          "#package#",
          getPackageName() == null ? "" : "$package " + getPackageName() + ";");
      template = template.replace("#imports#", importsToString());
      template = template.replace("#class#", getNameCamelCase());
      template = template.replace("#attributes#", attributesToString());

      template = template.replace("$" + toTranslate,
                                  getLangData().getTraduction(toTranslate));
    }

    return template.trim();
  }

  public void generate(String path, String templateConversion) {
    if (path == null || path.equals("."))
      path = "";
    if (!path.isEmpty() && !path.endsWith("/"))
      path += "/";
    Utils.writeFile(path + getNameCamelCase() + getLangData().getExtension(),
                    convert(templateConversion));
  }
}
