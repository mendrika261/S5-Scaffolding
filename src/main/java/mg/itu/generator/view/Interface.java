package mg.itu.generator.view;

import mg.itu.database.Database;
import mg.itu.database.Mapping;
import mg.itu.database.Provider;
import mg.itu.database.Table;
import mg.itu.generator.Entity;
import mg.itu.generator.Scaffolding;

import java.util.HashMap;

public class Interface extends Entity {

    public Interface(HashMap<String, String> params, HashMap<String, String> languageParams, HashMap<String, Mapping> languageMappings, String packageName, Table table, Provider provider, Database database) {
        super(params, languageParams, languageMappings, packageName, table, provider, database);
    }
}
