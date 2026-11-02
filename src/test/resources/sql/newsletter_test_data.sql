
/*
 * Inserts two subscribers:
 * - Jane (jane.doe@email.com), ID 1
 * - John (john@mail.com), ID 2
 * 
 * And one newsletter ("Newsletter", ID 1) with Jane and John as subscribers 
 */

INSERT INTO subscriber (id, first_name, email)
VALUES (1, 'Jane', 'jane.doe@email.com'), (2, 'John', 'john@mail.com');

INSERT INTO newsletter (id, name)
VALUES (1, 'Newsletter');

INSERT INTO newsletter_subscribers (newsletter_id, subscriber_id)
VALUES (1, 1), (1, 2);
