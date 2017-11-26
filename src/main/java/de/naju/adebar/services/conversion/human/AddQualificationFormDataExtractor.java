package de.naju.adebar.services.conversion.human;

import org.springframework.stereotype.Service;
import de.naju.adebar.controller.forms.human.AddQualificationForm;
import de.naju.adebar.model.human.Qualification;

/**
 * @author Rico Bergmann
 */
@Service
public class AddQualificationFormDataExtractor {

  public Qualification extractQualification(AddQualificationForm qualificationForm) {
    AddQualificationForm.AddType addType =
        AddQualificationForm.AddType.valueOf(qualificationForm.getAddType());

    Qualification qualification = null;
    switch (addType) {
      case NEW:
        qualification =
            new Qualification(qualificationForm.getName(), qualificationForm.getDescription());
        break;
      case EXISTING:
        qualification = new Qualification(qualificationForm.getQualification(), "");
        break;
    }
    return qualification;
  }

}
