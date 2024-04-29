package mg.itu.generator.view;

import mg.itu.database.Database;
import mg.itu.database.Mapping;
import mg.itu.database.Provider;
import mg.itu.database.Table;
import mg.itu.generator.Scaffolding;

import java.util.HashMap;

public class Router extends Scaffolding {
    public Router(HashMap<String, String> params, HashMap<String, String> languageParams, HashMap<String, Mapping> languageMappings, String packageName, Table table, Provider provider, Database database) {
        super(params, languageParams, languageMappings, packageName, table, provider, database);
    }

    public String processImport(String importName, String pathName) {
        String importCode = getLanguageParams().get("#import#");
        importCode = importCode.replace("#importName#", importName);
        importCode = importCode.replace("#importPath#", pathName);
        return importCode;
    }

    protected String processDepsRoute() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Table table: getDatabase().getTables()) {
            stringBuilder.append(processImport(
                    getParams().get("#import_name#")
                            .replace("#name#", table.getName()),
                    getParams().get("#import_path#")
                            .replace("#name#", table.getName())
            ));
        }
        return stringBuilder.toString();
    }

    private String processPaths() {
        StringBuilder paths = new StringBuilder();
        for (Table table: getDatabase().getTables()) {
            paths.append(getParams().get("#path#")
                    .replace("#name#", table.getName()));
        }
        return paths.toString();
    }

    @Override
    protected String process(String code) {
        return super.process(
                code
                        .replace("#imports#", processDepsRoute())
                        .replace("#paths#", processPaths())
        );
    }
}
