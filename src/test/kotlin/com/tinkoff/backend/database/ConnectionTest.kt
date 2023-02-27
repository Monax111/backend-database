package com.tinkoff.backend.database

import java.sql.DriverManager
import org.junit.jupiter.api.Test

class ConnectionTest {


    @Test
    fun create() {
        /*
            docker run -d --name some-postgres -e POSTGRES_PASSWORD=mysecretpassword -p 5432:5432 postgres
            docker exec -it postgres-test psql -U postgres
            SELECT nspname FROM pg_catalog.pg_namespace;
        */
        val connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/", "postgres", "mysecretpassword")

//        val connection = DriverManager.getConnection("jdbc:tc:postgresql:11:///test")

//        val connection = DriverManager.getConnection("jdbc:h2:mem:testdb")

        val sql = "SELECT 4*4;"
        val result = connection.prepareStatement(sql).executeQuery()
        result.next()
        println(result.getInt(1))
    }


}