package de.naju.adebar.infrastructure.database.simple;

import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import de.naju.adebar.infrastructure.database.DatabaseQueryExecutor;
import de.naju.adebar.infrastructure.database.MalformedSqlException;

/**
 * Service to provide a {@link SimplifiedResultSet} for a database query
 *
 * @author Rico Bergmann
 */
@Service
public class SimpleDatabaseQueryExecutor implements DatabaseQueryExecutor<SimplifiedResultSet> {

  private JdbcTemplate jdbc;

  /**
   * @param jdbc the JDBC template to run the query on
   */
  public SimpleDatabaseQueryExecutor(JdbcTemplate jdbc) {
    Assert.notNull(jdbc, "jdbcTemplate may not be null");
    this.jdbc = jdbc;
  }


  /*
   * (non-Javadoc)
   * 
   * @see de.naju.adebar.infrastructure.database.DatabaseQueryExecutor#runQuery(java.lang.String)
   */
  @Override
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public SimplifiedResultSet runQuery(String query) {
    try {
      return jdbc.query(query, new SimpleResultSetExtractor());
    } catch (BadSqlGrammarException e) {
      throw new MalformedSqlException(query, e);
    }

  }

}
