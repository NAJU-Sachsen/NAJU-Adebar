
/*
 * Ensures that each participant, activist and referent profile is
 * linked to a person
 */
ALTER TABLE participant
ADD FOREIGN KEY (id)
REFERENCES person(id);

ALTER TABLE activist
ADD FOREIGN KEY (id)
REFERENCES person(id);

ALTER TABLE referent
ADD FOREIGN KEY (id)
REFERENCES person(id);
