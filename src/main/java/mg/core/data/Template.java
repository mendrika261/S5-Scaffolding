package mg.core.data;

public class Template {
    private String name;
    private String templateFile;
    private String engine;

    // Constructor
    public Template() {}

    public Template(String name, String templateFile, String engine) {
        setName(name);
        setTemplateFile(templateFile);
        setEngine(engine);
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(String templateFile) {
        this.templateFile = templateFile;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }
}
