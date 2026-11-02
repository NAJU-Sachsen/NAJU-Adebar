package de.naju.adebar.app.filter;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import com.google.common.collect.Lists;

// TODO rewrite this class from the ground up. It is as ugly as anybody could guess.

public abstract class EnumeratedField extends AbstractFilterableField {

	public abstract Iterable<?> getValues();

	public int countValues() {
		int counter = 0;
		for (Object __ : getValues()) {
			counter++;
		}
		return counter;
	}

	public Iterable<String> getValueNames() {
		List<?> values = Lists.newArrayList(getValues());
		return values.stream() //
				.map(Object::toString) //
				.collect(Collectors.toList());
	}

	public String getValueNameFor(Object value) {
		return value.toString().toLowerCase();
	}

	public Object getValueFor(String name) {
		List<?> values = Lists.newArrayList(getValues());
		return values.stream() //
				.filter(value -> ((Object) value).toString().toLowerCase().equals(name.toLowerCase())) //
				.findFirst() //
				.orElseThrow(IllegalArgumentException::new);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.naju.adebar.app.filter.AbstractFilterableField#getType()
	 */
	@Override
	public FieldType getType() {
		return FieldType.ENUMERATED;
	}

	public static class EnumField<E extends Enum<E>> extends EnumeratedField {

		protected final Class<E> values;
		protected final List<String> valueNames;
		protected final EnumSet<?> valueSet;

		protected EnumField(Class<E> values) {
			this.values = values;
			this.valueNames = EnumSet.allOf(values).stream() //
					.map(val -> val.name().toLowerCase()) //
					.collect(Collectors.toList());
			this.valueSet = EnumSet.allOf(values);
		}

		public static <E extends Enum<E>> EnumField<E> of(Class<E> theEnum) {
			return new EnumField<>(theEnum);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.naju.adebar.app.filter.EnumeratedField#getValues()
		 */
		@Override
		public Iterable<?> getValues() {
			return valueSet;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.naju.adebar.app.filter.EnumeratedField#countValues()
		 */
		@Override
		public int countValues() {
			return valueSet.size();
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.naju.adebar.app.filter.EnumeratedField#getValueNames()
		 */
		@Override
		public Iterable<String> getValueNames() {
			return valueNames;
		}

	}

}
