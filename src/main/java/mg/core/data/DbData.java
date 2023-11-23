package mg.core.data;

import lombok.Data;
import mg.core.Utils;

@Data
public class DbData {
  Mapping[] mappings;

  public static DbData getDbData(String path) {
    return Utils.readFileJson(path, DbData.class);
  }
}
