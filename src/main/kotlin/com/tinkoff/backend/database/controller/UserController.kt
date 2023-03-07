package com.tinkoff.backend.database.controller

import com.tinkoff.backend.database.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("users")
class UserController(
    val userService: UserService
) {

    @PostMapping("/add/random/{count}")
    fun addRandomUser(@PathVariable count: Int) = userService.generateUser(count = count)

}