package de.naju.adebar.app.filter;

/**
 * @author Rico Bergmann
 */
public abstract class StringField extends AbstractFilterableField {

	public abstract boolean isLargeText();

	/*
	 * (non-Javadoc)
	 *
	 * @see de.naju.adebar.app.filter.AbstractFilterableField#getType()
	 */
	@Override
	public FieldType getType() {
		return FieldType.STRING;
	}

}
