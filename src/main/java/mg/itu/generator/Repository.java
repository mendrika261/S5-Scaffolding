package mg.itu.generator;

import java.util.HashMap;
import mg.itu.database.Database;
import mg.itu.database.Mapping;
import mg.itu.database.Provider;
import mg.itu.database.Table;

public class Repository extends Scaffolding {
  public Repository(HashMap<String, String> params,
                    HashMap<String, String> languageParams,
                    HashMap<String, Mapping> languageMappings,
                    String packageName, Table table, Provider provider,
                    Database database) {
    super(params, languageParams, languageMappings, packageName, table,
          provider, database);
  }

  private String processExtends() {
    addImport(getParams().getOrDefault("extends_import", null));
    return getParams().getOrDefault("#extends#", "");
  }

  @Override
  protected void processDeps() {
    processDeps("entity_package", "#entity_name#");
  }

  @Override
  protected String process(String code) {
    return super.process(code.replace("#extends#", processExtends()));
  }
}
