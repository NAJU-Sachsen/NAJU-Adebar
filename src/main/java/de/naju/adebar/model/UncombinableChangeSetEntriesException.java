package de.naju.adebar.model;

import java.util.Arrays;
import java.util.Collections;

/**
 * Exception to indicate that two {@link ChangeSetEntry ChangeSetEntries} may not be combined
 * 
 * @author Rico Bergmann
 */
public class UncombinableChangeSetEntriesException extends RuntimeException {

  private static final long serialVersionUID = 57226328154376298L;

  private final transient ChangeSetEntry first;
  private final transient ChangeSetEntry second;

  /**
   * @param first the base entry
   * @param second the entry that should have been combined into the first one
   */
  public UncombinableChangeSetEntriesException(ChangeSetEntry first, ChangeSetEntry second) {
    this.first = first;
    this.second = second;
  }

  /**
   * @param msg the error message
   * @param first the base entry
   * @param second the entry that should have been combined into the first one
   */
  public UncombinableChangeSetEntriesException(String msg, ChangeSetEntry first,
      ChangeSetEntry second) {
    super(msg);
    this.first = first;
    this.second = second;
  }


  /**
   * @return the base entry
   */
  public final ChangeSetEntry getFirstEntry() {
    return first;
  }

  /**
   * @return the entry that should have been combined into the first one
   */
  public final ChangeSetEntry getSecondEntry() {
    return second;
  }

  /**
   * @return both entries
   */
  public Iterable<ChangeSetEntry> getEntries() {
    return Collections.unmodifiableList(Arrays.asList(first, second));
  }

  @Override
  public String toString() {
    return String.format("%s: %s for entries %s and %s ", //
        this.getClass().getSimpleName(), getMessage(), first, second);
  }

}
