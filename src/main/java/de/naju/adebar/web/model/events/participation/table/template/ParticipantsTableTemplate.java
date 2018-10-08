package de.naju.adebar.web.model.events.participation.table.template;

import de.naju.adebar.app.security.user.Username;
import de.naju.adebar.documentation.infrastructure.JpaOnly;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.util.Assert2;
import de.naju.adebar.web.model.events.participation.table.ParticipantsTable;
import de.naju.adebar.web.model.events.participation.table.ParticipantsTableBuilder;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import org.springframework.data.annotation.Transient;
import org.springframework.util.Assert;

/**
 * A {@code Template} is a easy way to customize participants tables. It stores which columns should
 * be shown and may be applied to existing events to display them. Templates should be used if the
 * same columns are selected over and over again.
 * <p>
 * For instance activists that take care of diets for events frequently may find it helpful to
 * create a template that shows eating habits and health impairments of the participants.
 * <p>
 * Each template will be bound to a specific {@link de.naju.adebar.app.security.user.UserAccount
 * UserAccount} and may only be accessed by that user.
 *
 * @author Rico Bergmann
 * @see ParticipantsTable
 */
@Entity(name = "participantsTableTemplate")
public class ParticipantsTableTemplate implements Serializable {

  private static final long serialVersionUID = -6662753208382590518L;

  /*
   * The design of this class is a bit awkward as JPA does not support composite primary keys
   * directly. Thus the PK data has to be moved into a separate ID type.
   * However we also do not really want to deal with this type and keep its existence completely
   * hidden for the outside.
   */

  @EmbeddedId
  private ParticipantsTableTemplateId id;

  @ElementCollection
  @CollectionTable(joinColumns = {@JoinColumn(name = "createdBy"), @JoinColumn(name = "name")})
  private List<String> columns;

  /**
   * Constructs a new template.
   *
   * @param user the user who wants to save the template. May never be {@code null}.
   * @return a builder to take care of the further construction
   */
  public static TemplateBuilder by(Username user) {
    return new TemplateBuilder(user);
  }

  /**
   * Full constructor.
   *
   * @param createdBy the person to whom this template belongs. May never be {@code null}.
   * @param name the template's name. May never be {@code null} nor empty.
   * @param columns the {@link ParticipantsTable#ALL_COLUMNS columns} the table should contain.
   *     May not be {@code null} nor may it contain any {@code null} elements.
   */
  private ParticipantsTableTemplate(Username createdBy, String name, List<String> columns) {
    Assert.notNull(createdBy, "Creator may not be null");
    Assert.hasText(name, "Name may not be null nor empty");
    Assert.notNull(columns, "Columns may not be null");
    Assert2.noNullElements(columns, "No column may be null");
    this.id = new ParticipantsTableTemplateId(createdBy, name);
    this.columns = columns;
  }

  /**
   * Default constructor just for JPA's sake.
   */
  @JpaOnly
  private ParticipantsTableTemplate() {}

  /**
   * Gets the creator of this template. The user should be the only one able to access it.
   */
  @Transient
  public Username getCreatedBy() {
    return id.getCreatedBy();
  }

  /**
   * Gets the name of this template.
   */
  @Transient
  public String getName() {
    return id.getName();
  }

  /**
   * Gets the columns that the resulting table should contain.
   */
  public Collection<String> getColumns() {
    return Collections.unmodifiableCollection(columns);
  }

  /**
   * Generates the table for a specific event.
   *
   * @param event the event. May not be {@code null}.
   * @return the resulting table
   */
  public ParticipantsTable applyTo(Event event) {
    return ParticipantsTable //
        .with(columns) //
        .forEvent(event);
  }

  /**
   * Gets the PK wrapper of this template due to JPA not supporting composite PK's directly.
   */
  @JpaOnly
  private ParticipantsTableTemplateId getId() {
    return id;
  }

  /**
   * Sets the PK wrapper of this template due to JPA not supporting composite PK's directly.
   *
   * @param id may never be {@code null}
   */
  @JpaOnly
  private void setId(ParticipantsTableTemplateId id) {
    this.id = id;
  }

  /**
   * Sets the columns the resulting table should show.
   *
   * @param columns may never be {@code null} or contain {@code null} elements
   */
  @JpaOnly
  private void setColumns(List<String> columns) {
    this.columns = columns;
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
    result = prime * result + ((columns == null) ? 0 : columns.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
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
    ParticipantsTableTemplate other = (ParticipantsTableTemplate) obj;
    if (columns == null) {
      if (other.columns != null) {
        return false;
      }
    } else if (!columns.equals(other.columns)) {
      return false;
    }
    if (id == null) {
      return other.id == null;
    } else {
      return id.equals(other.id);
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "ParticipantsTableTemplate [id=" + id + ", columns=" + columns + "]";
  }

  /**
   * Builder to construct new templates through a nice API.
   */
  public static class TemplateBuilder {

    private final Username creator;
    private String name;

    /**
     * Primary constructor.
     *
     * @param creator the person who created the template. May not be {@code null}.
     */
    private TemplateBuilder(Username creator) {
      this.creator = creator;
    }

    /**
     * Sets the name of the template.
     *
     * @param name the name. May not be {@code null} nor empty. Names need to be unique per
     *     user.
     * @return this builder to enable chaining methods fluently
     */
    public TemplateBuilder called(String name) {
      this.name = name;
      return this;
    }

    /**
     * Sets the columns that should be shown in the tables which are generated using the template.
     *
     * @param content the columns. They need to correspond to the elements of {@link
     *     ParticipantsTable#ALL_COLUMNS}.
     * @return the resulting template
     * @see ParticipantsTable
     */
    public ParticipantsTableTemplate with(ParticipantsTableBuilder content) {
      return new ParticipantsTableTemplate(creator, name, content.getSelectedColumns());
    }

  }

}
