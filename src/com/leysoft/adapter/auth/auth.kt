package com.leysoft.adapter.auth

import com.leysoft.application.UserService
import com.leysoft.domain.*
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.Location
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route

data class SignUpRequest(
    val username: String,
    val password: String
){
    fun to(): User = User(
        username = UserName(username),
        password = UserPassword(password)
    )
}

data class LoginRequest(
    val username: String,
    val password: String
){
    fun to(): User = User(
        username = UserName(username),
        password = UserPassword(password)
    )
}

data class LoginResponse(
    val token: String
)

const val SIGNUP = "/signup"
@Location(path = SIGNUP)
class SignUp

const val LOGIN = "/login"
@Location(path = LOGIN)
class Login

fun Route.auth(service: UserService, hash: (String) -> String) {
    post<SignUp> {
        call.receive<SignUpRequest>()
            .let { it.copy(password = hash(it.password)) }.to()
            .let { service.create(it) }
            .let {
                call.respond(
                    status = HttpStatusCode.Created,
                    message = Unit
                )
            }
    }
    post<Login> {
        call.receive<LoginRequest>()
            .let { it.copy(password = hash(it.password)) }.to()
            .let { service.getBy(it.username, it.password) }
            .let {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = LoginResponse(
                        Jwt.gen(
                            it
                        )
                    )
                )
            }
    }
}