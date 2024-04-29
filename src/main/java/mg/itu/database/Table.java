package mg.itu.database;

import mg.itu.display.Console;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private String name;
    private List<Column> columns = new ArrayList<>();
    private boolean mutable = true;

    public Column getPrimaryKey() {
        for (Column column : getColumns()) {
            if (column.isPrimaryKey()) {
                return column;
            }
        }
        Console.LOGGER.throwing(Level.ERROR, new Exception("No primary key found for table: " + getName()));
        return null;
    }


    public List<Column> getForeignKeys()
    {
        List<Column> liste=new ArrayList<>();
        for(Column column:getColumns())
        {
            if(column.isForeignKey())
            {
                liste.add(column);
            }
        }
        return liste;
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
        return mutable;
    }

    public void setMutable(boolean mutable) {
        this.mutable = mutable;
    }

    @Override
    public String toString() {
        return "Table{" +
                "name='" + name + '\'' +
                ", columns=" + columns +
                ", mutable=" + mutable +
                '}';
    }
}
