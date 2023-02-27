package com.tinkoff.backend.database.entities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "userentity")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = 0,
    var email: String? = null,
    val name: String? = null,
    val age: Int? = null,
    val address: String? = null
)