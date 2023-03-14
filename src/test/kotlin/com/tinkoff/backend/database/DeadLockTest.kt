package com.tinkoff.backend.database

import java.sql.Connection
import java.sql.DriverManager
import kotlin.concurrent.thread
import mu.KLogging
import org.junit.jupiter.api.Test

class DeadLockTest : KLogging() {

    @Test
    fun deadlock() {

        val connection1 = DriverManager.getConnection("jdbc:tc:postgresql:11:///test")
        val connection2 = DriverManager.getConnection("jdbc:tc:postgresql:11:///test")

        // Создаем структуру

        connection1.prepareStatement("""
            CREATE TABLE userentity
            (
                Id int,
                Name varchar(255),
                Age int
            );
        """.trimIndent()
        ).execute()
        logger.info("Создали таличку")

        // Добавляем данные
        connection1.prepareStatement("""
            INSERT INTO userentity VALUES
            (
                0,
                'Борис',
                20
            )
        """.trimIndent()
        ).execute()
        logger.info("Создали Бориса")


        // Добавляем данные
        connection1.prepareStatement("""
            INSERT INTO userentity VALUES
            (
                1,
                'Влад',
                33
            )
        """.trimIndent()
        ).execute()
        logger.info("Создали Влада")

        val thread1 = thread {
            with(connection1) {
                // открываем транзакции
                execute("BEGIN;")
                execute("UPDATE  userentity SET age = age +1  where name = 'Борис';")
                logger.info("Открыли транзакцию 1")

                Thread.sleep(200)

                execute("UPDATE  userentity SET age = age +1  where name = 'Влад';")
                execute("COMMIT;")
                logger.info("Закомитили транзакцию 1")

            }
        }
        val thread2 = thread {
            with(connection2) {
                // открываем транзакции
                execute("BEGIN;")
                execute("UPDATE  userentity SET age = age +1  where name = 'Влад';")
                logger.info("Открыли транзакцию 1")

                Thread.sleep(200)

                execute("UPDATE  userentity SET age = age +1  where name = 'Борис';")
                execute("COMMIT;")
                logger.info("Закомитили транзакцию 1")

            }
        }

        thread1.join()
        thread2.join()
    }
}

fun Connection.execute(sql: String) = prepareStatement(sql).execute()
