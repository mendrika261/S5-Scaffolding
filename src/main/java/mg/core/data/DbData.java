package mg.core.data;

import mg.core.Utils;


public class DbData {
  private DbMapping[] mappings;

  // Constructors
  public DbData() {
  }


  // Methods
  public static DbData getDbData(String path) {
    try {
      return Utils.readFileJson(path, DbData.class);
    } catch (Exception e) {
      throw new RuntimeException("DbData file not found in: " + path);
    }
  }

  public DbMapping getMappedType(String type) {
    for (DbMapping dbMapping : getMappings()) {
      for (String databaseType : dbMapping.getDatabaseType()) {
        if (databaseType.equals(type)) {
          return dbMapping;
        }
      }
    }
    throw new RuntimeException("Type: " + type + " is not yet supported.");
  }


  // Getters and setters
  public DbMapping[] getMappings() {
    return mappings;
  }

  public void setMappings(DbMapping[] mappings) {
    this.mappings = mappings;
  }
}
