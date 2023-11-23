package mg.core;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class LangData {
    private String extension;
    private List<String> preImported;
    private HashMap<String, String> templates;
    private HashMap<String, String> traductions;

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
}
