package de.naju.adebar.util.conversion.human;

import de.naju.adebar.controller.forms.human.AddQualificationForm;
import de.naju.adebar.model.human.Qualification;
import org.springframework.stereotype.Service;

/**
 * @author Rico Bergmann
 */
@Service
public class AddQualificationFormDataExtractor {

    public Qualification extractQualification(AddQualificationForm qualificationForm) {
        AddQualificationForm.AddType addType = AddQualificationForm.AddType.valueOf(qualificationForm.getAddType());

        Qualification qualification = null;
        switch (addType) {
            case NEW:
                qualification = new Qualification(qualificationForm.getName(), qualificationForm.getDescription());
                break;
            case EXISTING:
                qualification = new Qualification(qualificationForm.getName(), null);
                break;
        }
        return qualification;
    }

}
