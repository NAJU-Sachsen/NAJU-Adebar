package de.naju.adebar.web.model.html;

import org.jspecify.annotations.NonNull;

public class NodeFactory {

	public static ElementId id(@NonNull String identifier) {
		return ElementId.of(identifier);
	}

}
