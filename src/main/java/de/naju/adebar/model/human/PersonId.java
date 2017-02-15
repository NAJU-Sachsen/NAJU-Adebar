package de.naju.adebar.model.human;

import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

/**
 * As we want to link {@link Person} instances with {@link Activist} and {@link Referent} objects, and need to persist
 * these associations, we somehow need to put them together. This is what {@code PersonId} is used for:
 * it is the common primary key of all these classes. For accessing single objects the manager classes should be used
 * @author Rico Bergmann
 * @see PersonManager
 * @see ActivistManager
 * @see ReferentManager
 * @see <a href="https://en.wikipedia.org/wiki/Unique_key">Primary keys</a>
 */
@Embeddable
class PersonId implements Serializable {
    @Column(unique=true) private final String id;

    /**
     * Just create a new identifier
     */
    public PersonId() {
        this.id = UUID.randomUUID().toString();
    }

    /**
     * Create an identifier using an existing one
     * @param id the existing id to use
     */
    public PersonId(String id) {
        Assert.notNull(id, "Id may not be null!");
        this.id = id;
    }

    /**
     * @return the identifier. As it should not be modified under no circumstances, it is {@code final}
     */
    public final String getId() {
        return id;
    }

    @Override  public String toString() {
        return id;
    }

}
