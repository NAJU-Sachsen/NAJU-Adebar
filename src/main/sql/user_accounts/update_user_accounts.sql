
/*
 * Updates the personal information of each user account
 * from its associated person 
 */
update user_account
inner join person
    on person.id = user_account.associated_person
set user_account.first_name = person.first_name,
    user_account.last_name = person.last_name,
    user_account.email = person.email;
