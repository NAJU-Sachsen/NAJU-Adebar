package de.naju.adebar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class of the application
 * 
 * @author Rico Bergmann
 *
 */
@SpringBootApplication
public class Application {

  /**
   * Just the main-method calling Spring to take care of all the rest.
   * 
   * @param args application arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
