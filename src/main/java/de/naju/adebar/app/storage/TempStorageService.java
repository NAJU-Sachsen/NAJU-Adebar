package de.naju.adebar.app.storage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * A {@code StorageService} that saves its files in a temporary directory. Therefore these files may
 * only exist for a short period of time and the {@link #load(String)} operation is not supported.
 *
 * @author Rico Bergmann
 */
@Service
public class TempStorageService implements StorageService {

  private final File directory;

  /**
   * Creates a new storage service and initializes a new temporary directory to write the files to.
   */
  public TempStorageService() {
    try {
      this.directory = Files.createTempDirectory("adebar").toFile();
    } catch (IOException e) {
      throw new StorageException(e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.naju.adebar.app.storage.StorageService#store(java.lang.String, java.io.InputStream)
   */
  @Override
  public File store(String filename, InputStream file) {
    Assert.isTrue(filename.matches(SIMPLE_FILE.pattern()), "Illegal filename: " + filename);
    File target = new File(directory, filename);

    try {
      Files.copy(file, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      throw new StorageException("Cannot store file " + filename, e);
    }

    return target;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.naju.adebar.app.storage.StorageService#load(java.lang.String)
   */
  @Override
  public File load(String filename) {
    throw new UnsupportedOperationException("Temporary files may not be re-loaded");
  }

}
