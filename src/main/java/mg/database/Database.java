package mg.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import lombok.Cleanup;
import lombok.Data;
import lombok.val;
import mg.core.data.DbData;
import mg.core.Utils;
import mg.exception.DatabaseException;

@Data
public class Database {
  public final String URL = Utils.getEnv("DB_URL");
  public final String USER = Utils.getEnv("DB_USER");
  public final String PASSWORD = Utils.getEnv("DB_PASSWORD");
  public final String SCHEMA = Utils.getEnv("DB_SCHEMA", "public");
  public final String DRIVER = Utils.getEnv("DB_DRIVER");
  public String jsonConfig;
  private DbData dbData;

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
      val databaseMetaData = connection.getMetaData();
      @Cleanup val resultSet = databaseMetaData.getTables(null, SCHEMA, null, new String[]{"TABLE", "VIEW"});
      val tables = new ArrayList<Table>();
      while (resultSet.next()) {
        val table = getTable(resultSet.getString("TABLE_NAME"), connection);
        table.setMutable(resultSet.getString("TABLE_TYPE").equals("TABLE"));
        tables.add(table);
      }
      return tables;
    } catch (SQLException e) {
      throw new DatabaseException("Error getting tables: " + e.getMessage());
    }
  }

  public Table getTable(String name, Connection connection) {
    val table = new Table();
    table.setName(name);
    val columns = new ArrayList<Column>();

    try {
      @Cleanup val resultSet = connection.getMetaData().getColumns(null, SCHEMA, name, null);
      while (resultSet.next()) {
        val column = new Column();
        column.setName(resultSet.getString("COLUMN_NAME"));
        column.setType(resultSet.getString("TYPE_NAME"));
        column.setNullable(resultSet.getBoolean("NULLABLE"));
        column.setDefaultValue(resultSet.getString("COLUMN_DEF"));
        column.setComment(resultSet.getString("REMARKS"));
        columns.add(column);
      }
      table.setColumns(columns);
      return table;
    } catch (SQLException e) {
      throw new DatabaseException("Error getting table: " + e.getMessage());
    }
  }
}
