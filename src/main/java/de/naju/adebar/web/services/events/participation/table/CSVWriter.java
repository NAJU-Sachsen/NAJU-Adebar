package de.naju.adebar.web.services.events.participation.table;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import de.naju.adebar.app.storage.Exporter;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.web.model.events.participation.table.ParticipantsTable;
import de.naju.adebar.web.model.events.participation.table.ParticipantsTableFormattingService;

/**
 * Service to write a {@link ParticipantsTable} to some (virtual) CSV file.
 *
 * @author Rico Bergmann
 * @see ParticipantsTable
 * @see CSVServer
 */
@Service
public class CSVWriter implements Exporter<ParticipantsTable> {

  private static final CSVFormat USED_FORMAT = CSVFormat.EXCEL;

  private final ParticipantsTableFormattingService formatter;
  private final MessageSource messageSource;

  /**
   * Full constructor. No parameter may be {@code null}.
   */
  public CSVWriter(ParticipantsTableFormattingService formatter, MessageSource messageSource) {
    Assert.notNull(formatter, "Formatter may not be null");
    Assert.notNull(messageSource, "MessageSource may not be null");
    this.formatter = formatter;
    this.messageSource = messageSource;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.naju.adebar.app.storage.Exporter#export(java.lang.Object)
   */
  @Override
  public InputStream export(ParticipantsTable instance) {
    final Event event = instance.getEvent();
    try {

      StringWriter writer = new StringWriter();
      CSVPrinter printer = new CSVPrinter(writer, USED_FORMAT);

      // write columns in the header
      for (String col : instance.getColumns()) {
        if (formatter.formatterIsApplicable(event, col)) {
          String columnName = messageSource.getMessage("col." + col, new Object[] {},
              LocaleContextHolder.getLocale());
          printer.print(columnName);
        }
      }
      printer.println();

      // write the participants as rows
      for (Person participant : instance.getParticipants()) {
        for (String col : instance.getColumns()) {
          if (formatter.formatterIsApplicable(event, col)) {
            printer.print(formatter.getColumnValueFor(event, participant, col));
          }
        }
        printer.println();
      }
      return new ByteArrayInputStream(writer.toString().getBytes());
    } catch (IOException e) {
      throw new CSVExportException("Could not export table " + instance, e);
    }
  }

}
