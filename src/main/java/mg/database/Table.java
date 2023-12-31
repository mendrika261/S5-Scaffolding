package mg.database;

import mg.exception.DatabaseException;

import java.util.ArrayList;
import java.util.List;


public class Table {
  private String name;
  private List<Column> columns = new ArrayList<>();
  private boolean isMutable = true;
  // Constructor
  public Table() {
  }


  // Getters and Setters
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Column> getColumns() {
    return columns;
  }

  public void setColumns(List<Column> columns) {
    this.columns = columns;
  }

  public boolean isMutable() {
    return isMutable;
  }

  public void setMutable(boolean mutable) {
    isMutable = mutable;
  }

  public Column getPrimaryKey() {
    for(Column column: getColumns()) {
        if (column.isPrimaryKey()) {
            return column;
        }
    }
    throw new DatabaseException("No primary key found for table: " + getName());
  }
}
