package de.naju.adebar.web.services.events.participation.table;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.lang.WordUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import de.naju.adebar.app.storage.TempStorageService;
import de.naju.adebar.web.model.events.participation.table.ParticipantsTable;

/**
 * Simple service to serve {@link ParticipantsTable} instances as CSV files. These files are not
 * meant to be stored permanentely but will only be present for a short period of time.
 * <p>
 * If you are not interested in writing the file to some temporary storage but only in the CSV data,
 * you should take a look at {@link CSVWriter}.
 *
 * @author Rico Bergmann
 * @see ParticipantsTable
 * @see CSVWriter
 */
@Service
public class CSVServer {

	private static final String FILE_NAME_TEMPLATE = "ParticipantsTable-%03d-%s.csv";

	private final CSVWriter csvWriter;
	private final TempStorageService storageService;

	/**
	 * Full constructor. No parameter may be {@code null}.
	 */
	public CSVServer(CSVWriter csvWriter, TempStorageService storageService) {
		Assert.notNull(csvWriter, "Writer may not be null");
		Assert.notNull(storageService, "StorageService may not be null");
		this.csvWriter = csvWriter;
		this.storageService = storageService;
	}

	/**
	 * Provides a CSV file for some participants table.
	 */
	public File generateCSV(ParticipantsTable table) {
		InputStream csvInput = csvWriter.export(table);
		return storageService.store( //
				String.format(FILE_NAME_TEMPLATE, generateUniqueHandle(), adaptEventName(table)), //
				csvInput);
	}

	/**
	 * Prepares the name of an event so that it may be used in a file name.
	 * <p>
	 * This means turning the name to CamelCase and removing all non-alphanumeric characters.
	 */
	private String adaptEventName(ParticipantsTable table) {
		String titleCase = WordUtils.capitalizeFully(table.getEvent().getName());
		return titleCase.replaceAll("\\W", "");
	}

	/**
	 * Provides a unique part of the file name to prevent (or at least make it very unlikely) that two
	 * users request a table for the same event and the results get intermingled.
	 */
	private int generateUniqueHandle() {
		return ThreadLocalRandom.current().nextInt(1, 1000);
	}

}
