package mg.core.scaffolding;

import lombok.Data;
import mg.core.Utils;

@Data
public class ScAttribute {
  private String importName;
  private String name;
  private Class<?> type;

  public String getNameCamelCase(boolean firstUpper) {
    return Utils.toCamelCase(getName(), firstUpper);
  }

  public String getNameCamelCase() {
    return getNameCamelCase(false);
  }

}
