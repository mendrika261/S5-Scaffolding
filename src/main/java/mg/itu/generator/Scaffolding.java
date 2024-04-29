package mg.itu.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mg.itu.database.Database;
import mg.itu.database.Mapping;
import mg.itu.database.Provider;
import mg.itu.database.Table;
import mg.itu.utils.File;
import mg.itu.utils.Format;

public abstract class Scaffolding {
  private String template;
  private HashMap<String, String> params;
  private HashMap<String, String> languageParams;
  private HashMap<String, Mapping> languageMappings;
  private Provider provider;
  private String packageName;
  private List<String> imports = new ArrayList<>();
  private Table table;
  private static String TEMPLATE_PATH;
  private Database database;

  public Scaffolding(HashMap<String, String> params,
                     HashMap<String, String> languageParams,
                     HashMap<String, Mapping> languageMappings,
                     String packageName, Table table, Provider provider,
                     Database database) {
    setParams(params);
    setLanguageParams(languageParams);
    setLanguageMappings(languageMappings);
    // setPackageName(Format.joinPackageName(packageName,
    // params.getOrDefault("default_package", "")));
    setPackageName(packageName);
    setTable(table);
    setProvider(provider);
    setDatabase(database);
    processDeps();
  }

  protected void processDeps(String packageName,
                             String name) { // eg: entity_package, #ClassName#
    String depsImport = getParams().getOrDefault(packageName, null);
    if (depsImport != null)
      addImport(Format.joinPackageName(
          getPackageName(), depsImport,
          getParams().getOrDefault(name, "#ClassName#")));
  }

  protected void processDeps() {
    // Override this method to add imports
  }

  private String processPackage() {
    return ((getPackageName() != null && !getPackageName().isEmpty()) ||
            getParams().containsKey("default_package"))
        ? getLanguageParams()
              .getOrDefault("#package#", "")
              .replace("#packageName#",
                       Format.joinPackageName(
                           getPackageName(),
                           getParams().getOrDefault("default_package", "")))
        : "";
  }

  private String processImports() {
    StringBuilder importCode = new StringBuilder();
    for (String importName : getImports()) {
      importCode.append(getLanguageParams()
                            .get("#import#")
                            .replace("#importName#", importName));
    }
    return importCode.toString();
  }

  private String processPrimaryKeyType() {
    final Mapping mapping = getMapping(getTable().getPrimaryKey().getType());
    if (mapping == null)
      return getTable().getPrimaryKey().getType();
    addImport(mapping.getToImport());
    return mapping.getType();
  }

  private String processClassAnnotation() {
    addImport(getParams().getOrDefault("class_import_annotation", null));
    return getParams().getOrDefault("#class_annotation#", "");
  }

  protected String process(String code) {
    return code.replace("#package#", processPackage())
        .replace("#class_annotation#", processClassAnnotation())
        .replace("#primaryKeyType#", processPrimaryKeyType())
        .replace(
            "#primaryKeyName#",
            Format.toCamelCase(getTable().getPrimaryKey().getName(), false))
        // order is important
        .replace("#imports#", processImports())
        .replace("#className#", Format.toCamelCase(getTable().getName(), false))
        .replace("#ClassName#", Format.toCamelCase(getTable().getName(), true));
  }

  private String evaluate(String code) {
    for (String key : getParams().keySet()) {
      code = code.replace("$" + key, getParams().get(key)).strip();
    }
    for (String key : getLanguageParams().keySet()) {
      code = code.replace("$" + key, getLanguageParams().get(key)).strip();
    }
    return code;
  }

  public void generate(String path) {
    String code = File.getFileContentAsString(getTemplatePath() +
                                              getParams().get("template"));
    code = evaluate(process(code));

    File.writeToFile(
        path +
            Format.toPathNameWithTrail(
                process(getParams().getOrDefault("default_package", ""))) +
            getParams()
                .get("file_name")
                .replace("#Name#",
                         Format.toCamelCase(getTable().getName(), true))
                .replace("#name#",
                         Format.toCamelCase(getTable().getName(), false))
                .replace("#extension#", getLanguageParams().get("extension")),
        code);
  }

  public Mapping getMapping(String type) {
    final Mapping mapping = getLanguageMappings().getOrDefault(
        getStandardType(type), getLanguageMappings().get("default"));
    if (mapping == null)
      return new Mapping();
    return mapping;
  }

  public String getStandardType(String type) {
    return getProvider().getMappings().getOrDefault(
        type, getProvider().getMappings().get("default"));
  }

  public void addImport(String importNames) {
    if (importNames == null || importNames.isEmpty())
      return;
    for (String importName : importNames.split(",")) {
      if (importName != null && !importName.isEmpty() &&
          !getImports().contains(importName)) {
        getImports().add(importName);
      }
    }
  }

  // Getters and setters
  public String getTemplate() { return template; }

  public void setTemplate(String template) { this.template = template; }

  public static String getTemplatePath() { return TEMPLATE_PATH; }

  public static void setTemplatePath(String templatePath) {
    TEMPLATE_PATH = templatePath;
  }

  public HashMap<String, String> getParams() { return params; }

  public void setParams(HashMap<String, String> params) {
    this.params = params;
  }

  public HashMap<String, String> getLanguageParams() { return languageParams; }

  public void setLanguageParams(HashMap<String, String> languageParams) {
    this.languageParams = languageParams;
  }

  public String getPackageName() { return packageName; }

  public String getPackageNameWithoutTheLast() {
    // remove the last package name if exists
    int last = getPackageName().lastIndexOf(".");
    if (last == -1)
      return "";
    return getPackageName().substring(0, last);
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public Table getTable() { return table; }

  public void setTable(Table table) { this.table = table; }

  public List<String> getImports() { return imports; }

  public void setImports(List<String> imports) { this.imports = imports; }

  public HashMap<String, Mapping> getLanguageMappings() {
    return languageMappings;
  }

  public void setLanguageMappings(HashMap<String, Mapping> languageMappings) {
    this.languageMappings = languageMappings;
  }

  public Provider getProvider() { return provider; }

  public void setProvider(Provider provider) { this.provider = provider; }

  public Database getDatabase() { return database; }

  public void setDatabase(Database database) { this.database = database; }
}
