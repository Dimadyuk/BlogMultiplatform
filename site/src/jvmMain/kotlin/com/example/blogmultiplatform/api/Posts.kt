package com.example.blogmultiplatform.api

import com.example.blogmultiplatform.Constants.AUTHOR_PARAM
import com.example.blogmultiplatform.Constants.CATEGORY_PARAM
import com.example.blogmultiplatform.Constants.POST_ID_PARAM
import com.example.blogmultiplatform.Constants.QUERY_PARAM
import com.example.blogmultiplatform.Constants.SKIP_PARAM
import com.example.blogmultiplatform.data.MongoDB
import com.example.blogmultiplatform.models.ApiListResponse
import com.example.blogmultiplatform.models.ApiResponse
import com.example.blogmultiplatform.models.Category
import com.example.blogmultiplatform.models.Post
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.Request
import com.varabyte.kobweb.api.http.Response
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bson.codecs.ObjectIdGenerator

val json = Json {
    ignoreUnknownKeys = true
}

@Api(routeOverride = "addpost")
suspend fun addPost(context: ApiContext) {

    try {
        val post = context.req.getBody<Post>()
        val newPost = post?.copy(id = ObjectIdGenerator().generate().toString())
        context.res.setBody(
            newPost?.let {
                context.data.getValue<MongoDB>().addPost(it).toString()
            } ?: false.toString()
        )
    } catch (e: Exception) {
        context.logger.error(e.message.toString())
        context.res.setBody(
            e.message
        )
    }
}

@Api(routeOverride = "updatepost")
suspend fun updatePost(context: ApiContext) {
    try {
        val updatedPost = context.req.getBody<Post>()
        context.res.setBody(
            updatedPost?.let {
                context.data.getValue<MongoDB>().updatePost(it)
            }
        )
    } catch (e: Exception) {
        context.logger.error(e.message.toString())
        context.res.setBody(
            e.message
        )
    }
}

@Api(routeOverride = "readmyposts")
suspend fun readMyPosts(context: ApiContext) {

    try {
        val skip = context.req.params[SKIP_PARAM]?.toInt() ?: 0
        val author = context.req.params[AUTHOR_PARAM] ?: ""
        val result = context.data.getValue<MongoDB>().readMyPosts(skip, author)

        context.res.setBody(
            ApiListResponse.Success(result)
        )
    } catch (e: Exception) {
        context.logger.error(e.message.toString())
        context.res.setBody(
            ApiListResponse.Error(e.message.toString())
        )
    }
}

@Api(routeOverride = "readmainposts")
suspend fun readMainPosts(context: ApiContext) {
    try {
        val result = context.data.getValue<MongoDB>().readMainPosts()

        context.res.setBody(
            ApiListResponse.Success(result)
        )
    } catch (e: Exception) {
        context.logger.error(e.message.toString())
        context.res.setBody(
            ApiListResponse.Error(e.message.toString())
        )
    }
}

@Api(routeOverride = "readlatestposts")
suspend fun readLatestPosts(context: ApiContext) {
    try {
        val skip = context.req.params[SKIP_PARAM]?.toInt() ?: 0
        val result = context.data.getValue<MongoDB>().readLatestPosts(skip)

        context.res.setBody(
            ApiListResponse.Success(result)
        )
    } catch (e: Exception) {
        context.logger.error(e.message.toString())
        context.res.setBody(
            ApiListResponse.Error(e.message.toString())
        )
    }
}

@Api(routeOverride = "readpopularposts")
suspend fun readPopularPosts(context: ApiContext) {
    try {
        val skip = context.req.params[SKIP_PARAM]?.toInt() ?: 0
        val result = context.data.getValue<MongoDB>().readPopularPosts(skip)

        context.res.setBody(
            ApiListResponse.Success(result)
        )
    } catch (e: Exception) {
        context.logger.error(e.message.toString())
        context.res.setBody(
            ApiListResponse.Error(e.message.toString())
        )
    }
}

@Api(routeOverride = "readsponsoredtposts")
suspend fun readSponsoredPosts(context: ApiContext) {
    try {
        val result = context.data.getValue<MongoDB>().readSponsoredPosts()

        context.res.setBody(
            ApiListResponse.Success(result)
        )
    } catch (e: Exception) {
        context.logger.error(e.message.toString())
        context.res.setBody(
            ApiListResponse.Error(e.message.toString())
        )
    }
}

@Api(routeOverride = "deleteselectedposts")
suspend fun deleteSelectedPosts(context: ApiContext) {

    try {
        val request = context.req.getBody<List<String>>()
        val result = context.data.getValue<MongoDB>().deleteSelectedPosts(request ?: emptyList())
        context.res.setBody(
            result
        )
    } catch (e: Exception) {
        context.logger.error(e.message.toString())
        context.res.setBody(
            e.message
        )
    }
}

@Api(routeOverride = "searchposts")
suspend fun searchPostsByTittle(context: ApiContext) {
    try {
        val query = context.req.params[QUERY_PARAM] ?: ""
        val skip = context.req.params[SKIP_PARAM]?.toInt() ?: 0
        val request = context.data.getValue<MongoDB>().searchPostsByTittle(query, skip)

        context.res.setBody(
            ApiListResponse.Success(request)
        )
    } catch (e: Exception) {
        context.logger.error(e.message.toString())
        context.res.setBody(
            ApiListResponse.Error(e.message.toString())
        )
    }
}

@Api(routeOverride = "searchpostsbycategory")
suspend fun searchPostsByCategory(context: ApiContext) {
    try {
        val category = Category.valueOf(
            context.req.params[CATEGORY_PARAM] ?: Category.Programming.name
        )
        val skip = context.req.params[SKIP_PARAM]?.toInt() ?: 0
        val request = context.data.getValue<MongoDB>().searchPostsByCategory(category, skip)

        context.res.setBody(
            ApiListResponse.Success(request)
        )
    } catch (e: Exception) {
        context.logger.error(e.message.toString())
        context.res.setBody(
            ApiListResponse.Error(e.message.toString())
        )
    }
}

@Api(routeOverride = "readselectedpost")
suspend fun readSelectedPost(context: ApiContext) {
    val postId = context.req.params[POST_ID_PARAM]

    if (!postId.isNullOrEmpty()) {
        try {
            val request = context.data.getValue<MongoDB>().readSelectedPost(postId)

            context.res.setBody(
                    ApiResponse.Success(request)
            )
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            context.res.setBody(
                ApiResponse.Error(e.message.toString())
            )
        }
    } else {
        context.res.setBody(
            ApiResponse.Error("Selected Post doesn't exist.")
        )
    }
}

inline fun <reified T> Response.setBody(objectBody: T) {
    setBodyText(
        json.encodeToString<T>(objectBody)
    )
}

inline fun <reified T> Request.getBody(): T? {
    return body?.decodeToString()?.let {
        json.decodeFromString<T>(it)
    }
}
