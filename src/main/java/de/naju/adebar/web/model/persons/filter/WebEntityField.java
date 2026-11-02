package de.naju.adebar.web.model.persons.filter;

import de.naju.adebar.app.filter.EntityField;

public class WebEntityField extends EntityField {

	public static WebEntityField augment(EntityField actualField, String dialogTitle,
			String dialogAction) {
		return new WebEntityField(actualField, dialogTitle, dialogAction);
	}

	private final EntityField actualField;

	private final String dialogTitle;

	private final String dialogAction;

	public WebEntityField(EntityField actualField, String dialogTitle, String dialogAction) {
		super();
		this.actualField = actualField;
		this.dialogTitle = dialogTitle;
		this.dialogAction = dialogAction;
	}

	public String getDialogTitle() {
		return dialogTitle;
	}

	public String getDialogAction() {
		return dialogAction;
	}

	public EntityField getActualField() {
		return actualField;
	}

	@Override
	public String getName() {
		return actualField.getName();
	}

	@Override
	public String getDefaultName() {
		return actualField.getDefaultName();
	}

	@Override
	public String getID() {
		return actualField.getID();
	}

	@Override
	public Object getValue() {
		return actualField.getValue();
	}

	@Override
	public void setValue(Object value) {
		actualField.setValue(value);
	}

	@Override
	public void supplyId(String id) {
		actualField.supplyId(id);
	}

	@Override
	public void supplyName(String name) {
		actualField.supplyName(name);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((dialogAction == null) ? 0 : dialogAction.hashCode());
		result = prime * result + ((dialogTitle == null) ? 0 : dialogTitle.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof WebEntityField))
			return false;
		WebEntityField other = (WebEntityField) obj;
		if (dialogAction == null) {
			if (other.dialogAction != null)
				return false;
		} else if (!dialogAction.equals(other.dialogAction))
			return false;
		if (dialogTitle == null) {
			if (other.dialogTitle != null)
				return false;
		} else if (!dialogTitle.equals(other.dialogTitle))
			return false;
		return true;
	}

}
