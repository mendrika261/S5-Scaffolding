package mg.itu.generator;

import java.util.HashMap;
import mg.itu.database.Database;
import mg.itu.database.Mapping;
import mg.itu.database.Provider;
import mg.itu.database.Table;

public class Controller extends Scaffolding {
  public Controller(HashMap<String, String> params,
                    HashMap<String, String> languageParams,
                    HashMap<String, Mapping> languageMappings,
                    String packageName, Table table, Provider provider,
                    Database database) {
    super(params, languageParams, languageMappings, packageName, table,
          provider, database);
  }

  @Override
  protected void processDeps() {
    processDeps("entity_package", "#entity_name#");
    processDeps("repository_package", "#repository_name#");
    processDeps("service_package", "#service_name#");

    addImport(getParams().get("request_body_import_annotation"));
    addImport(getParams().get("path_variable_import_annotation"));
    addImport(getParams().get("import"));
  }

  private String processDatasource() {
    addImport(getParams().get("datasource_import"));
    return getParams().get("#datasource#");
  }

  @Override
  protected String process(String code) {
    return super.process(
        code.replace("#datasource#", processDatasource())
            .replace("#save_annotation#", getParams().get("#save_annotation#"))
            .replace("#read_all_annotation#",
                     getParams().get("#read_all_annotation#"))
            .replace("#read_all_annotation_pagination#",
                     getParams().get("#read_all_annotation_pagination#"))
            .replace("#read_annotation#", getParams().get("#read_annotation#"))
            .replace("#update_annotation#",
                     getParams().get("#update_annotation#"))
            .replace("#delete_annotation#",
                     getParams().get("#delete_annotation#"))
            .replace("#save_process#", getParams().get("#save_process#"))
            .replace("#read_all_process#",
                     getParams().get("#read_all_process#"))
            .replace("#read_all_pagination_process#",
                     getParams().get("#read_all_pagination_process#"))
            .replace("#read_process#", getParams().get("#read_process#"))
            .replace("#update_process#", getParams().get("#update_process#"))
            .replace("#delete_process#", getParams().get("#delete_process#"))
            .replace("#request_body_annotation#",
                     getParams().get("#request_body_annotation#"))
            .replace("#path_variable_annotation#",
                     getParams().get("#path_variable_annotation#"))
            .replace("#throws#", getParams().get("#throws#")));
  }
}
