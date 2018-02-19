package de.naju.adebar.infrastructure.database.simple;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 * Service to convert a {@link ResultSet} to a {@link SimplifiedResultSet}
 *
 * @author Rico Bergmann
 */
class SimpleResultSetExtractor implements ResultSetExtractor<SimplifiedResultSet> {

  /*
   * (non-Javadoc)
   *
   * @see org.springframework.jdbc.core.ResultSetExtractor#extractData(java.sql.ResultSet)
   */
  @Override
  public SimplifiedResultSet extractData(ResultSet rs) throws SQLException {
    ResultSetMetaData tableInfo = rs.getMetaData();
    Iterable<String> columns = getTableColumns(tableInfo);
    SimplifiedResultSet resultSet = new SimplifiedResultSet(columns);

    while (rs.next()) {
      resultSet.appendRow(extractRow(rs, columns));
    }

    return resultSet;
  }

  /**
   * Provides a {@link SimplifiedTableRow} for the current row in the result set.
   *
   * @param resultSet the result set containing all rows
   * @param columns the columns to extract
   * @return the table row
   * @throws SQLException if some data may not be retrieved
   */
  private SimplifiedTableRow extractRow(ResultSet resultSet, Iterable<String> columns)
      throws SQLException {
    SimplifiedTableRow row = new SimplifiedTableRow();

    for (String column : columns) {
      row.append(column, resultSet.getObject(column));
    }

    return row;
  }

  /**
   * Provides all columns contained in a table
   *
   * @param tableInfo the table to analyze
   * @return the columns
   * @throws SQLException if the table may not be analyzed
   */
  private Iterable<String> getTableColumns(ResultSetMetaData tableInfo) throws SQLException {
    List<String> columns = new ArrayList<>(tableInfo.getColumnCount());

    for (int i = 1; i <= tableInfo.getColumnCount(); ++i) {
      columns.add(tableInfo.getColumnName(i));
    }

    return columns;
  }
}
