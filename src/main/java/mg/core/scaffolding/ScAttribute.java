package mg.core.scaffolding;

import mg.core.Utils;


public class ScAttribute {
  private String importName;
  private String name;
  private String langType;
  private String mappingType;


  // Constructors
  public ScAttribute() {
  }


  // Methods
  public String getName() {
    if(getLangType().equalsIgnoreCase("boolean"))
      if(name.startsWith("is"))
        return name.substring(2);
    return name;
  }

  public String getNameCamelCase(boolean firstUpper) {
    return Utils.toCamelCase(getName(), firstUpper);
  }

  public String getNameCamelCase() {
    return getNameCamelCase(false);
  }

  public String getGetterName() {
    if (getLangType().equalsIgnoreCase("boolean"))
      return "is" + getNameCamelCase(true);
    return "get" + getNameCamelCase(true);
  }

  public String getSetterName() {
    return "set" + getNameCamelCase(true);
  }


  // Getters and Setters
  public String getImportName() {
    return importName;
  }

  public void setImportName(String importName) {
    this.importName = importName;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLangType() {
    return langType;
  }

  public void setLangType(String langType) {
    this.langType = langType;
  }

  public String getMappingType() {
    return mappingType;
  }

  public void setMappingType(String mappingType) {
    this.mappingType = mappingType;
  }
}
