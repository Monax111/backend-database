package com.tinkoff.backend.database

class IndexTest


/*
    docker run -d --name some-postgres -e POSTGRES_PASSWORD=mysecretpassword -p 5432:5432 --cpus="0.1" postgres
    docker exec -it some-postgres psql -U postgres
    SELECT nspname FROM pg_catalog.pg_namespace;
    docker rm -f some-postgres
    pg_dump -U postgres -Fc > db.dump
    docker cp some-postgres:/db.dump .
    docker cp db.dump some-postgres:/db.dump
    pg_restore -U postgres -C -d postgres db.dump
*/

/*
select * from "public".userentity;

explain analyze select count(*) from "public".userentity where age < 50 ;

CREATE INDEX age_index ON "public".userentity (age);
drop index "public".name_index;

SELECT
    tablename,
    indexname,
    indexdef
FROM
    pg_indexes
WHERE
    schemaname = 'public'
ORDER BY
    tablename,
    indexname;



insert into "public".userentity ( "address", "age", "email", "name", "sex")
VALUES ( 'Москва2', 22, 'rye@mail.ru', 'Петька', 'Man');


CREATE EXTENSION pg_trgm;
CREATE INDEX reverse_email_index ON userentity USING GIN ( email gin_trgm_ops);
drop index "public".reverse_email_index;
explain select count(*) from "public".userentity where email like '%mail.ru';
explain select * from "public".userentity where email like '%mail.ru';
explain select count(*) from "public".userentity where reverse(email) like 'ur.liam%';


 */

