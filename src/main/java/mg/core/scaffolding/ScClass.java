package mg.core.scaffolding;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;
import mg.core.Utils;
import mg.core.data.LangData;
import mg.core.data.Mapping;
import mg.database.Database;
import mg.database.Table;

@Data
@NoArgsConstructor
public class ScClass {
  private String packageName;
  private List<String> imports = new ArrayList<>();
  private String name;
  private List<ScAttribute> attributes = new ArrayList<>();
  private LangData langData;
  private String language;

  public ScClass(String language, Table table, Database database) {
    setName(table.getName());
    setLanguage(language);

    for (val column : table.getColumns()) {
      val map = Mapping.getMappedType(column.getType(),
                                      database.getDbData().getMappings());
      ScAttribute scAttribute = new ScAttribute();
      scAttribute.setName(column.getName());
      scAttribute.setType(map);
      scAttribute.setImportName(map.getName());
      addAttributes(scAttribute);
    }
  }

  public void addImport(String importName) {
    if (importName != null && !getImports().contains(importName))
      getImports().add(importName);
  }

  public void addAttributes(ScAttribute scAttribute) {
    getAttributes().add(scAttribute);
    addImport(scAttribute.getImportName());
  }

  public String getJsonConfig() {
    return getLanguage() + ".json";
  }

  public LangData getLangData() {
    if (langData == null)
      return LangData.getLangData(Utils.DATA_LANG_PATH + getJsonConfig());
    return langData;
  }

  public String getNameCamelCase() { return Utils.toCamelCase(getName()); }

  public String importToCode(String importName) {
    return "$import " + importName + "$end_line\n";
  }

  public String attributeToCode(ScAttribute scAttribute) {
    return "\t$private " + scAttribute.getType().getSimpleName() + " " + scAttribute.getNameCamelCase() + "$end_line\n";
  }

  public String importsToCode() {
    val imports = new StringBuilder();
    for (val importName : getImports())
      if (getLangData().getPreImported().stream().noneMatch(importName::startsWith))
        imports.append(importToCode(importName));
    return imports.toString();
  }

  public String attributesToCode() {
    val attributes = new StringBuilder();
    for (val scAttribute : getAttributes())
      attributes.append(attributeToCode(scAttribute));
    return attributes.toString();
  }

    public String getterToCode(ScAttribute scAttribute) {
        return "\t$public " + scAttribute.getType().getSimpleName() + " get" + scAttribute.getNameCamelCase(true) + "() ${\n" +
             "\t\t$return " + scAttribute.getNameCamelCase() + "$end_line\n" +
             "\t$}\n\n";
    }

    public String setterToCode(ScAttribute scAttribute) {
        return "\t$public void set" + scAttribute.getNameCamelCase() + "(" + scAttribute.getType().getSimpleName() + " " + scAttribute.getNameCamelCase() + ") ${\n" +
             "\t\t$this." + scAttribute.getNameCamelCase() + " = " + scAttribute.getNameCamelCase(true) + "$end_line\n" +
             "\t$}\n\n";
    }

  public String gettersAndSettersToCode() {
    val gettersAndSetters = new StringBuilder();
    for (val scAttribute : getAttributes()) {
      gettersAndSetters.append(getterToCode(scAttribute));
      gettersAndSetters.append(setterToCode(scAttribute));
    }
    return gettersAndSetters.toString();
  }

  public String evaluate(String template) {
    for (val toTranslate : getLangData().getTraductions().keySet()) {
      template = template.replace("$" + toTranslate,
              getLangData().getTraduction(toTranslate));
    }
    return template;
  }

  public String convert(String templateConversion, boolean withGettersAndSetters) {
    var template =
        Utils.readFile(Utils.DATA_TEMPLATES_PATH +
                       getLangData().getTemplate(templateConversion));

    template = template.replace(
            "#package#",
            getPackageName() == null ? "" : getPackageName());
    template = template.replace("#imports#", importsToCode());
    template = template.replace("#class#", getNameCamelCase());
    template = template.replace("#attributes#", attributesToCode());
    template = template.replace("#getters_and_setters#", withGettersAndSetters ? gettersAndSettersToCode() : "");

    return evaluate(template).trim();
  }

  public void generate(String path, String packageName, String templateConversion, boolean withGettersAndSetters) {
    setPackageName(packageName);
    path = Utils.getCorrectPath(path);
    Utils.writeFile(path + getNameCamelCase() + getLangData().getExtension(),
                    convert(templateConversion, withGettersAndSetters));
  }

  public void generate(String path, String templateConversion, boolean withGettersAndSetters) {
    generate(path, Utils.getPackageFromPath(path), templateConversion, withGettersAndSetters);
  }
}
