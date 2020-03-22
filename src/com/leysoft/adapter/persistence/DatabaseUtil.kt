package com.leysoft.adapter.persistence

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseUtil {

    fun init() {
        Database.connect(postgresDataSource())
        transaction {
            SchemaUtils.create(EmojiTable)
            SchemaUtils.create(UserTable)
        }
    }

    private fun postgresDataSource(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.postgresql.Driver"
        config.jdbcUrl = "jdbc:postgresql://localhost:5432/ktor_db"
        config.maximumPoolSize = 10
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.username = "ktor"
        config.password = "ktor"
        config.validate()
        return HikariDataSource(config)
    }

    private fun h2DataSource(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.h2.Driver"
        config.jdbcUrl = "jdbc:h2:ktor_db"
        config.maximumPoolSize = 10
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }

    suspend fun <T, F : Table> all(table: F, fMap: (ResultRow) -> T) : List<T> =
        withContext(Dispatchers.IO) {
            transaction { table.selectAll().distinct().map { fMap(it) } }
        }

    suspend fun <T, F : Table> queryList(table: F, fMap: (ResultRow) -> T, where: Op<Boolean>): List<T> =
        withContext(Dispatchers.IO) {
            transaction { table.select(where).distinct().map { fMap(it) } }
        }

    suspend fun <T, F : Table> query(table: F, fMap: (ResultRow) -> T, where: Op<Boolean>): T =
        withContext(Dispatchers.IO) {
            transaction {
                table.select(where).distinct().map { fMap(it) }
                    .singleOrNull() ?: throw RuntimeException()
            }
        }

    suspend fun <F : Table> delete(table: F, where: Op<Boolean>): Boolean =
        withContext(Dispatchers.IO) {
            transaction { table.deleteWhere { where } > 0 }
        }
}