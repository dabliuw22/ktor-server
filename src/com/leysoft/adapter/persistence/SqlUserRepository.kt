package com.leysoft.adapter.persistence

import com.leysoft.domain.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class SqlUserRepository private constructor() : UserRepository {

    override suspend fun save(data: User): User =
        transaction {
            UserTable.insert {
                it[id] = data.id.value
                it[username] = data.username.value
                it[password] = data.password.value
            }.let { data }
        }

    override suspend fun findById(id: UserId): User =
        (UserTable.id eq id.value).let {
            DatabaseUtil.query(UserTable, f, it)
        }

    override suspend fun findByUsernameAndPassword(userName: UserName, password: UserPassword): User =
        ((UserTable.username eq userName.value) and (UserTable.password eq password.value))
            .let { where -> DatabaseUtil.query(UserTable, f, where) }

    companion object {

        fun make(): UserRepository = SqlUserRepository()

        private val f : (ResultRow) -> User = { row ->
            User(
                id = UserId(row[UserTable.id]),
                username = UserName(row[UserTable.username]),
                password = UserPassword(row[UserTable.password])
            )
        }
    }
}