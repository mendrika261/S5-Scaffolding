package mg.itu.database;

import java.util.HashMap;

public class Config {
  private HashMap<String, Provider> providers;
  private HashMap<String, Database> databases;
  private HashMap<String, HashMap<String, String>> languages;
  private HashMap<String, HashMap<String, HashMap<String, String>>> generations;
  private HashMap<String, HashMap<String, Mapping>> mappings;
  private String templatePath;

  // Getters and Setters
  public HashMap<String, Provider> getProviders() { return providers; }

  public void setProviders(HashMap<String, Provider> providers) {
    this.providers = providers;
  }

  public Database getConnexion(String name) { return databases.get(name); }

  public HashMap<String, Database> getDatabases() { return databases; }

  public void setDatabases(HashMap<String, Database> databases) {
    this.databases = databases;
  }

  public HashMap<String, HashMap<String, String>> getLanguages() {
    return languages;
  }

  public void setLanguages(HashMap<String, HashMap<String, String>> languages) {
    this.languages = languages;
  }

  public HashMap<String, HashMap<String, HashMap<String, String>>>
  getGenerations() {
    return generations;
  }

  public void setGenerations(
      HashMap<String, HashMap<String, HashMap<String, String>>> generations) {
    this.generations = generations;
  }

  public String getTemplatePath() { return templatePath; }

  public void setTemplatePath(String templatePath) {
    this.templatePath = templatePath;
  }

  public HashMap<String, HashMap<String, Mapping>> getMappings() {
    return mappings;
  }

  public void setMappings(HashMap<String, HashMap<String, Mapping>> mappings) {
    this.mappings = mappings;
  }

  @Override
  public String toString() {
    return "Config{"
        + "connexions=" + getDatabases() + '}';
  }
}
