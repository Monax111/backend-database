package com.tinkoff.backend.database.dto

import com.tinkoff.backend.database.entities.UserEntity

data class UserDto(
    val id: Int,
    val name: String,
    val age: Int,
    val email: String,
    val address: String
)


fun UserDto.toEntity() = UserEntity(
    id = null,
    email = email,
    name = name,
    age = age,
    address = address,
)