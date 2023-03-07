package com.tinkoff.backend.database.dto

import com.tinkoff.backend.database.entities.UserEntity

data class UserDto(
    val id: Int,
    val name: String,
    val sex: Sex,
    val age: Int,
    val email: String,
    val address: String
)


fun UserDto.toEntity() = UserEntity(
    id = null,
    email = email,
    name = name,
    sex = sex.toString(),
    age = age,
    address = address,
)

enum class Sex {
    Men,
    Woman
}
