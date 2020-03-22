package com.leysoft.adapter.persistence

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.dao.*


open class StringIdTable(name: String = "", columnName: String = "id") : IdTable<String>(name) {

    override val id: Column<EntityID<String>>  = varchar(columnName, 255).primaryKey().entityId()
}

object EmojiTable : Table("emojis") {
    val id: Column<String> = varchar(name = "id", length = 255).primaryKey()
    val name : Column<String> = varchar(name = "name", length = 255)
    val phrase : Column<String> = varchar(name = "phrase", length = 255)
}

object UserTable : Table("users") {
    val id: Column<String> = varchar(name = "id", length = 255).primaryKey()
    val username : Column<String> = varchar(name = "username", length = 255)
    val password : Column<String> = varchar(name = "password", length = 255)
}