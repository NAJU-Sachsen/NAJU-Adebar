package de.naju.adebar.web.model.persons.filter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import de.naju.adebar.documentation.Unmodifiable;
import org.springframework.lang.NonNull;

public class WebFilterForm implements Iterable<FilterGroup> {

	public static WebFilterForm of(FilterGroup... filterGroups) {
		return new WebFilterForm(Arrays.asList(filterGroups));
	}

	private final List<FilterGroup> filterGroups;

	private WebFilterForm(List<FilterGroup> filterGroups) {
		super();
		this.filterGroups = filterGroups;
	}

	@Unmodifiable
	public List<FilterGroup> getFilterGroups() {
		return Collections.unmodifiableList(filterGroups);
	}

	@Override
	@NonNull
	public Iterator<FilterGroup> iterator() {
		return filterGroups.iterator();
	}

	@Override
	public String toString() {
		return filterGroups.toString();
	}

}
