import java.sql.Connection;
import java.util.List;
import lombok.Cleanup;
import mg.core.scaffolding.ScClass;
import mg.database.Database;
import mg.database.provider.Postgresql;
import mg.database.Table;

public class Main {
  public static void main(String[] args) throws Exception {
    Database postgresql = new Postgresql();

    @Cleanup Connection connection = postgresql.getConnection();

    List<Table> tables = postgresql.getTables(connection);

    new ScClass("java", tables.get(1), postgresql).generate( "test/medar", "class", true);
  }
}
