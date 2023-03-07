package com.tinkoff.backend.database

import java.sql.DriverManager
import org.h2.Driver
import org.junit.jupiter.api.Test

class ConnectionTest {


    @Test
    fun createPostgres() {
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
//        val connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/", "postgres", "mysecretpassword")

        val connection = DriverManager.getConnection("jdbc:tc:postgresql:11:///test")

        val sql = "SELECT 4*4;"
        val result = connection.prepareStatement(sql).executeQuery()
        result.next()
        println(result.getInt(1))
    }

    @Test
    fun createH2() {
        val connection = Driver().connect("jdbc:h2:mem:testdb", null)

        val sql = "SELECT 4*4;"
        val result = connection.prepareStatement(sql).executeQuery()
        result.next()
        println(result.getInt(1))
    }




}