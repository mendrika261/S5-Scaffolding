package mg.database;

import lombok.Cleanup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.val;
import mg.exception.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Postgresql extends Database {
    private String jsonConfig = "postgresql.json";
    public static final String DRIVER = "org.postgresql.Driver";

    @Override
    public List<Table> getTables(Connection connection) {
        val sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = ? ORDER BY table_name";
        try {
            @Cleanup PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, SCHEMA);
            @Cleanup ResultSet resultSet = statement.executeQuery();
            val tables = new ArrayList<Table>();
            while (resultSet.next()) {
                val table = getTable(resultSet.getString("table_name"), connection);
                tables.add(table);
            }
            return tables;
        } catch (SQLException e) {
            throw new DatabaseException("Error getting tables: "+e.getMessage());
        }
    }

    @Override
    public Table getTable(String name, Connection connection) {
        val sql = "SELECT * FROM information_schema.columns WHERE table_schema = ? AND table_name = ? ORDER BY ordinal_position";
        val table = new Table();
        table.setName(name);
        val columns = new ArrayList<Column>();

        try {
            @Cleanup PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, SCHEMA);
            statement.setString(2, name);
            @Cleanup ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                val column = new Column();
                column.setName(resultSet.getString("column_name"));
                column.setType(resultSet.getString("data_type"));
                column.setNullable(resultSet.getString("is_nullable").equals("YES"));
                column.setDefaultValue(resultSet.getString("column_default"));
                columns.add(column);
                if(resultSet.getString("is_updatable").equals("NO"))
                    table.setMutable(false);
            }
            table.setColumns(columns);
            return table;
        } catch (SQLException e) {
            throw new DatabaseException("Error getting table: "+e.getMessage());
        }
    }

    public Connection getConnection() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("Error loading driver: "+DRIVER);
        } catch (SQLException e) {
            throw new DatabaseException("Error connecting to database: "+e.getMessage());
        }
    }
}
