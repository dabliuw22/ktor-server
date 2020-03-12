package com.leysoft.adapter.persistence

import com.leysoft.domain.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.transaction

class H2EmojiRepository private constructor() : EmojiRepository {

    override suspend fun save(data: Emoji): Emoji {
       return transaction {
           EmojiTable.insert {
               it[id] = data.id.value
               it[name] = data.name.value
               it[phrase] = data.phrase.value
           }.let { data }
       }
    }

    override suspend fun findAll(): List<Emoji> = DatabaseUtil.all(EmojiTable, f)

    override suspend fun findById(id: EmojiId): Emoji = DatabaseUtil
        .query(EmojiTable, f, EmojiTable.id eq id.value)

    override suspend fun delete(id: EmojiId): Boolean = DatabaseUtil
        .delete(EmojiTable, EmojiTable.id eq id.value)

    companion object {

        fun make(): EmojiRepository = H2EmojiRepository()

        private val f : (ResultRow) -> Emoji = {row ->
            Emoji(
                id = EmojiId(row[EmojiTable.id]),
                name = EmojiName(row[EmojiTable.name]),
                phrase = EmojiPhrase(row[EmojiTable.phrase])
            )
        }
    }
}