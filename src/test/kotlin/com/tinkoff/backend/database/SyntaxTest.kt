package com.tinkoff.backend.database

import java.sql.DriverManager
import mu.KLogging
import org.junit.jupiter.api.Test

//https://www.w3schools.com/sql/
class SyntaxTest : KLogging() {

    @Test
    fun example() {
        val connection = DriverManager.getConnection("jdbc:h2:mem:testdb")

        // Создаем структуру
        connection.prepareStatement("""
            CREATE TABLE Author
            (
                Id int,
                Name varchar(255),
                Birth int,
                Country varchar(255)
            );
        """.trimIndent()
        ).execute()

        connection.prepareStatement("""
            CREATE TABLE Book
            (
                Id int,
                AuthorId int,
                Name varchar(255),
                Year int,
                Price int
            );
        """.trimIndent()
        ).execute()

        // Заполняем данные
        connection.prepareStatement("""
            INSERT INTO Author VALUES
            (
                0,
                'Роберт Мартин',
                1952,
                'США'
            )
        """.trimIndent()
        ).execute()

        connection.prepareStatement("""
            INSERT INTO Book VALUES
            (
                0,
                0,
                'Чистый код',
                2008,
                938
            )
        """.trimIndent()
        ).execute()

        // Ищем данные
        val result = connection.prepareStatement("""
            SELECT * FROM Book 
        """.trimIndent()
        ).executeQuery()


            // Ищем данные
//            val result = connection . prepareStatement ("""
//            SELECT Author.name, AVG(Book.Price) as price FROM Book
//            JOIN Author ON Book.AuthorId = Author.Id
//            GROUP BY Author.name
//            HAVING price > 500 AND Author.name <> 'Тестовый'
//        """.trimIndent()
//            ).executeQuery()

        val resultMap = mutableMapOf<String, Any>()
        while (result.next()) {
            repeat(result.metaData.columnCount) {
                resultMap[result.metaData.getColumnLabel(it + 1)] = result.getObject(it + 1).toString()
            }
        }
        logger.info(resultMap.toString())

    }
}
