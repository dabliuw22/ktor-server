package com.leysoft.adapter.auth

import io.ktor.auth.Principal

data class User(val name: String) : Principal