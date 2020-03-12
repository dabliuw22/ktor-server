package com.leysoft

import com.leysoft.adapter.auth.User
import com.leysoft.adapter.http.*
import com.leysoft.adapter.persistence.DatabaseUtil
import com.leysoft.adapter.persistence.H2EmojiRepository
import com.leysoft.adapter.persistence.InMemoryEmojiRepository
import com.leysoft.application.DefaultEmojiService
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.basic
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
                if (credentials.password == "test.password") User(credentials.name) else null
            }
        }
    }

    install(Locations) // Enable Locations

    DatabaseUtil.init()

    val repository = H2EmojiRepository.make()
    val service = DefaultEmojiService.make(repository)

    routing {
        home()
        about()
        emoji(service)
    }
}

