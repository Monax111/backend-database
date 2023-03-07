package com.tinkoff.backend.database.service

import com.tinkoff.backend.database.dto.toEntity
import com.tinkoff.backend.database.generator.UserGenerator
import com.tinkoff.backend.database.repositories.UserEntityRepository
import kotlin.system.measureTimeMillis
import org.springframework.stereotype.Service

@Service
class UserService(
    val userEntityRepository: UserEntityRepository,
    val userGenerator: UserGenerator
) {

    fun generateUser(count : Int): Int{
        val time = measureTimeMillis {
            repeat(count) {
                userEntityRepository.save(userGenerator.generateUserDto(it).toEntity())
            }
        }
        return time.toInt()
    }
}