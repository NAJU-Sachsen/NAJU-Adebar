package de.naju.adebar.web.model.events.participation.table;

import de.naju.adebar.model.events.Event;
import java.util.Collections;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;

/**
 * Builder to fluently create new {@link ParticipantsTable} instances.
 * <p>
 * A builder will be initialized with the columns that should form the final table and may be reused
 * after the actual table was created. I.e. the selected columns will remain fixed and a table for
 * another event may be created.
 */
@Embeddable
public class ParticipantsTableBuilder {

  @ElementCollection
  private List<String> selectedColumns;

  /**
   * Full constructor.
   *
   * @param selectedColumns the columns that should be part of the final table
   */
  ParticipantsTableBuilder(List<String> selectedColumns) {
    this.selectedColumns = selectedColumns;
  }

  /**
   * Gets the columns that should form the table.
   */
  public List<String> getSelectedColumns() {
    return Collections.unmodifiableList(selectedColumns);
  }

  /**
   * Creates the table for a specific event.
   */
  public ParticipantsTable forEvent(Event event) {
    return new ParticipantsTable(event, selectedColumns);
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((selectedColumns == null) ? 0 : selectedColumns.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ParticipantsTableBuilder other = (ParticipantsTableBuilder) obj;
    if (selectedColumns == null) {
      if (other.selectedColumns != null) {
        return false;
      }
    } else if (!selectedColumns.equals(other.selectedColumns)) {
      return false;
    }
    return true;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "selectedColumns=" + selectedColumns;
  }

}
