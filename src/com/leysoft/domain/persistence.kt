package com.leysoft.domain

interface EmojiRepository {

    suspend fun save(data: Emoji): Emoji

    suspend fun findAll(): List<Emoji>

    suspend fun findById(id: EmojiId): Emoji

    suspend fun delete(id: EmojiId): Boolean
}

interface UserRepository {

    suspend fun save(data: User): User

    suspend fun findById(id: UserId): User

    suspend fun findByUsernameAndPassword(userName: UserName, password: UserPassword): User
}