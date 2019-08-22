package de.naju.adebar.web.validation.events.participation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import de.naju.adebar.web.model.events.participation.table.ParticipantsTable;
import de.naju.adebar.web.model.events.participation.table.ParticipantsTableBuilder;

/**
 * POJO representation of the data inside the "edit participants table" form.
 * <p>
 * Each field corresponds to one of the column codes specified in {@link ParticipantsTable} and
 * toggles, whether this column should be included in the table.
 *
 * @author Rico Bergmann
 */
public class ParticipantsTableForm {

	private boolean name = true;
	private boolean email;
	private boolean phone;
	private boolean address;
	private boolean city;
	private boolean dateOfBirth;
	private boolean age;
	private boolean eatingHabits;
	private boolean healthImpairments;
	private boolean nabu;
	private boolean personRemarks;
	private boolean parents;
	private boolean parentsPrivatePhone;
	private boolean parentsLandlinePhone;
	private boolean parentsWorkPhone;
	private boolean registrationDate;
	private boolean registrationFormSent;
	private boolean registrationFormFilled;
	private boolean feePayed;
	private boolean arrival;
	private boolean departure;
	private boolean participationTime;
	private boolean participationRemarks;

	public ParticipantsTableForm() {}

	/**
	 * Creates a form based on an existing table. All fields will be toggled accordingly.
	 *
	 * @param table the table. May not be {@code null}.
	 */
	public ParticipantsTableForm(ParticipantsTable table) {
		table.getColumns().forEach(column -> {
			try {
				this.getClass().getDeclaredField(column).set(this, true);
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
					| SecurityException e) {
				String msg = String.format("Could not set field %s based on table %s", column, table);
				throw new IllegalStateException(msg, e);
			}
		});
	}

	public boolean isName() {
		return name;
	}

	public void setName(boolean name) {
		this.name = name;
	}

	public boolean isEmail() {
		return email;
	}

	public void setEmail(boolean email) {
		this.email = email;
	}

	public boolean isPhone() {
		return phone;
	}

	public void setPhone(boolean phone) {
		this.phone = phone;
	}

	public boolean isAddress() {
		return address;
	}

	public void setAddress(boolean address) {
		this.address = address;
	}

	public boolean isCity() {
		return city;
	}

	public void setCity(boolean city) {
		this.city = city;
	}

	public boolean isDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(boolean dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public boolean isAge() {
		return age;
	}

	public void setAge(boolean age) {
		this.age = age;
	}

	public boolean isEatingHabits() {
		return eatingHabits;
	}

	public void setEatingHabits(boolean eatingHabits) {
		this.eatingHabits = eatingHabits;
	}

	public boolean isHealthImpairments() {
		return healthImpairments;
	}

	public void setHealthImpairments(boolean healthImpairments) {
		this.healthImpairments = healthImpairments;
	}

	public boolean isNabu() {
		return nabu;
	}

	public void setNabu(boolean nabu) {
		this.nabu = nabu;
	}

	public boolean isPersonRemarks() {
		return personRemarks;
	}

	public void setPersonRemarks(boolean personRemarks) {
		this.personRemarks = personRemarks;
	}

	public boolean isParents() {
		return parents;
	}

	public void setParents(boolean parents) {
		this.parents = parents;
	}

	public boolean isParentsPrivatePhone() {
		return parentsPrivatePhone;
	}

	public void setParentsPrivatePhone(boolean parentsPrivatePhone) {
		this.parentsPrivatePhone = parentsPrivatePhone;
	}

	public boolean isParentsLandlinePhone() {
		return parentsLandlinePhone;
	}

	public void setParentsLandlinePhone(boolean parentsLandlinePhone) {
		this.parentsLandlinePhone = parentsLandlinePhone;
	}

	public boolean isParentsWorkPhone() {
		return parentsWorkPhone;
	}

	public void setParentsWorkPhone(boolean parentsWorkPhone) {
		this.parentsWorkPhone = parentsWorkPhone;
	}

	public boolean isRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(boolean registrationDate) {
		this.registrationDate = registrationDate;
	}

	public boolean isRegistrationFormSent() {
		return registrationFormSent;
	}

	public void setRegistrationFormSent(boolean registrationFormSent) {
		this.registrationFormSent = registrationFormSent;
	}

	public boolean isRegistrationFormFilled() {
		return registrationFormFilled;
	}

	public void setRegistrationFormFilled(boolean registrationFormFilled) {
		this.registrationFormFilled = registrationFormFilled;
	}

	public boolean isFeePayed() {
		return feePayed;
	}

	public void setFeePayed(boolean feePayed) {
		this.feePayed = feePayed;
	}

	public boolean isArrival() {
		return arrival;
	}

	public void setArrival(boolean arrival) {
		this.arrival = arrival;
	}

	public boolean isDeparture() {
		return departure;
	}

	public void setDeparture(boolean departure) {
		this.departure = departure;
	}

	public boolean isParticipationTime() {
		return participationTime;
	}

	public void setParticipationTime(boolean participationTime) {
		this.participationTime = participationTime;
	}

	public boolean isParticipationRemarks() {
		return participationRemarks;
	}

	public void setParticipationRemarks(boolean participationRemarks) {
		this.participationRemarks = participationRemarks;
	}

	/**
	 * Initializes a builder to create a participants table based on the data in this form. The event
	 * for the table has to be supplied from outside.
	 */
	public ParticipantsTableBuilder toParticipantsTable() {
		List<String> selectedColumns = new ArrayList<>(this.getClass().getFields().length);

		for (Field col : this.getClass().getDeclaredFields()) {
			try {
				if (col.getBoolean(this)) {
					selectedColumns.add(col.getName());
				}
			} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
				String msg = String.format("Could not access field %s based in form %s", col, this);
				throw new IllegalStateException(msg, e);
			}
		}
		return ParticipantsTable.with(selectedColumns);
	}

	@Override
	public String toString() {
		return "ParticipantsTableForm [" + "name=" + name + ", email=" + email + ", phone=" + phone
				+ ", address=" + address + ", city=" + city + ", dateOfBirth=" + dateOfBirth + ", age="
				+ age + ", eatingHabits=" + eatingHabits + ", healthImpairments=" + healthImpairments
				+ ", nabu=" + nabu + ", personRemarks=" + personRemarks + ", parents=" + parents
				+ ", parentsPrivatePhone=" + parentsPrivatePhone + ", parentsLandlinePhone="
				+ parentsLandlinePhone + ", parentsWorkPhone=" + parentsWorkPhone + ", registrationDate="
				+ registrationDate + ", registrationFormSent=" + registrationFormSent
				+ ", registrationFormFilled=" + registrationFormFilled + ", feePayed=" + feePayed
				+ ", arrival=" + arrival + ", departure=" + departure + ", participationTime="
				+ participationTime + ", participationRemarks=" + participationRemarks + ']';
	}
}
