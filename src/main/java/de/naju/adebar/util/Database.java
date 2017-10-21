package de.naju.adebar.util;

import de.naju.adebar.infrastructure.ReadOnlyRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * Utility functions for dealing with databases. Mainly for testing purposes. Usage on productive
 * environments may not be sensible.
 *
 * @author Rico Bergmann
 */
public class Database {

  /**
   * Plots the content of a database
   *
   * @param db database to print
   */
  public static void dumpDatabase(CrudRepository<?, ?> db) {
    for (Object entity : db.findAll()) {
      System.out.println(entity.toString());
    }
  }

  /**
   * Plots the content of a database
   *
   * @param db database to print
   */
  public static void dumpDatabase(ReadOnlyRepository<?, ?> db) {
    for (Object entity : db.findAll()) {
      System.out.println(entity.toString());
    }
  }

}
