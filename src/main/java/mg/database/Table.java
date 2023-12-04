package mg.database;

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
}
