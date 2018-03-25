package de.naju.adebar.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import de.naju.adebar.infrastructure.database.simple.SimpleDatabaseQueryExecutor;

/**
 * Provides access to a SQL "console" which enables the user to type {@code SELECT} queries and
 * display their results.
 *
 * @author Rico Bergmann
 */
@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class SqlConsoleController {

  private final SimpleDatabaseQueryExecutor databaseQueryExecutor;

  public SqlConsoleController(SimpleDatabaseQueryExecutor databaseQueryExecutor) {
    Assert.notNull(databaseQueryExecutor, "databaseQueryExecutor may not be null");
    this.databaseQueryExecutor = databaseQueryExecutor;
  }

  /**
   * Performs the given query
   *
   * @param query the query. Spring will default this to the empty string
   * @param model the model which will contain the data displayed by the view
   * @return the sqlConsole template
   */
  @GetMapping("/admin/sql-console")
  public String showSqlConsole(@RequestParam(value = "query", defaultValue = "") String query,
      Model model) {
    if (query != null && !query.isEmpty()) {
      model.addAttribute("query", query);
      model.addAttribute("resultSet", databaseQueryExecutor.runQuery(query));
    }
    return "sqlConsole";
  }

}
