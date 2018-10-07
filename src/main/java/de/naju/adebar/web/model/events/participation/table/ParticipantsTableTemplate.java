package de.naju.adebar.web.model.events.participation.table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "participantsTableTemplate")
public class ParticipantsTableTemplate {

  @Id
  @Column(name = "createdBy")
  private String createdBy;

}
