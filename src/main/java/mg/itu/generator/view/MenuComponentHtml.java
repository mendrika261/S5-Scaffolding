package mg.itu.generator.view;

import mg.itu.database.*;
import mg.itu.generator.Entity;
import mg.itu.utils.Format;

import java.util.HashMap;

public class MenuComponentHtml extends Entity {
    public MenuComponentHtml(HashMap<String, String> params, HashMap<String, String> languageParams, HashMap<String, Mapping> languageMappings, String packageName, Table table, Provider provider, Database database) {
        super(params, languageParams, languageMappings, packageName, table, provider, database);
    }

    private String processMenu() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Table table: getDatabase().getTables()) {
            stringBuilder.append(
                    getParams().get("#menu#")
                            .replace("#Name#", Format.toCamelCase(table.getName(), true))
                            .replace("#url#",
                                    getParams().get("#url#")
                                            .replace("#name#", table.getName())
                            )
            );
        }
        return stringBuilder.toString();
    }

    @Override
    protected String process(String code) {
        return super.process(
                code
                        .replace("#menus#", processMenu())
        );
    }
}
