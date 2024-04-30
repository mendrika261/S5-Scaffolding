package mg.itu.database;

import java.util.HashMap;

public class Provider {
  private String driver;
  private HashMap<String, String> mappings;

  // Getters and Setters
  public String getDriver() { return driver; }

  public void setDriver(String driver) { this.driver = driver; }

  public HashMap<String, String> getMappings() { return mappings; }

  public void setMappings(HashMap<String, String> mappings) {
    this.mappings = mappings;
  }
}
