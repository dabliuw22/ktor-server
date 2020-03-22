package com.leysoft.adapter.auth

import io.ktor.util.hex
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object Hash {

    private val hashKey = hex("46e08c9ba85840caa48b0e8c1f72385d")

    private val algorithm = "HmacSHA1"

    private val hmacKey = SecretKeySpec(hashKey, algorithm)

    val hash: (String) -> String = { data ->
        Mac.getInstance(algorithm).let {
            it.init(hmacKey)
            hex(it.doFinal(data.toByteArray(Charsets.UTF_8)))
        }
    }
}