package com.tinkoff.backend.database.dto

import com.tinkoff.backend.database.generator.UserGenerator
import com.tinkoff.backend.database.repositories.UserEntityRepository
import java.io.File
import java.time.LocalDateTime
import kotlin.random.Random
import kotlin.system.measureNanoTime
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

private val logger = KotlinLogging.logger { }

val folder: File = File(System.getenv("PATH_TO_FOLDER")).apply {
    deleteRecursively()
    mkdirs()
}

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    properties = [
//        "spring.datasource.url=jdbc:h2:file:./data/demo"
        "spring.datasource.url=jdbc:tc:postgresql:11:///test"
    ]
)
class UserDtoTest {

    @Autowired
    lateinit var userEntityRepository: UserEntityRepository

    val generator = UserGenerator()
    @Test
    fun perf() {
        val data = List(1_000) {
            generator.generateUserDto(it)
        }
        val newList = mutableListOf<UserDto>()
        logMeasureTime("Saved to memory") {
            data.forEach { user ->
                newList.add(user.copy())
            }
        }
        logMeasureTime("Search in memory") {
            data.forEach { user ->
                data.first { it.id == user.id }
            }
        }

        logMeasureTime("Saved to file") {
            data.forEach { user ->
                folder.resolve("${user.id}.txt").also {
                    it.createNewFile()
                    it.writeText(user.toString())
                }
            }

        }
        logMeasureTime("Search in file") {
            data.forEach { user ->
                folder.listFiles().any {
                    (it.readText().indexOf("id=${user.id}") != 0)
                }
            }

        }

        repeat(10_000) {
            val user = data.random()
            val userEntity = userEntityRepository.save(user.toEntity())
            userEntityRepository.findById(userEntity.id)
        }
        userEntityRepository.deleteAll()

        val dataEntity = logMeasureTime("Saved to dataBase") {
            data.map { user ->
                userEntityRepository.save(user.toEntity())
            }

        }

        logMeasureTime("Search in dataBase") {
            dataEntity.forEach { user ->
                userEntityRepository.findById(user.id)
            }

        }

        logger.info("Finish")
    }
}



fun <T> logMeasureTime(name: String, block: () -> T): T {
    var result: T
    val duration = measureNanoTime {
        result = block()
    }
    logger.info("$name. Duration ${duration / 1000_000} ms")
    return result
}
