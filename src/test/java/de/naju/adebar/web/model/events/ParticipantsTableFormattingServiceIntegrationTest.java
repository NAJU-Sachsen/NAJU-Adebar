package de.naju.adebar.web.model.events;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import de.naju.adebar.Application;
import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.web.model.events.participation.table.ParticipantsTable;
import de.naju.adebar.web.model.events.participation.table.ParticipantsTableFormattingService;

@SpringBootTest(classes = Application.class)
public class ParticipantsTableFormattingServiceIntegrationTest {

	private static final String DEFAULT_EMAIL = "test@test.com";

	@Autowired
	private ParticipantsTableFormattingService formattingService;

	private Event eventMock = mock(Event.class);
	private Person participantMock = mock(Person.class);

	public ParticipantsTableFormattingServiceIntegrationTest() {
		when(participantMock.hasEmail()).thenReturn(true);
		when(participantMock.getEmail()).thenReturn(Email.of(DEFAULT_EMAIL));
	}

	@Test
	public void usesFormatterAccordingToColumnCode() {
		assertEquals(formattingService.getColumnValueFor(eventMock, participantMock,
				ParticipantsTable.COLUMN_EMAIL), DEFAULT_EMAIL);
	}

}
