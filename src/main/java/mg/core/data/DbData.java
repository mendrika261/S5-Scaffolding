package mg.core.data;

import lombok.Data;
import mg.core.Utils;

@Data
public class DbData {
  DbMapping[] mappings;

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
}
