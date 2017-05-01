package de.naju.adebar.util.conversion;

import de.naju.adebar.model.human.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Basic testing of the person conversions
 * @author Rico Bergmann
 * @see PersonConverter
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Component
public class PersonConverterUnitTest {
    private Address hansAddress = new Address("zu Hause 3", "01234", "Nirgends");
    private Address clausAddress = new Address("Hinter der Boje 7", "55555", "Aufm Meer");
    private Address bertaAddress = new Address("Bei Mir 1", "98765", "Entenhausen");
    private Person hans = new Person("Hans", "Wurst", "hans.wurst@web.de", "", Gender.MALE, hansAddress, LocalDate.now().minusDays(3));
    private Person claus = new Person("Claus", "Störtebecker", "derkaeptn@meermensch.de", "", Gender.MALE, clausAddress, LocalDate.of(1635, 3, 5));
    private Person berta = new Person("Berta", "Beate", "bb@gmx.net", "", Gender.FEMALE, bertaAddress, LocalDate.now().minusYears(23));
    private List<Person> persons = Arrays.asList(hans, claus, berta);
    private List<Activist> activists;
    private List<Referent> referents;

    @Autowired private PersonManager personManager;
    @Autowired private ActivistManager activistManager;
    @Autowired private ReferentManager referentManager;
    @Autowired private PersonRepository personRepo;

    private PersonConverter streamConverter;

    @Before
    public void setUp() {
        for (Person p : persons) {
            personManager.savePerson(p);
        }

        Activist hansActivist = activistManager.createActivistForPerson(hans);
        Activist clausActivist = activistManager.createActivistForPerson(claus);
        Activist bertaActivist = activistManager.createActivistForPerson(berta);
        activists = Arrays.asList(hansActivist, clausActivist, bertaActivist);

        Referent hansReferent = referentManager.createReferentForPerson(hans);
        Referent clausReferent = referentManager.createReferentForPerson(claus);
        Referent bertaReferent = referentManager.createReferentForPerson(berta);
        referents = Arrays.asList(hansReferent, clausReferent, bertaReferent);
    }


    @Test public void testConvertActivists() {
        streamConverter = new PersonConverter(personRepo);
        Assert.assertArrayEquals("Arrays should be equal", persons.toArray(),
                streamConverter.convertActivistStream(activists.stream()).toArray());
    }

    @Test public void testConvertReferents() {
        streamConverter = new PersonConverter(personRepo);
        Assert.assertArrayEquals("Arrays should be equal", persons.toArray(),
                streamConverter.convertReferentStream(referents.stream()).toArray());
    }

}
