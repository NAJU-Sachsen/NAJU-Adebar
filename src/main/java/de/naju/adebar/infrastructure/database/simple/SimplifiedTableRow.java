package de.naju.adebar.infrastructure.database.simple;

import java.util.HashMap;
import java.util.Map;
import org.springframework.util.Assert;
import com.google.common.collect.HashBiMap;

/**
 * A table row simply is a tuple of named entries.
 *
 * @author Rico Bergmann
 */
public class SimplifiedTableRow {

  private Map<String, Object> content;
  private Map<Integer, String> columnIdxs;
  private int currIdx;

  /**
   * Default constructor
   */
  SimplifiedTableRow() {
    this.content = new HashMap<>();
    this.columnIdxs = new HashMap<>();
    currIdx = -1;
  }

  /**
   * @return the names of the columns
   */
  public Iterable<String> getColumnNames() {
    return content.keySet();
  }

  /**
   * @return the column's indexes, mapped by their name
   */
  public Map<String, Integer> getColumnIndexes() {
    return HashBiMap.create(columnIdxs).inverse();
  }

  /**
   * @param columnIndex the index of the column
   * @return the value of the column
   */
  public Object getColumn(int columnIndex) {
    return content.get(columnIdxs.get(columnIndex));
  }

  /**
   * @param name the name of the column
   * @return the value of the column
   */
  public Object getColumn(String name) {
    Assert.hasText(name, "name may not be empty");
    return content.get(name.toUpperCase());
  }

  /**
   * @return the number of columns in the row
   */
  public int getColumnCount() {
    return content.size();
  }

  /**
   * Inserts the given column into the row.
   *
   * <p>
   * If the name of the column is not unique, the old value will be overridden
   *
   * @param columnName the columns name
   * @param value the column value
   */
  void append(String columnName, Object value) {
    Assert.hasText(columnName, "columnName may not be empty");

    columnName = columnName.toUpperCase();
    currIdx++;
    content.put(columnName, value);
    columnIdxs.put(currIdx, columnName);
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return content.toString();
  }
}
