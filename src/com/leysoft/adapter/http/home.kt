package com.leysoft.adapter.http

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.response.*
import io.ktor.routing.Route
import io.ktor.routing.get

const val HOME = "/home"

fun Route.home() {
    authenticate("basic-auth") {
        get(HOME) {
            call.respondText("Hello, World!")
        }
        get("$HOME/hello") {
            call.respondText { "Hello, Ktor!" }
        }
    }
}