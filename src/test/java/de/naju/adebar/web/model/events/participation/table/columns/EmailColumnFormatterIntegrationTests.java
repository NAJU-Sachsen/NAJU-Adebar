package de.naju.adebar.web.model.events.participation.table.columns;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import de.naju.adebar.Application;
import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.persons.Person;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class EmailColumnFormatterIntegrationTests {

	private static final String FIELD_NOT_SET = "---";
	private static final String DEFAULT_EMAIL = "test@test.com";

	@Autowired
	private EmailColumnFormatter formatter;

	private Event eventMock = mock(Event.class);
	private Person participantWithEmailMock = mock(Person.class);
	private Person participantWithNoEmailMock = mock(Person.class);

	public EmailColumnFormatterIntegrationTests() {
		when(participantWithEmailMock.hasEmail()).thenReturn(true);
		when(participantWithEmailMock.getEmail()).thenReturn(Email.of(DEFAULT_EMAIL));
		when(participantWithNoEmailMock.hasEmail()).thenReturn(false);
	}

	@Test
	public void usesFieldNotSetPropertyIfNecessary() {
		assertEquals(formatter.formatColumnFor(participantWithNoEmailMock, eventMock), FIELD_NOT_SET);
	}

	@Test
	public void usesParticipantsEmailIfAvailable() {
		assertEquals(formatter.formatColumnFor(participantWithEmailMock, eventMock), DEFAULT_EMAIL);
	}

}
