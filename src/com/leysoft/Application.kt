package com.leysoft

import com.leysoft.adapter.auth.*
import com.leysoft.adapter.http.*
import com.leysoft.adapter.persistence.DatabaseUtil
import com.leysoft.adapter.persistence.SqlEmojiRepository
import com.leysoft.adapter.persistence.SqlUserRepository
import com.leysoft.application.DefaultEmojiService
import com.leysoft.application.DefaultUserService
import com.leysoft.domain.UserId
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.basic
import io.ktor.auth.jwt.jwt
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.locations.Locations
import io.ktor.response.respondText
import io.ktor.routing.routing

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    DatabaseUtil.init()

    val emojiService = SqlEmojiRepository.make().let { DefaultEmojiService.make(it) }

    val userService = SqlUserRepository.make().let { DefaultUserService.make(it) }

    install(DefaultHeaders)

    install(CallLogging)

    install(StatusPages) { // Enable Error Handler
        exception<Throwable> { e ->
            println(e)
            call.respondText(
                text = e.localizedMessage,
                contentType = ContentType.Text.Plain,
                status = HttpStatusCode.InternalServerError
            )
        }
    }

    install(ContentNegotiation) { // Enable Json
        jackson()
    }

    install(Authentication) { // Enable Authentication
        basic("basic-auth") {
            realm = "ktor-server"
            validate { credentials ->
                if (credentials.password == "test.password") BasicUserPrincipal(credentials.name) else null
            }
        }
        jwt("jwt") {
            realm = "ktor-server"
            verifier(Jwt.verifier())
            validate { credentials ->
                credentials.payload.getClaim("id").asString()
                    .let { userService.getBy(UserId(it)) }
                    .let { JwtUserPrincipal(it.id.value, it.username.value) }
            }
        }
    }

    install(Locations) // Enable Locations

    routing {
        home()
        about()
        emoji(emojiService)
        auth(userService, Hash.hash)
    }
}

