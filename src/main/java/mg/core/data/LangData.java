package mg.core.data;

import java.util.HashMap;
import java.util.List;
import mg.core.Utils;

public class LangData {
  private String extension;
  private List<String> preImported;
  private HashMap<String, String> templates;
  private HashMap<String, String> traductions;
  private List<LangMapping> mappings;

  // Constructors
  public LangData() {}

  // Methods
  public static LangData getLangData(String path) {
    return Utils.readFileJson(path, LangData.class);
  }

  public String getTraduction(String key) {
    if (getTraductions().containsKey(key))
      return getTraductions().get(key);
    throw new RuntimeException("Traduction not found for key: " + key);
  }

  public String getTemplate(String key) {
    if (getTemplates().containsKey(key))
      return getTemplates().get(key);
    throw new RuntimeException("Template not found for key: " + key);
  }

  public LangMapping getMappedType(String map) {
    for (LangMapping langMapping : getMappings()) {
      if (langMapping.getMappingType().equalsIgnoreCase(map)) {
        return langMapping;
      }
    }
    throw new RuntimeException("Langage map type not found for: " + map);
  }

  // Getters and Setters
  public String getExtension() { return extension; }

  public void setExtension(String extension) { this.extension = extension; }

  public List<String> getPreImported() { return preImported; }

  public void setPreImported(List<String> preImported) {
    this.preImported = preImported;
  }

  public HashMap<String, String> getTemplates() { return templates; }

  public void setTemplates(HashMap<String, String> templates) {
    this.templates = templates;
  }

  public HashMap<String, String> getTraductions() { return traductions; }

  public void setTraductions(HashMap<String, String> traductions) {
    this.traductions = traductions;
  }

  public List<LangMapping> getMappings() { return mappings; }

  public void setMappings(List<LangMapping> mappings) {
    this.mappings = mappings;
  }
}
