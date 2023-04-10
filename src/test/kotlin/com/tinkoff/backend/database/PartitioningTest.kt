package com.tinkoff.backend.database

import java.sql.DriverManager
import kotlin.random.Random
import mu.KLogging
import org.junit.jupiter.api.Test


class PartitioningTest : KLogging() {

    /*

    docker run -d --rm --name postgres-master -e POSTGRES_PASSWORD=password -p 5432:5432 --cpus="1" postgres
    docker exec -it postgres-master psql -U postgres

    */

    val connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/", "postgres", "password")


    @Test
    fun example() {

        connection.prepareStatement(
            """
                CREATE TABLE IF NOT EXISTS measurement (
                city_id         int not null,
                logdate         date not null,
                peaktemp        int,
                unitsales       int
                );
            """.trimIndent()
        ).execute()

        // Заполняем данные
        repeat(10) {
            connection.prepareStatement(
                """
                INSERT INTO measurement VALUES
                (
                    0,
                    DATE '2021-${Random.nextInt(1, 12)}-${Random.nextInt(1, 29)}',
                    ${Random.nextInt(16, 25)},
                    ${Random.nextInt(1, 5)}
                )
                """.trimIndent()
            ).execute()
        }
    }


    @Test
    fun manualPartition() {

        connection.prepareStatement(
            """
                CREATE TABLE IF NOT EXISTS measurement_year2021_m06 (
                city_id         int not null,
                logdate         date not null,
                peaktemp        int,
                unitsales       int
                );
            """.trimIndent()
        ).execute()

        for (year in 2021..2022) {
            for (month in 1..12) {
                connection.prepareStatement(
                    """
                        CREATE TABLE IF NOT EXISTS measurement_${year}_${month} (
                        city_id         int not null,
                        logdate         date not null,
                        peaktemp        int,
                        unitsales       int
                        );
                    """.trimIndent()
                ).execute()
            }
        }

        // ручное создание индексов в партициях

        // создание представления
        connection.prepareStatement(
            """
                CREATE OR REPLACE VIEW measurement_view as
                SELECT * from measurement_2021_1
                union all
                SELECT * from measurement_2021_2
                union all
                SELECT * from measurement_2021_2
            """.trimIndent()
        ).execute()
    }

    @Test
    fun inheritsPartition() {

        // создаем родительскую таблицу
        connection.prepareStatement(
            """
                CREATE TABLE IF NOT EXISTS measurement (
                city_id         int not null,
                logdate         date not null,
                peaktemp        int,
                unitsales       int
                );
            """.trimIndent()
        ).execute()


        // создаем партиции
        for (year in 2021..2022) {
            for (month in 1..11) {
                connection.prepareStatement(
                    """
                    CREATE TABLE IF NOT EXISTS measurement_${year}_$month (
                        CHECK ( logdate >= DATE '${year}-$month-01' AND logdate < DATE '${year}-${month + 1}-01' )
                    ) INHERITS (measurement);
                    """.trimIndent()
                ).execute()
            }
        }

        // ручное создание индексов в партициях


        // создаем функцию
        connection.prepareStatement(
            """
                CREATE OR REPLACE FUNCTION measurement_insert_trigger()
                    RETURNS TRIGGER AS ${'$'}${'$'}
                    BEGIN
                        INSERT INTO measurement_2022_11 VALUES (NEW.*);
                        RETURN NULL;
                    END;
                    ${'$'}${'$'}
                    LANGUAGE plpgsql;
            """.trimIndent()
        ).execute()


        // создаем триггер
        connection.prepareStatement(
            """
                CREATE OR REPLACE TRIGGER insert_measurement_trigger
                    BEFORE INSERT ON measurement
                    FOR EACH ROW EXECUTE PROCEDURE measurement_insert_trigger();
            """.trimIndent()
        ).execute()


        // Заполняем данные
        repeat(10) {
            connection.prepareStatement(
                """
                INSERT INTO measurement VALUES
                (
                    0,
                    DATE '2022-11-${Random.nextInt(1, 29)}',
                    ${Random.nextInt(16, 25)},
                    ${Random.nextInt(1, 5)}
                )
                """.trimIndent()
            ).execute()
        }

        // исключение по умолчанию

    }

    @Test
    fun declarativePartition() {

        // создаем таблицу
        connection.prepareStatement(
            """
                CREATE TABLE IF NOT EXISTS measurement (
                    city_id         int not null,
                    logdate         date not null,
                    peaktemp        int,
                    unitsales       int
                ) PARTITION BY RANGE (logdate);
            """.trimIndent()
        ).execute()


        // создаем партиции
        for (year in 2021..2022) {
            for (month in 1..11) {
                connection.prepareStatement(
                    """
                    CREATE TABLE IF NOT EXISTS measurement_${year}_$month PARTITION OF measurement
                        FOR VALUES FROM ('${year}-$month-01') TO ('${year}-${month + 1}-01');
                    """.trimIndent()
                ).execute()
            }
        }

        // создаем индексы

        // Заполняем данные
        repeat(10) {
            connection.prepareStatement(
                """
                INSERT INTO measurement VALUES
                (
                    0,
                    DATE '2022-${Random.nextInt(1, 11)}-${Random.nextInt(1, 29)}',
                    ${Random.nextInt(16, 25)},
                    ${Random.nextInt(1, 5)}
                )
                """.trimIndent()
            ).execute()
        }

        // исключение по умолчанию
    }

}
