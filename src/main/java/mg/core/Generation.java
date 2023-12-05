package mg.core;

import mg.core.scaffolding.ScClass;
import mg.database.Database;
import mg.database.Table;
import mg.exception.DatabaseException;
import mg.exception.ScaffoldingException;

import java.sql.Connection;
import java.sql.SQLException;

public class Generation {
    private String langage;
    private String path;
    private String packageName;
    private String template;
    private Database database;
    private boolean gettersSetters;

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
            generateAllClass(connection);
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
}
