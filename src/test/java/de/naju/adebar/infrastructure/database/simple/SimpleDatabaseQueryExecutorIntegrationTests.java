package de.naju.adebar.infrastructure.database.simple;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import de.naju.adebar.infrastructure.database.MalformedSqlException;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Sql("/sql/newsletter_test_data.sql")
public class SimpleDatabaseQueryExecutorIntegrationTests {

  @Autowired
  private SimpleDatabaseQueryExecutor queryExec;

  @Test(expected = AccessDeniedException.class)
  @WithMockUser
  public void requiresAdminPrivileges() {
    String query = "SELECT * FROM subscriber";
    queryExec.runQuery(query);
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void executesSelectQueries() {
    String query = "SELECT * FROM subscriber";
    SimplifiedResultSet resultSet = queryExec.runQuery(query);

    assertThat(resultSet).hasSize(2);
    assertThat(resultSet.getColumnNames()).hasSize(4);
    assertThat(resultSet.getResultSet())
        .anySatisfy((row) -> row.getColumn("EMAIL").equals("john@email.com"));
    assertThat(resultSet.getResultSet())
        .anySatisfy((row) -> row.getColumn("EMAIL").equals("jane@email.com"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void executesJoins() {
    String query = "SELECT * FROM subscriber AS s " //
        + "JOIN newsletter_subscribers AS n ON s.id = n.subscriber_id";
    SimplifiedResultSet resultSet = queryExec.runQuery(query);

    assertThat(resultSet).hasSize(2);
  }

  @Test(expected = DataAccessException.class)
  @WithMockUser(roles = "ADMIN")
  public void doesNotExecuteInserts() {
    String insert = "INSERT INTO subscriber (email) VALUES ('john.smith@email.com')";
    queryExec.runQuery(insert);
  }

  @Test(expected = DataAccessException.class)
  @WithMockUser(roles = "ADMIN")
  public void doesNotExecuteUpdates() {
    String update = "UPDATE subscriber SET first_name = 'Joe' WHERE email = 'john@email.com'";
    queryExec.runQuery(update);
  }

  @Test(expected = MalformedSqlException.class)
  @WithMockUser(roles = "ADMIN")
  public void throwsMalformedSqlExceptionIfMalformedQuery() {
    String query = "SELECT WHERE last_name = 'Foo'";
    queryExec.runQuery(query);
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void extractsAllColumns() {
    String query = "SELECT * FROM subscriber";
    SimplifiedResultSet resultSet = queryExec.runQuery(query);

    assertThat(resultSet.getResultSet()).hasSize(2);
    assertThat(resultSet.getResultSet()).allMatch((row) -> row.getColumnCount() == 4);
  }

}
