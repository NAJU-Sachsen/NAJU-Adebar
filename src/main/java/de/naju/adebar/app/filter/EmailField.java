package de.naju.adebar.app.filter;

public class EmailField extends AbstractFilterableField {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.naju.adebar.app.filter.AbstractFilterableField#getType()
	 */
	@Override
	public FieldType getType() {
		return FieldType.EMAIL;
	}

}
