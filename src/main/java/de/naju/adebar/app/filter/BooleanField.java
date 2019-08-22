package de.naju.adebar.app.filter;

public class BooleanField extends AbstractFilterableField {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.naju.adebar.app.filter.AbstractFilterableField#getType()
	 */
	@Override
	public FieldType getType() {
		return FieldType.BOOL;
	}

}
