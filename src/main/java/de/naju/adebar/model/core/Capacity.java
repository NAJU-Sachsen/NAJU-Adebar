package de.naju.adebar.model.core;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.springframework.util.Assert;
import de.naju.adebar.documentation.infrastructure.JpaOnly;

@Embeddable
public class Capacity implements Comparable<Capacity> {

  @Column(name = "value")
  private int value;

  public static Capacity of(int value) {
    return new Capacity(value);
  }

  private Capacity(int value) {
    Assert.isTrue(value >= 0, "Value may not be negative");
    this.value = value;
  }

  @JpaOnly
  private Capacity() {}

  public int getValue() {
    return value;
  }

  public boolean isSmallerThan(Capacity other) {
    return this.compareTo(other) < 0;
  }

  public boolean isLargerThan(Capacity other) {
    return this.compareTo(other) > 0;
  }

  @JpaOnly
  private void setValue(int value) {
    Assert.isTrue(value >= 0, "Value may not be negative");
    this.value = value;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(Capacity other) {
    return Integer.compare(this.value, other.value);
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
    result = prime * result + value;
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Capacity other = (Capacity) obj;
    if (value != other.value)
      return false;
    return true;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return Integer.toString(value);
  }

}
