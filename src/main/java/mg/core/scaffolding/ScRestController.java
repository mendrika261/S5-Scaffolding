package mg.core.scaffolding;

import mg.database.Database;
import mg.database.Table;

import java.util.ArrayList;
import java.util.List;

public class ScRestController extends ScClass {
    private List<ScAnnotation> annotations = new ArrayList<>();

    public ScRestController(String language, Table table, Database database, String objectImport, String objectServiceImport) {
        super(language, table, database);

        List<String> imports = new ArrayList<>();
        if (!objectImport.isEmpty() && !objectImport.isBlank())
            imports.add(objectImport + "." + getNameCamelCase());
        if (!objectServiceImport.isEmpty() && !objectServiceImport.isBlank())
            imports.add(objectServiceImport + "." + getNameCamelCase() + "Service");
        setImports(imports);
    }

    public static String annotationsToCode(List<ScAnnotation> annotations) {
        StringBuilder sb = new StringBuilder();
        for (ScAnnotation annotation : annotations) {
            sb.append("$annotation_start").append(annotation.getNom()).append("$annotation_end");
            if (annotation.getValeur() != null && !annotation.getValeur().isEmpty()) {
                sb.append("(").append(annotation.getValeur()).append(")");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String convert(String templateConversion, boolean withGettersAndSetters) {
        return super.convert(templateConversion, withGettersAndSetters)
                .replace("#className#", getName().toLowerCase());
    }

    @Override
    public String getFileName() {
        return getNameCamelCase() + "RestController" + getLangData().getExtension();
    }

    public List<ScAnnotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<ScAnnotation> annotations) {
        this.annotations = annotations;
    }
}
