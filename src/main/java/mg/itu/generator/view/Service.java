package mg.itu.generator.view;

import java.util.HashMap;
import mg.itu.database.Database;
import mg.itu.database.Mapping;
import mg.itu.database.Provider;
import mg.itu.database.Table;
import mg.itu.generator.Scaffolding;

public class Service extends Scaffolding {
  public Service(HashMap<String, String> params,
                 HashMap<String, String> languageParams,
                 HashMap<String, Mapping> languageMappings, String packageName,
                 Table table, Provider provider, Database database) {

    super(params, languageParams, languageMappings, packageName, table,
          provider, database);
  }
}
