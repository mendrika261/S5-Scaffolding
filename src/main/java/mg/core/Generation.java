package mg.core;

import mg.core.data.Template;
import mg.core.scaffolding.ScClass;
import mg.core.scaffolding.ScEntity;
import mg.core.scaffolding.ScRepository;
import mg.core.scaffolding.ScRestController;
import mg.database.Database;
import mg.database.Table;
import mg.exception.DatabaseException;
import mg.exception.ScaffoldingException;

import java.sql.Connection;

public class Generation {
    private String langage;
    private String path;
    private String packageName;
    private Template template;
    private Database database;
    private boolean gettersSetters;
    private Table table;
    private String objectImport;
    private String objectServiceImport;

    // Constructors
    public Generation(String langage, String path, String packageName, Template template, Database database, boolean gettersSetters) {
        setLangage(langage);
        setPath(path);
        setPackageName(packageName);
        setTemplate(template);
        setDatabase(database);
        setGettersSetters(gettersSetters);
    }

    // Methods
    public void generate(Connection connection) {
        if (getTemplate().getEngine().equals("class")) {
            if (getTable() == null)
                generateAllClass(connection);
            else
                generateClass(getTable());
        } else if (getTemplate().getEngine().equals("rest_controller")) {
            if (getTable() == null)
                generateAllRestController(connection);
            else
                generateRestController(getTable());
        } else if (getTemplate().getEngine().equals("entity")) {
            if (getTable() == null)
                generateAllEntity(connection);
            else
                generateEntity(getTable());
        } else if (getTemplate().getEngine().equals("repository")) {
            if (getTable() == null)
                generateAllRepository(connection);
            else
                generateRepository(getTable());
        } else {
            throw new ScaffoldingException("Template engine not found: " + getTemplate().getEngine());
        }
    }

    public void generateClass(Table table) {
        new ScClass(getLangage(), table, getDatabase())
                .generate(getPath(), getPackageName(), getTemplate(), haveGettersSetters());
    }

    public void generateAllClass(Connection connection) {
        for (Table table : getDatabase().getTables(connection)) {
            generateClass(table);
        }
    }

    public void generateRestController(Table table) {
        new ScRestController(getLangage(), table, getDatabase(), getObjectImport(), getObjectServiceImport())
                .generate(getPath(), getPackageName(), getTemplate(), haveGettersSetters());
    }

    public void generateAllRestController(Connection connection) {
        for (Table table : getDatabase().getTables(connection)) {
            generateRestController(table);
        }
    }

    public void generateEntity(Table table) {
        new ScEntity(getLangage(), table, getDatabase())
                .generate(getPath(), getPackageName(), getTemplate(), haveGettersSetters());
    }

    public void generateAllEntity(Connection connection) {
        for (Table table : getDatabase().getTables(connection)) {
            generateEntity(table);
        }
    }

    public void generateRepository(Table table) {
        new ScRepository(getLangage(), table, getDatabase())
                .generate(getPath(), getPackageName(), getTemplate(), haveGettersSetters());
    }

    public void generateAllRepository(Connection connection) {
        for (Table table : getDatabase().getTables(connection)) {
            generateRepository(table);
        }
    }

    // Getters and setters
    public Database getDatabase() {
        if(database == null)
            throw new DatabaseException("Database is not set");
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public String getLangage() {
        return langage;
    }

    public void setLangage(String langage) {
        this.langage = langage;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean haveGettersSetters() {
        return gettersSetters;
    }

    public void setGettersSetters(boolean gettersSetters) {
        this.gettersSetters = gettersSetters;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String getObjectImport() {
        return objectImport;
    }

    public void setObjectImport(String objectImport) {
        this.objectImport = objectImport;
    }

    public String getObjectServiceImport() {
        return objectServiceImport;
    }

    public void setObjectServiceImport(String objectServiceImport) {
        this.objectServiceImport = objectServiceImport;
    }
}
