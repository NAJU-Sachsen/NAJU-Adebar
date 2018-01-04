
/*
 * This script adapts the user_account table to not only relate to a person
 * but to also maintain a copy of the data relevant for a user account
 */
alter table user_account
add column first_name varchar(255),
add column last_name varchar(255),
add column email varchar(255),
add column read_release_notes bit default false,
change column person associated_person varchar(255) not null;
