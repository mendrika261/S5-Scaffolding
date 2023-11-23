package mg.database;

import lombok.Data;
import mg.core.DbData;
import mg.core.Utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Data
public abstract class Database {
    public final String URL = Utils.getEnv("DB_URL");
    public final String USER = Utils.getEnv("DB_USER");
    public final String PASSWORD = Utils.getEnv("DB_PASSWORD");
    public final String SCHEMA = Utils.getEnv("DB_SCHEMA", "public");
    private DbData dbData;

    public abstract List<Table> getTables(Connection connection);

    public abstract Table getTable(String name, Connection connection) throws SQLException;

    public abstract Connection getConnection();

    public abstract String getJsonConfig();

    public DbData getDbData() {
        if (dbData == null)
            return DbData.getDbData(Utils.DATA_DB_PATH + getJsonConfig());
        return dbData;
    }
}
