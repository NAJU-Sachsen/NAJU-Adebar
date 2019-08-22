package de.naju.adebar.web.model.html;

import org.springframework.lang.NonNull;

public class NodeFactory {

	public static ElementId id(@NonNull String identifier) {
		return ElementId.of(identifier);
	}

}
