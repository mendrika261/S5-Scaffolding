package mg.core.data;

import lombok.Data;

@Data
public class Mapping {
  private String[] databaseType;
  private String javaType;

  public static Class<?> getMappedType(String type, Mapping[] mappings) {
    for (Mapping mapping : mappings) {
      for (String databaseType : mapping.getDatabaseType()) {
        if (databaseType.equals(type)) {
          try {
            return Class.forName(mapping.getJavaType());
          } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to find class: " +
                                       mapping.getJavaType());
          }
        }
      }
    }
    throw new RuntimeException("Type: " + type + " is not yet supported.");
  }
}
