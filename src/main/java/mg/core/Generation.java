package mg.core;

import mg.core.scaffolding.ScClass;
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
    private String template;
    private Database database;
    private boolean gettersSetters;
    private Table table;

    // Constructors
    public Generation(String langage, String path, String packageName, String template, Database database, boolean gettersSetters) {
        setLangage(langage);
        setPath(path);
        setPackageName(packageName);
        setTemplate(template);
        setDatabase(database);
        setGettersSetters(gettersSetters);
    }

    // Methods
    public void generate(Connection connection) {
        if (getTemplate().startsWith("class")) {
            if (getTable() == null)
                generateAllClass(connection);
            else
                generateClass(getTable());
        } else if (getTemplate().startsWith("rest_controller")) {
            if (getTable() == null)
                generateAllRestController(connection);
            else
                generateRestController(getTable());
        } else {
            throw new ScaffoldingException("Template not found: " + getTemplate());
        }
    }

    public void generateClass(Table table) {
        new ScClass(getLangage(), table, getDatabase()).generate(getPath(), getPackageName(), getTemplate(), haveGettersSetters());
    }

    public void generateAllClass(Connection connection) {
        for (Table table : getDatabase().getTables(connection)) {
            generateClass(table);
        }
    }

    public void generateRestController(Table table) {
        new ScRestController(getLangage(), table, getDatabase()).generate(getPath(), getPackageName(), getTemplate(), haveGettersSetters());
    }

    public void generateAllRestController(Connection connection) {
        for (Table table : getDatabase().getTables(connection)) {
            generateRestController(table);
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

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
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
}
