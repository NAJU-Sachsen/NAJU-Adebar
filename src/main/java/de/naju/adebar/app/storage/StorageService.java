package de.naju.adebar.app.storage;

import java.io.File;
import java.io.InputStream;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.validation.constraints.NotEmpty;

/**
 * Service to write files to some buffer and retrieve them later on.
 *
 * @author Rico Bergmann
 */
public interface StorageService {

  /**
   * Regular expression to represent simple file names which could be used by the stores.
   */
  Pattern SIMPLE_FILE =
      Pattern.compile("(?<fname>[a-zA-Z]*[a-zA-Z0-9\\-]*\\w*)(\\.(?<ftype>[a-zA-Z0-9]*))?");

  /**
   * Saves a file. If it may not be saved for some reason a {@link StorageException} will be thrown.
   *
   * @param filename the filename
   * @param file the file's content
   * @return the resulting (written) file
   */
  @Nonnull
  File store(@NotEmpty String filename, @Nonnull InputStream file);

  /**
   * Tries to load a file. If it may not be loaded for some reason a {@link StorageException} will
   * be thrown.
   *
   * @param filename the name of the file to load
   * @return the file
   */
  @Nonnull
  File load(@NotEmpty String filename);

}
