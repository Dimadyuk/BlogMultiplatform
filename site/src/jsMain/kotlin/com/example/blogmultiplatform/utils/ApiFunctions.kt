package com.example.blogmultiplatform.utils

import com.example.blogmultiplatform.Constants.HUMOR_API_URL
import com.example.blogmultiplatform.models.ApiListResponse
import com.example.blogmultiplatform.models.Post
import com.example.blogmultiplatform.models.RandomJoke
import com.example.blogmultiplatform.models.User
import com.example.blogmultiplatform.models.UserWithoutPassword
import com.varabyte.kobweb.browser.api
import com.varabyte.kobweb.browser.http.http
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.js.Date


suspend fun checkUserExistence(user: User): UserWithoutPassword? {
    return try {
        val result = window.api.tryPost(
            apiPath = "usercheck",
            body = Json.encodeToString(user).encodeToByteArray()
        )
        result?.decodeToString()?.let { Json.decodeFromString(it) }
    } catch (e: Exception) {
        println(e.message)
        null
    }
}

suspend fun checkUserId(id: String): Boolean {
    return try {
        val result = window.api.tryPost(
            apiPath = "checkuserid",
            body = Json.encodeToString(id).encodeToByteArray()
        )
        result?.decodeToString()?.let {
            Json.decodeFromString<Boolean>(it)
        } == true
    } catch (e: Exception) {
        println(e.message)
        false
    }
}

suspend fun fetchRandomJoke(
    onComplete: (RandomJoke) -> Unit
) {
    val date = localStorage["date"]
    if (date != null) {
        val difference = (Date.now() - date.toDouble())
        val dayHasPassed = difference >= 86400000
        if (dayHasPassed) {
            try {
                val result = window.http.get(HUMOR_API_URL).decodeToString()
                onComplete(Json.decodeFromString<RandomJoke>(result))
                localStorage["date"] = Date.now().toString()
                localStorage["joke"] = result
            } catch (e: Exception) {
                onComplete(
                    RandomJoke(
                        id = -1,
                        joke = e.message.toString()
                    )
                )
                println("Error: $e")
            }
        } else {
            try {
                val joke = localStorage["joke"]
                if (joke != null) {
                    onComplete(Json.decodeFromString<RandomJoke>(joke))
                }
            } catch (e: Exception) {
                onComplete(
                    RandomJoke(
                        id = -1,
                        joke = e.message.toString()
                    )
                )
            }
        }
    } else {
        try {
            val result = window.http.get(HUMOR_API_URL).decodeToString()
            onComplete(Json.decodeFromString<RandomJoke>(result))
            localStorage["date"] = Date.now().toString()
            localStorage["joke"] = result
        } catch (e: Exception) {
            onComplete(
                RandomJoke(
                    id = -1,
                    joke = e.message.toString()
                )
            )
            println("Error: $e")
        }
        localStorage["date"] = Date.now().toString()
    }
}

suspend fun addPost(post: Post): Boolean {
    return try {
        window.api.tryPost(
            apiPath = "addpost",
            body = Json.encodeToString(post).encodeToByteArray()
        )?.decodeToString()?.let {
            Json.decodeFromString<Boolean>(it)
        } ?: false
    } catch (e: Exception) {
        println(e.message)
        false
    }
}

suspend fun fetchMyPosts(
    skip: Int,
    onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {
    try {
        val result = window.api.tryGet(
            apiPath = "readmyposts?skip=$skip&author=${localStorage["username"]}",
        )?.decodeToString()
        onSuccess(
            Json.decodeFromString(result.toString())
        )
    } catch (e: Exception) {
        onError(e)
    }
}
