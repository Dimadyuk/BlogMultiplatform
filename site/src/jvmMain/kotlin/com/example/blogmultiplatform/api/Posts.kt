package com.example.blogmultiplatform.api

import com.example.blogmultiplatform.data.MongoDB
import com.example.blogmultiplatform.models.Post
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bson.codecs.ObjectIdGenerator


@Api(routeOverride = "addpost")
suspend fun addPost(context: ApiContext) {
    val json = Json {
        ignoreUnknownKeys = true
    }

    try {
        val post = context.req.body?.decodeToString()?.let {
            json.decodeFromString<Post>(it)
        }
        val newPost = post?.copy(id = ObjectIdGenerator().generate().toString())
        context.res.setBodyText(
            newPost?.let {
                context.data.getValue<MongoDB>().addPost(it).toString()
            } ?: false.toString()
        )
    } catch (e: Exception) {
        context.logger.error(e.message.toString())
        context.res.setBodyText(
            json.encodeToString(e.message)
        )
    }
}
