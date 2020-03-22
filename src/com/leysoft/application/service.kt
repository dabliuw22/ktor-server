package com.leysoft.application

import com.leysoft.domain.*

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

interface UserService {

    suspend fun create(user: User): User

    suspend fun getBy(id: UserId): User

    suspend fun getBy(username: UserName, password: UserPassword): User
}

class DefaultUserService private constructor(private val repository: UserRepository) : UserService {

    override suspend fun create(user: User): User  = repository.save(user)

    override suspend fun getBy(id: UserId): User = repository.findById(id)

    override suspend fun getBy(username: UserName, password: UserPassword): User =
        repository.findByUsernameAndPassword(username, password)

    companion object {

        fun make(repository: UserRepository): UserService = DefaultUserService(repository)
    }
}
