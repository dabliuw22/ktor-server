package com.leysoft.adapter.auth

import io.ktor.auth.Principal

interface UserPrincipal : Principal

data class BasicUserPrincipal(
    val name: String,
    val password: String = "test.password"
) : UserPrincipal

data class JwtUserPrincipal(
    val id: String,
    val username: String
) : UserPrincipal