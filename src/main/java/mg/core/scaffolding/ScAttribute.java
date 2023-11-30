package mg.core.scaffolding;

import lombok.Data;
import mg.core.Utils;

@Data
public class ScAttribute {
  private String importName;
  private String name;
  private String type;

  public String getName() {
    if(getType().equals("boolean"))
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
    if (getType().equalsIgnoreCase("boolean"))
      return "is" + getNameCamelCase(true);
    return "get" + getNameCamelCase(true);
  }

  public String getSetterName() {
    return "set" + getNameCamelCase(true);
  }
}
