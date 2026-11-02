package de.naju.adebar.app.filter;

public class CompoundField extends BooleanField {

	/*
	 * (non-Javadoc)
	 *
	 * @see de.naju.adebar.app.filter.AbstractFilterableField#getType()
	 */
	@Override
	public FieldType getType() {
		return FieldType.COMPOUND;
	}

}
