package com.leysoft.adapter.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.leysoft.domain.User
import java.util.Date

object Jwt {

    private val issuer = "jwt.issuer"

    private val secret = "jwt.secret"

    private val algorithm = Algorithm.HMAC512(secret)

    val verifier: () -> JWTVerifier = {
        JWT.require(algorithm).withIssuer(issuer).build()
    }

    private fun expiresAt() = Date(System.currentTimeMillis() + 3600000 * 24)

    fun gen(user: User): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("id", user.id.value)
        .withExpiresAt(expiresAt())
        .sign(algorithm)
}