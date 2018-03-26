
/*
 * initializes the parent table with all known parents
 */

insert into parent (id, landline_phone, work_phone)
select distinct parent, null, null
from parents
where parent not in (select id from parent);

update person
set parent=true
where id in (select parent from parents);

update person
set parent=false
where id not in (select parent from parents);
