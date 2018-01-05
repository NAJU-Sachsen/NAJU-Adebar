
/**
 * Adapts the participant table to the updated NABU membership information
 */
alter table participant
add column nabu_member bit default false;

update table participant
set nabu_member = (nabu_membership_number != "");

update table participant
set nabu_member = false,
    nabu_membership_number = ""
where nabu_member is null;
