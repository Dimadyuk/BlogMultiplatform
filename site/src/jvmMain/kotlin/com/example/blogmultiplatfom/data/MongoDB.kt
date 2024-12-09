package com.example.blogmultiplatfom.data

import com.example.blogmultiplatfom.models.User
import com.example.blogmultiplatfom.utils.Constants.DATABASE_NAME
import com.varabyte.kobweb.api.data.add
import com.varabyte.kobweb.api.init.InitApi
import com.varabyte.kobweb.api.init.InitApiContext
import org.litote.kmongo.KMongo
import org.litote.kmongo.and
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection

@InitApi
fun initMongoDB(context: InitApiContext) {
    System.setProperty(
        "org.litote.kmongo.test.mapping.service",
        "org.litote.kmongo.serialization.SerializationClassMappingTypeService"
    )
    context.data.add(MongoDB(context))
}

class MongoDB(val context: InitApiContext) : MongoRepository {
    private val client = KMongo.createClient()
    private val database = client.getDatabase(DATABASE_NAME)
    private val userCollection = database.getCollection<User>()

    override suspend fun checkUserExistence(user: User): User? {
        return try {
            userCollection
                .findOne {
                    and(
                        User::username eq user.username,
                        User::password eq user.password
                    )
                }
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            null
        }
    }
}
