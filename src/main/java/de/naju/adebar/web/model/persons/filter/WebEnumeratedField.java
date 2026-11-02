package de.naju.adebar.web.model.persons.filter;

import de.naju.adebar.app.filter.EnumeratedField;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WebEnumeratedField extends EnumeratedField {

	private final EnumeratedField actualField;
	private final List<WebEnumeratedFieldElement> elems;

	public WebEnumeratedField(EnumeratedField field,
			Function<Object, String> elementDisplayGenerator) {
		this.actualField = field;
		this.elems = new ArrayList<>(field.countValues());
		field.getValues().forEach(value -> {
			WebEnumeratedFieldElement fieldElem = new WebEnumeratedFieldElement(value,
					field.getValueNameFor(value), elementDisplayGenerator.apply(value));
			this.elems.add(fieldElem);
		});
	}

	public EnumeratedField getActualField() {
		return actualField;
	}

	@Override
	public Iterable<?> getValues() {
		return elems;
	}

	@Override
	public int countValues() {
		return elems.size();
	}

	@Override
	public Iterable<String> getValueNames() {
		return elems.stream().map(WebEnumeratedFieldElement::getName).collect(Collectors.toList());
	}

	@Override
	public String getValueNameFor(Object value) {
		return elems.stream().filter(elem -> elem.get().equals(value))
				.map(WebEnumeratedFieldElement::getName).findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

	@Override
	public String getDefaultName() {
		return actualField.getDefaultName();
	}

	public class WebEnumeratedFieldElement {

		private final Object value;
		private final String valueName;
		private final String display;

		public WebEnumeratedFieldElement(Object value, String name, String display) {
			this.value = value;
			this.valueName = name;
			this.display = display;
		}

		public Object get() {
			return value;
		}

		public String getName() {
			return valueName;
		}

		public String getDisplay() {
			return display;
		}

	}

}
