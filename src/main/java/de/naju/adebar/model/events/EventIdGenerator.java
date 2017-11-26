package de.naju.adebar.model.events;

import java.util.Iterator;
import org.springframework.stereotype.Service;

/**
 * Generator for new event identifiers should be used instead of directly calling the constructor
 * 
 * @author Rico Bergmann
 */
@Service
public interface EventIdGenerator extends Iterator<EventId> {

}
