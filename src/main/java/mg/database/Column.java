package mg.database;


public class Column {
  private String name;
  private String type;
  private boolean isNullable;
  private String defaultValue;
  private String comment;
  private boolean isPrimaryKey;

  // Constructors
  public Column() {
  }


  // Getters and Setters
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public boolean isNullable() {
    return isNullable;
  }

  public void setNullable(boolean nullable) {
    isNullable = nullable;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public boolean isPrimaryKey() {
    return isPrimaryKey;
  }

  public void setPrimaryKey(boolean primaryKey) {
    isPrimaryKey = primaryKey;
  }
}
