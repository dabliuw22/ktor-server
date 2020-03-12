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