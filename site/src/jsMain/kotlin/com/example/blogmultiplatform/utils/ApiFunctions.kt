package com.example.blogmultiplatform.utils

import com.example.blogmultiplatform.Constants.AUTHOR_PARAM
import com.example.blogmultiplatform.Constants.CATEGORY_PARAM
import com.example.blogmultiplatform.Constants.HUMOR_API_URL
import com.example.blogmultiplatform.Constants.POST_ID_PARAM
import com.example.blogmultiplatform.Constants.QUERY_PARAM
import com.example.blogmultiplatform.Constants.SKIP_PARAM
import com.example.blogmultiplatform.models.ApiListResponse
import com.example.blogmultiplatform.models.ApiResponse
import com.example.blogmultiplatform.models.Category
import com.example.blogmultiplatform.models.Newsletter
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
        window.api.tryPost(
            apiPath = "usercheck",
            body = Json.encodeToString(user).encodeToByteArray()
        )?.decodeToString().parseData()
    } catch (e: Exception) {
        println(e.message)
        null
    }
}

suspend fun checkUserId(id: String): Boolean {
    return try {
        window.api.tryPost(
            apiPath = "checkuserid",
            body = Json.encodeToString(id).encodeToByteArray()
        )?.decodeToString().parseData<Boolean>()
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
                onComplete(result.parseData())
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
                val joke = localStorage["joke"]?.parseData<RandomJoke>()
                println("jokeId - ${joke?.id} joke - ${joke?.joke}")
                joke?.let { onComplete(it) }
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
            onComplete(result.parseData())
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

suspend fun updatePost(post: Post): Boolean {
    return try {
        window.api.tryPost(
            apiPath = "updatepost",
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
            apiPath = "readmyposts?$SKIP_PARAM=$skip&$AUTHOR_PARAM=${localStorage["username"]}",
        )?.decodeToString()
        onSuccess(
            result.parseData()
        )
    } catch (e: Exception) {
        onError(e)
    }
}

suspend fun fetchMainPosts(
    onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {
    try {
        val result = window.api.tryGet(
            apiPath = "readmainposts",
        )?.decodeToString()
        onSuccess(
            result.parseData()
        )
    } catch (e: Exception) {
        println(e)
        onError(e)
    }
}

suspend fun fetchLatestPosts(
    skip: Int,
    onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {
    try {
        val result = window.api.tryGet(
            apiPath = "readlatestposts?$SKIP_PARAM=$skip"
        )?.decodeToString()
        onSuccess(
            result.parseData()
        )
    } catch (e: Exception) {
        println(e)
        onError(e)
    }
}

suspend fun fetchPopularPosts(
    skip: Int,
    onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {
    try {
        val result = window.api.tryGet(
            apiPath = "readpopularposts?$SKIP_PARAM=$skip"
        )?.decodeToString()
        onSuccess(
            result.parseData()
        )
    } catch (e: Exception) {
        println(e)
        onError(e)
    }
}

suspend fun fetchSponsoredPosts(
    onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {
    try {
        val result = window.api.tryGet(
            apiPath = "readsponsoredtposts",
        )?.decodeToString()
        onSuccess(
            result.parseData()
        )
    } catch (e: Exception) {
        println(e)
        onError(e)
    }
}

suspend fun deleteSelectedPosts(ids: List<String>): Boolean {
    return try {
        val result = window.api.tryPost(
            apiPath = "deleteselectedposts",
            body = Json.encodeToString(ids).encodeToByteArray()
        )?.decodeToString()?.let {
            Json.decodeFromString<Boolean>(it)
        }
        result ?: false
    } catch (e: Exception) {
        println(e.message)
        false
    }
}

suspend fun searchPostsByTitle(
    query: String,
    skip: Int,
    onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {
    try {
        val result = window.api.tryGet(
            apiPath = "searchposts?$QUERY_PARAM=$query&$SKIP_PARAM=$skip",
        )?.decodeToString()
        onSuccess(
            result.parseData()
        )
    } catch (e: Exception) {
        onError(e)
    }
}

suspend fun searchPostsByCategory(
    category: Category,
    skip: Int,
    onSuccess: (ApiListResponse) -> Unit,
    onError: (Exception) -> Unit
) {
    try {
        val result = window.api.tryGet(
            apiPath = "searchpostsbycategory?$CATEGORY_PARAM=${category.name}&$SKIP_PARAM=$skip",
        )?.decodeToString()
        onSuccess(
            result.parseData()
        )
    } catch (e: Exception) {
        onError(e)
    }
}

suspend fun fetchSelectedPost(
    id: String,
): ApiResponse {
    return try {
        val result = window.api.tryGet(
            apiPath = "readselectedpost?$POST_ID_PARAM=$id",
        )?.decodeToString()

        result?.parseData() ?: ApiResponse.Error("Post not found")
    } catch (e: Exception) {
        ApiResponse.Error(e.message.toString())
    }
}

suspend fun subscribeNewsletter(
    newsletter: Newsletter,
): String {
    return window.api.tryPost(
        apiPath = "subscribe",
        body = Json.encodeToString(newsletter).encodeToByteArray()
    )?.decodeToString()?.let {
        Json.decodeFromString(it)
    } ?: "Something went wrong. Please try again later."
}

inline fun <reified T> String?.parseData(): T {
    return Json.decodeFromString<T>(this.toString())
}
