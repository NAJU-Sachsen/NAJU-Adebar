package de.naju.adebar.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;
import de.naju.adebar.infrastructure.ReadOnlyRepository;

/**
 * Utility functions for dealing with databases. Mainly for testing purposes. Usage on productive
 * environments may not be sensible.
 *
 * @author Rico Bergmann
 */
public class Database {

  private static final Logger log = LoggerFactory.getLogger(Database.class);

  private Database() {}

  /**
   * Plots the content of a database
   *
   * @param db database to print
   */
  public static void dumpDatabase(CrudRepository<?, ?> db) {
    if (!log.isDebugEnabled()) {
      return;
    }
    for (Object entity : db.findAll()) {
      log.debug(entity.toString());
    }
  }

  /**
   * Plots the content of a database
   *
   * @param db database to print
   */
  public static void dumpDatabase(ReadOnlyRepository<?, ?> db) {
    if (!log.isDebugEnabled()) {
      return;
    }
    for (Object entity : db.findAll()) {
      log.debug(entity.toString());
    }
  }

}
