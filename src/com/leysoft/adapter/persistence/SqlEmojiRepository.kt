package com.leysoft.adapter.persistence

import com.leysoft.domain.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.transaction

class SqlEmojiRepository private constructor() : EmojiRepository {

    override suspend fun save(data: Emoji): Emoji =
        transaction {
            EmojiTable.insert {
                it[id] = data.id.value
                it[name] = data.name.value
                it[phrase] = data.phrase.value
            }.let { data }
        }

    override suspend fun findAll(): List<Emoji> = DatabaseUtil.all(EmojiTable, f)

    override suspend fun findById(id: EmojiId): Emoji = DatabaseUtil
        .query(EmojiTable, f, EmojiTable.id eq id.value)

    override suspend fun delete(id: EmojiId): Boolean = DatabaseUtil
        .delete(EmojiTable, EmojiTable.id eq id.value)

    companion object {

        fun make(): EmojiRepository = SqlEmojiRepository()

        private val f : (ResultRow) -> Emoji = { row ->
            Emoji(
                id = EmojiId(row[EmojiTable.id]),
                name = EmojiName(row[EmojiTable.name]),
                phrase = EmojiPhrase(row[EmojiTable.phrase])
            )
        }
    }
}