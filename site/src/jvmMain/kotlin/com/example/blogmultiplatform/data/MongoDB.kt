package com.example.blogmultiplatform.data

import com.example.blogmultiplatform.Constants
import com.example.blogmultiplatform.models.Post
import com.example.blogmultiplatform.models.User
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.and
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.varabyte.kobweb.api.data.add
import com.varabyte.kobweb.api.init.InitApi
import com.varabyte.kobweb.api.init.InitApiContext
import kotlinx.coroutines.flow.firstOrNull

@InitApi
fun initMongoDB(context: InitApiContext) {
    context.data.add(MongoDB(context))
}

class MongoDB(val context: InitApiContext) : MongoRepository {
    private val uri = "mongodb://localhost:27017/"
    private val client = MongoClient.create(uri)
    private val database = client.getDatabase(Constants.DATABASE_NAME)
    private val userCollection = database.getCollection<User>("user")
    private val postCollection = database.getCollection<Post>("post")

    override suspend fun addPost(post: Post): Boolean {
        return postCollection.insertOne(post).wasAcknowledged()
    }

    override suspend fun checkUserExistence(user: User): User? {
        return try {
            userCollection
                .find(
                    and(
                        Filters.eq(User::username.name, user.username),
                        Filters.eq(User::password.name, user.password)
                    )
                ).firstOrNull()
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            null
        }
    }

    override suspend fun checkUserId(id: String): Boolean {
        return try {
            val documentCount = userCollection.countDocuments(
                Filters.eq("_id", id)
            )
            documentCount > 0
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            false
        }
    }
}
