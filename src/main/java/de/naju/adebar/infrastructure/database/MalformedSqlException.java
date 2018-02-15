package de.naju.adebar.infrastructure.database;

/**
 * Exception to indicate that a SQL-query has syntax errors. Each exception provides access to the
 * faulty query.
 *
 * @author Rico Bergmann
 */
public class MalformedSqlException extends RuntimeException {

  private static final long serialVersionUID = -1697878725758998908L;

  private final String query;

  /**
   * @param query the erroneous query
   */
  public MalformedSqlException(String query) {
    this.query = query;
  }

  /**
   * @param query the erroneous query
   * @param cause more error details
   */
  public MalformedSqlException(String query, Throwable cause) {
    super(cause);
    this.query = query;
  }

  /**
   * @param query the erroneous query
   * @param message an error description
   */
  public MalformedSqlException(String query, String message) {
    super(message);
    this.query = query;
  }

  /**
   * @param query the erroneous query
   * @param message an error description
   * @param cause more error details
   */
  public MalformedSqlException(String query, String message, Throwable cause) {
    super(message, cause);
    this.query = query;
  }

  /**
   * @return the erroneous query
   */
  public String getQuery() {
    return query;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "MalformedSqlException [query=" + query + ", message=" + getMessage() + ", cause="
        + getCause() + "]";
  }

}
