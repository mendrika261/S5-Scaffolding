package mg.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import mg.core.data.DbData;
import mg.core.Utils;
import mg.core.scaffolding.ScClass;
import mg.core.scaffolding.ScRestController;
import mg.exception.DatabaseException;
import mg.exception.ScaffoldingException;


public class Database {
  public final String URL = Utils.getEnv("DB_URL");
  public final String USER = Utils.getEnv("DB_USER");
  public final String PASSWORD = Utils.getEnv("DB_PASSWORD");
  public final String SCHEMA = Utils.getEnv("DB_SCHEMA", "public");
  public final String DRIVER = Utils.getEnv("DB_DRIVER");
  public String jsonConfig;
  private DbData dbData;


  // Constructors
  public Database() {
  }


  // Methods
  public String getJsonConfig() {
    if (jsonConfig == null)
      try {
        setJsonConfig(URL.split(":")[1] + ".json");
      } catch (Exception e) {
        throw new DatabaseException("Error getting json config: " + e.getMessage());
      }
    return jsonConfig;
  }

  public DbData getDbData() {
    if (dbData == null)
      try {
        setDbData(DbData.getDbData(Utils.DATA_DB_PATH + getJsonConfig()));
      } catch (Exception e) {
        throw new DatabaseException("Error getting db data: " + e.getMessage());
      }
    return dbData;
  }

  public Connection getConnection() {
    try {
      Class.forName(DRIVER);
      return DriverManager.getConnection(URL, USER, PASSWORD);
    } catch (ClassNotFoundException e) {
      throw new DatabaseException("Error loading driver: " + DRIVER);
    } catch (SQLException e) {
      throw new DatabaseException("Error connecting to database: " +
              e.getMessage());
    }
  }

  public List<Table> getTables(Connection connection) {
    try {
      final DatabaseMetaData databaseMetaData = connection.getMetaData();
      final ResultSet resultSet = databaseMetaData.getTables(null, SCHEMA, null, new String[]{"TABLE", "VIEW"});
      final List<Table> tables = new ArrayList<>();

      while (resultSet.next()) {
        final Table table = getTable(resultSet.getString("TABLE_NAME"), connection);
        table.setMutable(resultSet.getString("TABLE_TYPE").equals("TABLE"));
        tables.add(table);
      }

      resultSet.close();
      return tables;
    } catch (SQLException e) {
      throw new DatabaseException("Error getting tables: " + e.getMessage());
    }
  }

  public Table getTable(String name, Connection connection) {
    Table table = new Table();
    table.setName(name);
    List<Column> columns = new ArrayList<>();

    try {
      ResultSet resultSet = connection.getMetaData().getColumns(null, SCHEMA, name, null);
      while (resultSet.next()) {
        Column column = new Column();
        column.setName(resultSet.getString("COLUMN_NAME"));
        column.setType(resultSet.getString("TYPE_NAME"));
        column.setNullable(resultSet.getBoolean("NULLABLE"));
        column.setDefaultValue(resultSet.getString("COLUMN_DEF"));
        column.setComment(resultSet.getString("REMARKS"));
        columns.add(column);
      }
      table.setColumns(columns);
      resultSet.close();
      return table;
    } catch (SQLException e) {
      throw new DatabaseException("Error getting table: " + e.getMessage());
    }
  }


  // Getters and Setters
  public void setJsonConfig(String jsonConfig) {
    this.jsonConfig = jsonConfig;
  }

  public void setDbData(DbData dbData) {
    this.dbData = dbData;
  }
}
