package mg.itu.generator.view;

import mg.itu.database.*;
import mg.itu.generator.Entity;
import mg.itu.utils.Format;

import java.util.HashMap;
import java.util.List;

public class FormComponent extends Entity {
    private final StringBuilder importsDeps = new StringBuilder();

    public FormComponent(HashMap<String, String> params, HashMap<String, String> languageParams, HashMap<String, Mapping> languageMappings, String packageName, Table table, Provider provider, Database database) {
        super(params, languageParams, languageMappings, packageName, table, provider, database);
    }

    public String processImport(String importName, String pathName) {
        String importCode = getLanguageParams().get("#import#");
        importCode = importCode.replace("#importName#", importName);
        importCode = importCode.replace("#importPath#", pathName);
        return importCode;
    }

    public String processForeignKeys()
    {
        StringBuilder foreignKeys=new StringBuilder();
        List<Column> list=getTable().getForeignKeys();
        for (Column c:list)
        {
            String fk=getParams().get("#FKtable#");
            String fkName = Format.toCamelCase(c.getTableName(),false);
            String fkType = Format.toCamelCase(c.getTableName(),true);
            foreignKeys.append(fk
                    .replace("#FKname#", fkName)
                    .replace("#FKtype#", fkType));
            importsDeps.append(
                    processImport(
                            fkType,
                            getParams().get("interface_path")
                                    .replace("#FKname#", fkName))
            );
        }
        return foreignKeys.toString();
    }

    private String processDeps(String deps, String fkType, String fkName) {
        return processImport(
                fkType,
                getParams().get(deps)
                        .replace("#FKname#", fkName));
    }


    public String processForeignKeyServices()
    {
        StringBuilder foreignKeys=new StringBuilder();
        List<Column> list=getTable().getForeignKeys();
        for (Column c:list)
        {
            String fk=getParams().get("#FKservice#");
            String fkName = Format.toCamelCase(c.getTableName(),false);
            String fkType = Format.toCamelCase(c.getTableName(),true);
            foreignKeys.append(fk
                    .replace("#FKname#", fkName)
                    .replace("#FKtype#", fkType));
            importsDeps.append(processDeps("service_path", fkType+getParams().get("service_suffix"), fkName));

        }
        return foreignKeys.toString();
    }

    public String processForeignKeyGetters()
    {
        StringBuilder stringBuilder=new StringBuilder();
        List<Column> list=getTable().getForeignKeys();
        for (Column c:list)
        {
            String fk=getParams().get("#FKgetter#");
            String fkName = Format.toCamelCase(c.getTableName(),false);
            String fkType = Format.toCamelCase(c.getTableName(),true);
            stringBuilder.append(fk
                    .replace("#FKname#", fkName)
                    .replace("#FKtype#", fkType));

        }
        return stringBuilder.toString();
    }
    public String processForeignKeyGetterFunction()
    {
        StringBuilder stringBuilder=new StringBuilder();
        List<Column> list=getTable().getForeignKeys();
        for (Column c:list)
        {
            String fk=getParams().get("#FKgettersFunction#");
            String fkName = Format.toCamelCase(c.getTableName(),false);
            String fkType = Format.toCamelCase(c.getTableName(),true);
            stringBuilder.append(fk
                    .replace("#FKname#", fkName)
                            .replace("#service_suffix#",getParams().get("service_suffix"))
                    .replace("#FKtype#", fkType));

        }
        return stringBuilder.toString();
    }

    @Override
    protected String process(String code) {
        return super.process(code
                .replace("#FKtables#", processForeignKeys())
                .replace("#FKservices#",processForeignKeyServices())
                .replace("#depsImports#", importsDeps.toString())
                .replace("#FKgettersFunctions#",processForeignKeyGetterFunction())
                .replace("#FKgetters#",processForeignKeyGetters())
        );
    }
}
