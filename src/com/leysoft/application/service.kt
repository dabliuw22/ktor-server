package com.leysoft.application

import com.leysoft.domain.Emoji
import com.leysoft.domain.EmojiId
import com.leysoft.domain.EmojiRepository

interface EmojiService {

    suspend fun create(emoji: Emoji): Emoji

    suspend fun getBy(id: EmojiId): Emoji

    suspend fun getAll(): List<Emoji>

    suspend fun delete(id: EmojiId)
}

class DefaultEmojiService private constructor(private val repository: EmojiRepository) : EmojiService {

    override suspend fun create(emoji: Emoji) : Emoji  = repository.save(emoji)

    override suspend fun getBy(id: EmojiId): Emoji = repository.findById(id)

    override suspend fun getAll(): List<Emoji> = repository.findAll()

    override suspend fun delete(id: EmojiId) {
        repository.delete(id)
    }

    companion object {

        fun make(repository: EmojiRepository): EmojiService = DefaultEmojiService(repository)
    }
}

