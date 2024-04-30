package mg.itu.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import mg.itu.display.Console;
import org.apache.logging.log4j.Level;

public class Database {
  private String provider;
  private String host;
  private String schema = "public";
  private String port;
  private String user;
  private String password;
  private String database;
  private List<Table> tables;

  public void init(Config config) {
    final Provider provider = config.getProviders().get(getProvider());
    Connection connection = getConnection(provider);
    setTables(getTables(connection));
    try {
      connection.close();
    } catch (SQLException ignored) {
    }
  }

  public Connection getConnection(Provider provider) {
    final String url = "jdbc:" + getProvider() + "://" + getHost() + ":" +
                       getPort() + "/" + getDatabase();
    try {
      Class.forName(provider.getDriver());
      return DriverManager.getConnection(url, getUser(), getPassword());
    } catch (Exception e) {
      Console.LOGGER.log(Level.ERROR, e.getMessage());
      return null;
    }
  }

  public String getForeignKey(Connection connection, String column, String tab)
      throws SQLException {
    ResultSet resultSet = connection.getMetaData().getImportedKeys(
        connection.getCatalog(), getSchema(), tab);
    while (resultSet.next()) {
      String columnName = resultSet.getString("PKCOLUMN_NAME");
      if (columnName.equals(column)) {
        return resultSet.getString("PKTABLE_NAME");
      }
    }
    return null;
  }

  public Table getTableForeignKey(String collumn, Connection connection,
                                  String tableName) throws SQLException {
    Table table = null;
    ResultSet set = connection.getMetaData().getImportedKeys(
        connection.getCatalog(), getSchema(), tableName);
    while (set.next()) {
      String columnName = set.getString("FKCOLUMN_NAME");
      String name = set.getString("PKTABLE_NAME");
      if (columnName.equals(collumn)) {
        table = getTable(name, connection);
        return table;
      }
    }
    return table;
  }

  public Table getTable(String name, Connection connection) {
    final Table table = new Table();
    table.setName(name);
    final List<Column> columns = new ArrayList<>();

    try {
      final ResultSet resultSet =
          connection.getMetaData().getColumns(null, getSchema(), name, null);
      while (resultSet.next()) {
        final Column column = new Column();
        column.setPrimaryKey(resultSet.isFirst());
        column.setName(resultSet.getString("COLUMN_NAME"));
        column.setType(resultSet.getString("TYPE_NAME"));
        column.setNullable(resultSet.getBoolean("NULLABLE"));
        column.setDefaultValue(resultSet.getString("COLUMN_DEF"));
        column.setComment(resultSet.getString("REMARKS"));
        String fk = getForeignKey(connection, column.getName(), name);
        if (fk != null) {
          column.setForeignKey(true);
          column.setTableName(fk);
        }
        columns.add(column);
      }
      table.setColumns(columns);
      resultSet.close();
    } catch (SQLException ignored) {
    }

    return table;
  }

  public List<Table> getTables(Connection connection) {
    final List<Table> tables = new ArrayList<>();
    try {
      final DatabaseMetaData databaseMetaData = connection.getMetaData();
      final ResultSet resultSet = databaseMetaData.getTables(
          null, getSchema(), null, new String[] {"TABLE", "VIEW"});

      while (resultSet.next()) {
        final Table table =
            getTable(resultSet.getString("TABLE_NAME"), connection);
        table.setMutable(resultSet.getString("TABLE_TYPE").equals("TABLE"));
        tables.add(table);
      }

      resultSet.close();
    } catch (SQLException ignored) {
    }
    return tables;
  }

  // Getters and Setters
  public String getProvider() { return provider; }

  public void setProvider(String provider) { this.provider = provider; }

  public String getSchema() { return schema; }

  public void setSchema(String schema) { this.schema = schema; }

  public List<Table> getTables() { return tables; }

  public void setTables(List<Table> tables) { this.tables = tables; }

  public String getHost() { return host; }

  public void setHost(String host) { this.host = host; }

  public String getPort() { return port; }

  public void setPort(String port) { this.port = port; }

  public String getUser() { return user; }

  public void setUser(String user) { this.user = user; }

  public String getPassword() { return password; }

  public void setPassword(String password) { this.password = password; }

  public String getDatabase() { return database; }

  public void setDatabase(String database) { this.database = database; }

  @Override
  public String toString() {
    return "Connexion{"
        + "driver='" + getProvider() + '\'' + ", host='" + getHost() + '\'' +
        ", port='" + getPort() + '\'' + ", user='" + getUser() + '\'' +
        ", password='" + getPassword() + '\'' + ", database='" + getDatabase() +
        '\'' + '}';
  }
}
