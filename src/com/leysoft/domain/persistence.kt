package com.leysoft.domain

interface EmojiRepository {

    suspend fun save(data: Emoji): Emoji

    suspend fun findAll(): List<Emoji>

    suspend fun findById(id: EmojiId): Emoji

    suspend fun delete(id: EmojiId): Boolean
}