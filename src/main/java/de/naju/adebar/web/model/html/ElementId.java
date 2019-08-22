package de.naju.adebar.web.model.html;

import org.springframework.lang.NonNull;

public class ElementId {

	public static ElementId of(@NonNull String identifier) {
		return new ElementId(identifier);
	}

	private final String id;

	public ElementId(@NonNull String identifier) {
		this.id = identifier;
	}

	@NonNull
	public String get() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ElementId))
			return false;
		ElementId other = (ElementId) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return id;
	}

}
