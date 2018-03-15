package de.naju.adebar.infrastructure.database.simple;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.springframework.util.Assert;

/**
 * The result set provides a description of the underlying relation as well as a number of {@link
 * SimplifiedTableRow SimplifiedTableRows} which represent the entries of the result set. These rows
 * do not necessarily have to have the same columns. However placing different rows in the result
 * set completely contradicts its sense.
 *
 * @author Rico Bergmann
 */
public class SimplifiedResultSet implements Iterable<SimplifiedTableRow> {

  private Iterable<String> columns;
  private List<SimplifiedTableRow> resultSet;

  /**
   * @param columns the columns in the relation
   */
  SimplifiedResultSet(Iterable<String> columns) {
    this.columns = columns;
    this.resultSet = new LinkedList<>();
  }

  /**
   * @return the columns in the relation
   */
  public Iterable<String> getColumnNames() {
    return columns;
  }

  /**
   * @return the relation's rows
   */
  public Iterable<SimplifiedTableRow> getResultSet() {
    return Collections.unmodifiableList(resultSet);
  }

  /**
   * @return the number of rows in the result set
   */
  public int getResultSetSize() {
    return resultSet.size();
  }

  /**
   * @param row the row to add to the result set
   */
  void appendRow(SimplifiedTableRow row) {
    Assert.notNull(row, "row may not be null");
    resultSet.add(row);
  }


  /*
   * (non-Javadoc)
   *
   * @see java.lang.Iterable#iterator()
   */
  @Override
  public Iterator<SimplifiedTableRow> iterator() {
    return resultSet.iterator();
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "SimplifiedResultSet [columns=" + columns + ", resultSet=" + resultSet + "]";
  }


}
