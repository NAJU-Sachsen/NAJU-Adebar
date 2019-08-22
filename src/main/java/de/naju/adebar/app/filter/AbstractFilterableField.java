package de.naju.adebar.app.filter;

public abstract class AbstractFilterableField {

	public enum FieldType {
		BOOL, INT, FLOAT, STRING, DATE, EMAIL, ENUMERATED, ENTITY, COMPOUND
	}

	protected Object value;

	protected String suppliedId;
	protected String suppliedName;

	public abstract FieldType getType();

	public String getName() {
		return suppliedName != null ? suppliedName : getDefaultName();
	}

	public String getDefaultName() {
		return this.getClass().getSimpleName().toLowerCase();
	}

	public String getID() {
		return suppliedId != null ? suppliedId : this.getClass().getName();
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public void supplyId(String id) {
		this.suppliedId = id;
	}

	public void supplyName(String name) {
		this.suppliedName = name;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getID().hashCode();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		} else if (!(other instanceof AbstractFilterableField)) {
			return false;
		} else {
			return getID().equals(((AbstractFilterableField) other).getID());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName();
	}

}
