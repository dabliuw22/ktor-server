package com.leysoft.adapter.http

import com.leysoft.adapter.auth.User
import com.leysoft.application.EmojiService
import com.leysoft.domain.Emoji
import com.leysoft.domain.EmojiId
import com.leysoft.domain.EmojiName
import com.leysoft.domain.EmojiPhrase
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post as rpost
import io.ktor.routing.get as rget

data class EmojiRequest(
    val emoji: String,
    val phrase: String
){
    fun to(): Emoji = Emoji(
        name = EmojiName(emoji),
        phrase = EmojiPhrase(phrase)
    )
}

data class EmojiResponse(
    val id: String,
    val emoji: String,
    val phrase: String
)

fun Emoji.toResponse() = EmojiResponse(
    id = this.id.value,
    emoji = this.name.value,
    phrase = this.phrase.value
)

fun String.to() = EmojiId(this)

const val EMOJI = "/emojis"

@Location(path = EMOJI)
class EmojiPath

const val ID = "id"

const val GET_EMOJI = "$EMOJI/{$ID}"

@Location(path = GET_EMOJI)
data class GetEmojiPath(val id: String) {

    fun to() : EmojiId = EmojiId(id)
}

fun Route.emoji(service: EmojiService) {
    authenticate("basic-auth") {
        get<EmojiPath> {
            println("User: ${(call.authentication.principal as User)}")
            service.getAll()
                .map { it.toResponse() }
                .let { call.respond(it) }
        }
        get<GetEmojiPath> { getEmoji ->
            service.getBy(getEmoji.to()).toResponse()
                .let { call.respond(it) }
        }
        /*rget(GET_EMOJI) {
            val id = call.parameters[ID] ?: throw RuntimeException()
            id.let { service.getBy(it.to()).toResponse() }
                .let { call.respond(it) }
        }*/
        post<EmojiPath> {
            call.receive<EmojiRequest>().to()
                .let { service.create(it).toResponse() }
                .let { call.respond(it) }
        }
    }
}