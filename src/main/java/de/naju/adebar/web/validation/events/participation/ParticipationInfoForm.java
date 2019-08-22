package de.naju.adebar.web.validation.events.participation;

import com.google.common.collect.Lists;
import de.naju.adebar.model.core.Age;
import de.naju.adebar.model.core.Capacity;
import de.naju.adebar.model.events.ArrivalOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.javamoney.moneta.Money;

public class ParticipationInfoForm {

	private Capacity participantsLimit;
	private Age minParticipantAge;
	private Money intParticipationFee;
	private Money extParticipationFee;
	private List<ArrivalOption> arrivalOptions = new ArrayList<>();

	public ParticipationInfoForm() {}

	public ParticipationInfoForm(Capacity participantsLimit, Age minParticipantAge,
			Money intParticipationFee, Money extParticipationFee,
			Collection<ArrivalOption> arrivalOptions) {
		this.participantsLimit = participantsLimit;
		this.minParticipantAge = minParticipantAge;
		this.intParticipationFee = intParticipationFee;
		this.extParticipationFee = extParticipationFee;
		this.arrivalOptions = Lists.newArrayList(arrivalOptions);
	}

	public Capacity getParticipantsLimit() {
		return participantsLimit;
	}

	public void setParticipantsLimit(Capacity participantsLimit) {
		this.participantsLimit = participantsLimit;
	}

	public Age getMinParticipantAge() {
		return minParticipantAge;
	}

	public void setMinParticipantAge(Age minParticipantAge) {
		this.minParticipantAge = minParticipantAge;
	}

	public Money getIntParticipationFee() {
		return intParticipationFee;
	}

	public void setIntParticipationFee(Money intParticipationFee) {
		this.intParticipationFee = intParticipationFee;
	}

	public Money getExtParticipationFee() {
		return extParticipationFee;
	}

	public void setExtParticipationFee(Money extParticipationFee) {
		this.extParticipationFee = extParticipationFee;
	}

	public List<ArrivalOption> getArrivalOptions() {
		return arrivalOptions;
	}

	public void setArrivalOptions(List<ArrivalOption> arrivalOptions) {
		this.arrivalOptions = arrivalOptions;
	}

	@Override
	public String toString() {
		return "ParticipationInfoForm [" + "participantsLimit=" + participantsLimit
				+ ", minParticipantAge=" + minParticipantAge + ", intParticipationFee="
				+ intParticipationFee + ", extParticipationFee=" + extParticipationFee + ", arrivalOptions="
				+ arrivalOptions + ']';
	}

}
