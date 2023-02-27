package com.tinkoff.backend.database.repositories

import com.tinkoff.backend.database.entities.UserEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserEntityRepository : CrudRepository<UserEntity, Int>