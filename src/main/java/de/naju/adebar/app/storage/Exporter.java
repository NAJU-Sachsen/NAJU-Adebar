package de.naju.adebar.app.storage;

import java.io.InputStream;
import javax.annotation.Nonnull;

/**
 * An {@code Exporter} writes objects to a streams to store them in files and the like.
 *
 * @author Rico Bergmann
 * @param <T> the types of objects this exporter handles
 */
public interface Exporter<T> {

	/**
	 * Provides an {@code InputStream} to write an object to the File System or other targets.
	 *
	 * @param instance the object to write
	 * @return a stream for the object
	 */
	@Nonnull
	InputStream export(@Nonnull T instance);

}
