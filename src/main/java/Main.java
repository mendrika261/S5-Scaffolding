import lombok.Cleanup;
import mg.core.ScJava;
import mg.database.Database;
import mg.database.Postgresql;
import mg.database.Table;

import java.sql.Connection;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        Database postgresql = new Postgresql();

        @Cleanup Connection connection = postgresql.getConnection();

        List<Table> tables = postgresql.getTables(connection);

        new ScJava(tables.get(1), postgresql).generate("test", "class");

    }
}