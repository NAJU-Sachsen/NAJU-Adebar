package de.naju.adebar.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import org.springframework.util.Assert;

/**
 * A change set consist of many {@link ChangeSetEntry ChangeSetEntries}
 * 
 * @author Rico Bergmann
 */
class ChangeSet implements Iterable<ChangeSetEntry> {

  private Set<ChangeSetEntry> entries;

  /**
   * Creates a new change set
   * 
   * @param entries the entries
   * @return the set
   */
  public static ChangeSet of(Collection<ChangeSetEntry> entries) {
    return new ChangeSet(entries);
  }

  /**
   * Default constructor, nothing special about it
   */
  private ChangeSet() {
    this.entries = new HashSet<>();
  }

  /**
   * Initializes the change set
   * 
   * @param entries the entries
   */
  private ChangeSet(Collection<ChangeSetEntry> entries) {
    Assert.notNull(entries, "Change set may not be null");
    Assert.noNullElements(entries.toArray(), "Change set may not contain null values");
    this.entries = new HashSet<>(entries);
  }

  /**
   * @return the entries
   */
  public Collection<ChangeSetEntry> getEntries() {
    return Collections.unmodifiableCollection(entries);
  }

  /**
   * @param entry the entry to check
   * @return whether the change set contains an entry with the same field
   */
  public boolean hasEntry(ChangeSetEntry entry) {
    return entries.contains(entry);
  }

  /**
   * Combines two change sets. This will basically merge the {@code other} change set into this one,
   * assuming that the other set is newer than this one.
   * 
   * @param other the other change set
   * @return
   */
  public ChangeSet mergeWith(ChangeSet other) {
    ArrayList<ChangeSetEntry> resultingEntries =
        new ArrayList<>(this.entries.size() + other.entries.size());

    for (ChangeSetEntry entry : this) {
      if (other.hasEntry(entry)) {
        resultingEntries.add(entry.combineWith(other.getEntryFor(entry.getField())));
      }
    }

    resultingEntries.trimToSize();
    return new ChangeSet(resultingEntries);
  }

  /**
   * @param field the field to search
   * @return the entry for the given field
   * @throws NoSuchElementException if no matching entry was found
   */
  public ChangeSetEntry getEntryFor(String field) {
    return entries.stream() //
        .filter(e -> e.getField().equals(field)) //
        .findAny() // there may only be one or none entry for a field
        .orElseThrow(() -> new NoSuchElementException("No entry for field " + field));
  }

  @Override
  public Iterator<ChangeSetEntry> iterator() {
    return entries.iterator();
  }

}
