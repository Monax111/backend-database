//package com.tinkoff.backend.database.controller
//
//import com.tinkoff.sirius.financial_tracker.entities.User
//import io.swagger.v3.oas.annotations.Operation
//import io.swagger.v3.oas.annotations.tags.Tag
//import com.tinkoff.sirius.financial_tracker.exceptions.ResourceNotFoundException
//import com.tinkoff.sirius.financial_tracker.services.UserService
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.PathVariable
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RestController
//
//@RestController
//@RequestMapping("users")
//@Tag(name = "Пользователи")
//class UserController(private val userService: UserService) {
//
//    @GetMapping("/{id}")
//    @Operation(summary = "Получить пользователя по его ID")
//    fun getUser(@PathVariable id: Int): User {
//        return userService.findUserById(id) ?:
//            throw ResourceNotFoundException("User with such id doesn't exist")
//    }
//
//}