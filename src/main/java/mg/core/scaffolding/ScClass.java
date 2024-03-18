package mg.core.scaffolding;

import java.util.ArrayList;
import java.util.List;

import mg.core.Utils;
import mg.core.data.DbMapping;
import mg.core.data.LangData;
import mg.core.data.LangMapping;
import mg.core.data.Template;
import mg.database.Column;
import mg.database.Database;
import mg.database.Table;


public class ScClass {
  private Table table;
  private String packageName;
  private List<String> imports = new ArrayList<>();
  private String name;
  private List<ScAttribute> attributes = new ArrayList<>();
  private LangData langData;
  private String language;
  private Database database;

  // Constructors
  public ScClass() {
  }

  public ScClass(String language, Table table, Database database) {
    setTable(table);
    setName(getTable().getName());
    setLanguage(language);
    setDatabase(database);

    for (Column column : getTable().getColumns()) {
      final DbMapping map = database.getDbData().getMappedType(column.getType());
      final LangMapping langMap = getLangData().getMappedType(map.getMappingType());
      ScAttribute scAttribute = new ScAttribute();
      scAttribute.setName(column.getName());
      scAttribute.setLangType(langMap.getEquivalentType());
      scAttribute.setMappingType(map.getMappingType());
      scAttribute.setImportName(langMap.getImportPackage());
      scAttribute.setPrimaryKey(column.isPrimaryKey());
      addAttributes(scAttribute);
    }
  }


  // Methods
  public void addImport(String importName) {
    if (importName != null && !importName.isEmpty() && !getImports().contains(importName))
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
      setLangData(LangData.getLangData(Utils.DATA_LANG_PATH + getJsonConfig()));
    return langData;
  }

  public String getNameCamelCase() { return Utils.toCamelCase(getName()); }

  public String importToCode(String importName) {
    return "$import " + importName + "$end_line\n";
  }

  public String attributeToCode(ScAttribute scAttribute) {
    return "\t$private " + scAttribute.getLangType() + " " + scAttribute.getNameCamelCase() + "$end_line\n";
  }

  public String importsToCode() {
    final StringBuilder imports = new StringBuilder();
    for (String importName : getImports())
      if (!importName.isBlank() && getLangData().getPreImported().stream().noneMatch(importName::startsWith))
        imports.append(importToCode(importName));
    return imports.toString();
  }

  public String attributesToCode() {
    final StringBuilder attributes = new StringBuilder();
    for (ScAttribute scAttribute : getAttributes())
      attributes.append(attributeToCode(scAttribute));
    return attributes.toString();
  }

  public String getterToCode(ScAttribute scAttribute) {
    return "\t$public " + scAttribute.getLangType() + " " + scAttribute.getGetterName() + "() ${\n" +
            "\t\t$return " + scAttribute.getNameCamelCase() + "$end_line\n" +
            "\t$}\n\n";
  }

    public String setterToCode(ScAttribute scAttribute) {
      return "\t$public $void " + scAttribute.getSetterName() + "(" + scAttribute.getLangType() + " " + scAttribute.getNameCamelCase() + ") ${\n" +
             "\t\t$this$." + scAttribute.getNameCamelCase() + " = " + scAttribute.getNameCamelCase() + "$end_line\n" +
             "\t$}\n\n";
    }

  public String gettersAndSettersToCode() {
    final StringBuilder gettersAndSetters = new StringBuilder();
    for (ScAttribute scAttribute : getAttributes()) {
      gettersAndSetters.append(getterToCode(scAttribute));
      gettersAndSetters.append(setterToCode(scAttribute));
    }
    return gettersAndSetters.toString();
  }

  private String packageToCode() {
    if (getPackageName() == null || getPackageName().isEmpty())
      return "";
    return "$package " + getPackageName() + "$end_line\n\n";
  }

  public String evaluate(String template) {
    for (String toTranslate : getLangData().getTraductions().keySet()) {
      template = template.replace("$" + toTranslate,
              getLangData().getTraduction(toTranslate));
    }
    return template;
  }

  public String convert(String templateConversion, boolean withGettersAndSetters) {
    String template =
        Utils.readFile(Utils.DATA_TEMPLATES_PATH +
                       getLangData().getTemplateFile(templateConversion));

    template = template.replace("#package#", packageToCode());
    template = template.replace("#imports#", importsToCode());
    template = template.replace("#class#", getNameCamelCase());
    template = template.replace("#className#", getName().toLowerCase());
    template = template.replace("#attributes#", attributesToCode());
    template = template.replace("#getters_and_setters#", withGettersAndSetters ? gettersAndSettersToCode() : "");
    template = template.replace("#idType#",
                    getLangData().getMappedType(getDatabase().getDbData()
                            .getMappedType(getTable().getPrimaryKey().getType()).getMappingType()).getMappingType());

    return evaluate(template).trim();
  }

  public String getFileName() {
    return getNameCamelCase() + getLangData().getExtension();
  }

  public void generate(String path, String packageName, Template templateConversion, boolean withGettersAndSetters) {
    setPackageName(packageName);
    path = Utils.getCorrectPath(path);
    Utils.writeFile(path + getFileName(), convert(templateConversion.getTemplateFile(), withGettersAndSetters));
  }

  public void generate(String path, Template templateConversion, boolean withGettersAndSetters) {
    generate(path, Utils.getPackageFromPath(path), templateConversion, withGettersAndSetters);
  }


  // Getters and Setters
  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public List<String> getImports() {
    return imports;
  }

  public void setImports(List<String> imports) {
    this.imports = imports;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<ScAttribute> getAttributes() {
    return attributes;
  }

  public void setAttributes(List<ScAttribute> attributes) {
    this.attributes = attributes;
  }

  public void setLangData(LangData langData) {
    this.langData = langData;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public Table getTable() {
    return table;
  }

  public void setTable(Table table) {
    this.table = table;
  }

  public Database getDatabase() {
    return database;
  }

  public void setDatabase(Database database) {
    this.database = database;
  }
}
