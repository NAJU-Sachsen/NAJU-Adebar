/*
 * Script to migrate the datbase from Adebar v0.5.X to v0.6.0
 */

/*
 * =================================================
 *    alter participants gender
 * =================================================
 */
alter table participant
  add column tmp_gender varchar(123) default null;

update participant
set tmp_gender = 'FEMALE'
where gender = 0;

update participant
set tmp_gender = 'MALE'
where gender = 1;

alter table participant
  drop column gender;

alter table participant
  change column tmp_gender gender varchar(123) default null;

/*
 * =================================================
 *    create new tables
 * =================================================
 */
create table room_specification (
  dtype                      varchar(31) not null,
  id                         bigint(20)  not null,
  extra_space_for_counselors bit(1) default null,
  primary key (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

create table accommodation_fallback_rooms (
  spec_id    bigint(20) not null,
  beds_count int(11)    not null,
  gender     varchar(123) default null,
  key FKxrx8hioaik4fwm14nqvqofrn (spec_id),
  constraint FKxrx8hioaik4fwm14nqvqofrn foreign key (spec_id) references room_specification (id)
)
  ENGINE = InnoDB
  default CHARSET = utf8;


create table accommodation_flex_rooms (
  spec_id    bigint(20) not null,
  beds_count int(11)    not null,
  gender     varchar(123) default null,
  key FK5a6htui5pdylytwc2k5iria1x (spec_id),
  constraint FK5a6htui5pdylytwc2k5iria1x foreign key (spec_id) references room_specification (id)
)
  ENGINE = InnoDB
  default CHARSET = utf8;

create table accommodation_rooms (
  spec_id    bigint(20) not null,
  beds_count int(11)    not null,
  gender     varchar(123) default null,
  key FKfr60b4eqyfr0u5w2pl0tdk2rg (spec_id),
  constraint FKfr60b4eqyfr0u5w2pl0tdk2rg foreign key (spec_id) references room_specification (id)
)
  ENGINE = InnoDB
  default CHARSET = utf8;

create table event_arrival_options (
  event          varchar(255) not null,
  arrival_option varchar(255) default null,
  key FKg5o7nc2hjt07ij7jw3gyfhnge (event),
  constraint FKg5o7nc2hjt07ij7jw3gyfhnge foreign key (event) references event (id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

/*
 * =================================================
 *    adapt existing tables
 * =================================================
 */
alter table event_participants
  add column arrival varchar(255) default null,
  add column departure varchar(255) default null,
  add column first_night int(11) default 1,
  add column last_night int(11) default null,
  add column registration_date datetime(6) default null,
  add column form_sent bit(1) default false,
  add column go_home_singly bit(1) default false,
  drop foreign key FK6t8mkuh9bfo4uf71pwixvycet;

update event_participants
set last_night = datediff(
    (select end_time
     from event
     where id = event_id),
    (select start_time
     from event
     where id = event_id)
);

alter table counselors
  add column arrival varchar(255) default null,
  add column departure varchar(255) default null,
  add column first_night int(11) default 1,
  add column last_night int(11) default null,
  add column remarks varchar(511) default '';

update counselors
set last_night = datediff(
    (select end_time
     from event
     where id = event_id),
    (select start_time
     from event
     where id = event_id)
);

alter table event
  add column booked_out bit(1) default false,
  add column participants_limit int(11) default null,
  add column accommodation bigint(20) default null,
  add column flexible_participation_times bit(1) default false,
  add constraint FKlutila95e0m5i8h6i09pityyc foreign key (accommodation) references room_specification (id);

update event e
set participants_limit = (select participants_limit
                          from participants_list pl
                          where e.id = pl.id);

update event
set participants_limit = null
where participants_limit > 500;

alter table event_reservations
  add column id bigint(20) not null auto_increment key,
  add constraint FKb4y7yvtseapsselogk3wrhev6 foreign key (event_id) references event (id),
  drop foreign key FKlndq0qync66f9opxpsld3b5ph;

update event_reservations
set contact_email = null
where contact_email = '';

alter table event_waiting_list
  add constraint FKl3j6v02pvko4x7rw4lqptwlwb foreign key (event_id) references event (id),
  drop foreign key FKq4cfaehxmf2a9itmtnf459t7q;

alter table event_to_contact
  change contact_info contact_info varchar(511) default null;

drop table participants_list;
drop table event_lectures;
