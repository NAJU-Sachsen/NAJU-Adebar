package de.naju.adebar.infrastructure.database;

/**
 * Service to run a raw SQL-query on a database
 *
 * @author Rico Bergmann
 * @param <R> the result of a query
 */
public interface DatabaseQueryExecutor<R> {

  /**
   * Executes a query
   *
   * @param query the query
   * @return the querie's result
   * @throws MalformedSqlException if the query contains syntax errors
   */
  R runQuery(String query);

}
