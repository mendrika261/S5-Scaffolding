package mg.core;

import lombok.Data;

@Data
public class DbData {
  Mapping[] mappings;

  public static DbData getDbData(String path) {
    return Utils.readFileJson(path, DbData.class);
  }
}
