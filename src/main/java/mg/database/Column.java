package mg.database;

import lombok.Data;

@Data
public class Column {
  private String name;
  private String type;
  private boolean isNullable;
  private String defaultValue;
  private String comment;
}
