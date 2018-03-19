package de.naju.adebar.web.validation.persons.referent;

/**
 * POJO representation of the data submitted in the 'add qualification' dialog.
 *
 * <p> The dialog is divided in to parts: on for adding a completely new (unknown) qualification and
 * one for adding an existing qualification to the referent. Therefore these two cases are present
 * in this form as well (by adding attributes to represent both possible states). However part of
 * these attributes will always be {@code null} (or empty) as the data actually submitted was for
 * the other case
 *
 * @author Rico Bergmann
 */
public class AddQualificationForm {

  private String newQualificationName;
  private String newQualificationDescription;
  private String existingQualificationKey;

  //@formatter:off
  /**
   * Full constructor.
   * 
   * <p> Creating a form from existing data always means creating it for a new
   * qualification. That is because if this constructor is being used, an error must have occurred.
   * <br>
   * But there are only two types of errors possible when using this class:
   * 
   * <ol>
   *   <li>The ID of an existing qualification was invalid</li>
   *   <li>The data for the new qualification was invalid</li>
   * </ol>
   * 
   * If the first case occurs, there is no reason to create a form for that, because this is an internal error and a non-existent qualification may not be displayed on the frontend in any reasonable way.
   * <br>
   * If the second case occurs, we should actually display this data to the user and tell, why it was invalid.
   * 
   * <p> Therefore we will create a form with exactly this data. 
   *
   * @param newQualificationName the name of the qualification to add
   * @param newQualificationDescription the description of the qualification to add
   */
  //@formatter:on
  public AddQualificationForm(String newQualificationName,
      String newQualificationDescription) {
    this.newQualificationName = newQualificationName;
    this.newQualificationDescription = newQualificationDescription;
  }

  /**
   * Default constructor
   */
  public AddQualificationForm() {
  }

  /**
   * @return the name of the new qualification
   */
  public String getNewQualificationName() {
    return newQualificationName;
  }

  /**
   * @param newQualificationName the name of the new qualification
   */
  public void setNewQualificationName(String newQualificationName) {
    this.newQualificationName = newQualificationName;
  }

  /**
   * @return the description of the new qualification
   */
  public String getNewQualificationDescription() {
    return newQualificationDescription;
  }

  /**
   * @param newQualificationDescription the description of the new qualification
   */
  public void setNewQualificationDescription(String newQualificationDescription) {
    this.newQualificationDescription = newQualificationDescription;
  }

  /**
   * @return the identifier of the existing qualification
   */
  public String getExistingQualificationKey() {
    return existingQualificationKey;
  }

  /**
   * @param existingQualificationKey the identifier of the existing qualification
   */
  public void setExistingQualificationKey(String existingQualificationKey) {
    this.existingQualificationKey = existingQualificationKey;
  }

  /**
   * @return whether a new qualification should be added or whether it already exists
   */
  public AddType getAddType() {
    if (existingQualificationKey == null || existingQualificationKey.isEmpty()) {
      return AddType.NEW_QUALIFICATION;
    } else {
      return AddType.EXISTING_QUALIFICATION;
    }
  }

  /**
   * @return a shorter representation of the add-type
   *
   * @see #getAddType()
   */
  public String getAddTypeShort() {
    switch (getAddType()) {
      case NEW_QUALIFICATION:
        return "new";
      case EXISTING_QUALIFICATION:
        return "existing";
    }
    return "";
  }

  @Override
  public String toString() {
    return "AddQualificationForm [" +
        "newQualificationName='" + newQualificationName + '\'' +
        ", newQualificationDescription='" + newQualificationDescription + '\'' +
        ", existingQualificationKey='" + existingQualificationKey + '\'' +
        ']';
  }

  /**
   * The kind of qualifications that could be added
   */
  public enum AddType {
    NEW_QUALIFICATION,
    EXISTING_QUALIFICATION
  }

}
