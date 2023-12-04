package mg.core.data;


public class LangMapping {
    String mappingType;
    String equivalentType;
    String importPackage;

    // Constructors
    public LangMapping() {
    }


    // Getters and Setters
    public String getMappingType() {
        return mappingType;
    }

    public void setMappingType(String mappingType) {
        this.mappingType = mappingType;
    }

    public String getEquivalentType() {
        return equivalentType;
    }

    public void setEquivalentType(String equivalentType) {
        this.equivalentType = equivalentType;
    }

    public String getImportPackage() {
        return importPackage;
    }

    public void setImportPackage(String importPackage) {
        this.importPackage = importPackage;
    }
}
