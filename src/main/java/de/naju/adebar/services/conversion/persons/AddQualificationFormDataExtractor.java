package de.naju.adebar.services.conversion.persons;

import org.springframework.stereotype.Service;
import de.naju.adebar.model.persons.qualifications.Qualification;
import de.naju.adebar.web.validation.persons.AddQualificationForm;
import de.naju.adebar.web.validation.persons.AddQualificationForm.AddType;

/**
 * @author Rico Bergmann
 */
@Service
public class AddQualificationFormDataExtractor {

  public Qualification extractQualification(AddQualificationForm qualificationForm) {
    AddQualificationForm.AddType addType =
        AddQualificationForm.AddType.valueOf(qualificationForm.getAddType());

    Qualification qualification = null;
    if (addType == AddType.NEW) {
      qualification =
          new Qualification(qualificationForm.getName(), qualificationForm.getDescription());
    } else if (addType == AddType.EXISTING) {
      qualification = new Qualification(qualificationForm.getQualification(), "");
    }
    return qualification;
  }

}
