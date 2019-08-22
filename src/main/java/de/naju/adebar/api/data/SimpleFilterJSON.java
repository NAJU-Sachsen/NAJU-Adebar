package de.naju.adebar.api.data;

public class SimpleFilterJSON {

	public static SimpleFilterJSON create(String id, String title, String description) {
		return new SimpleFilterJSON(id, title, description);
	}

	private final String id;
	private final String title;
	private final String description;

	SimpleFilterJSON(String id, String title, String description) {
		this.id = id;
		this.title = title;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof SimpleFilterJSON))
			return false;
		SimpleFilterJSON other = (SimpleFilterJSON) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SimpleFilterJSON [id=" + id + ", title=" + title + ", description=" + description + "]";
	}

}
