package com.leysoft.domain

import java.util.UUID

data class EmojiId(val value: String)

data class EmojiName(val value: String)

data class EmojiPhrase(val value: String)

data class Emoji(
    val id: EmojiId = EmojiId(UUID.randomUUID().toString()),
    val name: EmojiName,
    val phrase: EmojiPhrase
)

data class UserId(val value: String = UUID.randomUUID().toString())

data class UserName(val value: String)

data class UserPassword(val value: String)

data class User(
    val id: UserId = UserId(),
    val username: UserName,
    val password: UserPassword
)