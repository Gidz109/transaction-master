insert into transactionmaster.tmsch."user"(name, surname) values ('John', 'Doe');
insert into transactionmaster.tmsch."user"(name, surname) values ('Jane', 'Doe');
insert into transactionmaster.tmsch.authentication(username, retry_count, locked, user_id) values ('johndoe', 0, false, 1);
insert into transactionmaster.tmsch.authentication(username, retry_count, locked, user_id) values ('janedoe', 0, false, 2);