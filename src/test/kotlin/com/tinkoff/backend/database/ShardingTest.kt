package com.tinkoff.backend.database

import java.sql.DriverManager
import kotlin.random.Random
import mu.KLogging
import org.junit.jupiter.api.Test


class ShardingTest : KLogging() {

    val connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/", "postgres", "password")


    @Test
    fun example() {
        /*

            docker run -d --rm --name postgres-master -e POSTGRES_PASSWORD=password -p 5432:5432 --cpus="1" postgres
            docker run -d --rm --name postgres-slave1 -e POSTGRES_PASSWORD=password -p 5431:5432 --cpus="1" postgres
            docker exec -it postgres-master psql -U postgres

             CREATE EXTENSION postgres_fdw;

             CREATE SERVER slave1
             FOREIGN DATA WRAPPER postgres_fdw
             OPTIONS (host '127.0.0.1', port '5431', dbname 'postgres');

             CREATE USER MAPPING FOR postgres SERVER slave1 OPTIONS ( user 'postgres' , password 'password');

         */


        // Создание таблицы на стороннем сервере
        connection.prepareStatement(
            """
               CREATE FOREIGN TABLE IF NOT EXISTS measurement_old (
                city_id         int not null,
                logdate         date not null,
                peaktemp        int,
                unitsales       int
                )
                SERVER slave1
                OPTIONS( schema_name 'public', table_name 'measurement_old' )
            """.trimIndent()
        ).execute()

        // создание представления
        connection.prepareStatement(
            """
                CREATE OR REPLACE VIEW measurement_view as
                SELECT * from measurement_old
            """.trimIndent()
        ).execute()

    }

}
