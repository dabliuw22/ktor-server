package com.leysoft.adapter.persistence

import com.leysoft.domain.Emoji
import com.leysoft.domain.EmojiId
import com.leysoft.domain.EmojiRepository

class InMemoryEmojiRepository private constructor() : EmojiRepository {

    private val store = mutableMapOf<String, Emoji>()

    override suspend fun save(data: Emoji): Emoji {
        store[data.id.value] = data
        return data
    }

    override suspend fun findAll(): List<Emoji> = store.values.toList()

    override suspend fun findById(id: EmojiId): Emoji = store[id.value] ?:
        throw RuntimeException("Not found Emoji: ${id.value}")


    override suspend fun delete(id: EmojiId): Boolean = store.remove(id.value)
        .let { true } ?: false

    companion object {

        fun make(): EmojiRepository = InMemoryEmojiRepository()
    }
}