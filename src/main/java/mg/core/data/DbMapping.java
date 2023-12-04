package mg.core.data;

public class DbMapping {
  private String[] databaseType;
  private String mappingType;

  // Constructors
  public DbMapping() {}

  // Getters and setters
  public String[] getDatabaseType() { return databaseType; }

  public void setDatabaseType(String[] databaseType) {
    this.databaseType = databaseType;
  }

  public String getMappingType() { return mappingType; }

  public void setMappingType(String mappingType) {
    this.mappingType = mappingType;
  }
}
