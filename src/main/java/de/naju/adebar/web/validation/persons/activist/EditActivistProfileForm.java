package de.naju.adebar.web.validation.persons.activist;

import de.naju.adebar.model.persons.details.JuleicaCard;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * POJO representation of the information inside the 'edit activist' form
 *
 * @author Rico Bergmann
 */
public class EditActivistProfileForm {

  private boolean juleica;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate juleicaExpiryDate;

  /**
   * Full constructor
   *
   * @param juleicaCard the activist's Juleica card. May be {@code null} if the person has no
   *     Juleica
   */
  public EditActivistProfileForm(JuleicaCard juleicaCard) {
    this.juleica = juleicaCard != null;

    if (juleicaCard != null) {
      this.juleicaExpiryDate = juleicaCard.getExpiryDate();
    }
  }

  /**
   * Default constructor
   */
  public EditActivistProfileForm() {
  }

  /**
   * @return whether the activist has a Juleica card
   */
  public boolean isJuleica() {
    return juleica;
  }

  /**
   * @param juleica whether the activist has a Juleica card
   */
  public void setJuleica(boolean juleica) {
    this.juleica = juleica;
  }

  /**
   * @return the expiry date of the activist's Juleica card. May be {@code null} if the person has
   *     no Juleica
   */
  public LocalDate getJuleicaExpiryDate() {
    return juleicaExpiryDate;
  }

  /**
   * @param juleicaExpiryDate the expiry date of the activist's Juleica card. May be {@code
   *     null} if the person has no Juleica
   */
  public void setJuleicaExpiryDate(LocalDate juleicaExpiryDate) {
    this.juleicaExpiryDate = juleicaExpiryDate;
  }

  @Override
  public String toString() {
    return "EditActivistProfileForm [" +
        "juleica=" + juleica +
        ", juleicaExpiryDate=" + juleicaExpiryDate +
        ']';
  }
}
