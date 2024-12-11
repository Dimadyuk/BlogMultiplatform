package com.example.blogmultiplatform.data

import com.example.blogmultiplatform.models.User
import com.example.blogmultiplatform.utils.Constants.DATABASE_NAME
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
    private val database = client.getDatabase(DATABASE_NAME)
    private val userCollection = database.getCollection<User>("user")

    override suspend fun checkUserExistence(user: User): User? {
        println("$client")
        println("$database")
        println("$userCollection")
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
}
