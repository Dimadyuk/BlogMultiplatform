package com.example.blogmultiplatform.data

import com.example.blogmultiplatform.Category
import com.example.blogmultiplatform.Constants.DATABASE_NAME_REMOTE
import com.example.blogmultiplatform.Constants.MAIN_POSTS_LIMIT
import com.example.blogmultiplatform.Constants.POSTS_PER_PAGE
import com.example.blogmultiplatform.models.Newsletter
import com.example.blogmultiplatform.models.Post
import com.example.blogmultiplatform.models.PostWithoutDetails
import com.example.blogmultiplatform.models.User
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Indexes.descending
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.varabyte.kobweb.api.data.add
import com.varabyte.kobweb.api.init.InitApi
import com.varabyte.kobweb.api.init.InitApiContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList

@InitApi
fun initMongoDB(ctx: InitApiContext) {
    System.setProperty(
        "org.litote.mongo.test.mapping.service",
        "org.litote.kmongo.serialization.SerializationClassMappingTypeService"
    )
    ctx.data.add(MongoDB(ctx))
}

class MongoDB(private val context: InitApiContext) : MongoRepository {
    private val client = MongoClient.create(System.getenv("MONGODB_URI"))
    private val database = client.getDatabase(DATABASE_NAME_REMOTE)
    private val userCollection = database.getCollection<User>("user")
    private val postCollection = database.getCollection<Post>("post")
    private val newsletterCollection = database.getCollection<Newsletter>("newsletter")

    override suspend fun addPost(post: Post): Boolean {
        return postCollection.insertOne(post).wasAcknowledged()
    }

    override suspend fun updatePost(post: Post): Boolean {
        return postCollection
            .updateOne(
                Filters.eq("_id", post.id),
                Updates.combine(
                    Updates.set(Post::title.name, post.title),
                    Updates.set(Post::subtitle.name, post.subtitle),
                    Updates.set(Post::thumbnail.name, post.thumbnail),
                    Updates.set(Post::content.name, post.content),
                    Updates.set(Post::category.name, post.category),
                    Updates.set(Post::popular.name, post.popular),
                    Updates.set(Post::main.name, post.main),
                    Updates.set(Post::sponsored.name, post.sponsored),
                )
            )
            .wasAcknowledged()
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

    override suspend fun readMyPosts(skip: Int, author: String): List<PostWithoutDetails> {
        return postCollection.withDocumentClass(PostWithoutDetails::class.java)
                .find(
                    Filters.eq(
                        PostWithoutDetails::author.name, author
                    )
                )
                .sort(descending(PostWithoutDetails::date.name))
                .skip(skip)
                .limit(POSTS_PER_PAGE)
                .toList()
    }

    override suspend fun readMainPosts(): List<PostWithoutDetails> {
        return postCollection
            .withDocumentClass(PostWithoutDetails::class.java)
            .find(
                Filters.eq(PostWithoutDetails::main.name, true)
            )
            .sort(descending(PostWithoutDetails::date.name))
            .limit(MAIN_POSTS_LIMIT)
            .toList()
    }

    override suspend fun readLatestPosts(skip: Int): List<PostWithoutDetails> {
        return postCollection
            .withDocumentClass(PostWithoutDetails::class.java)
            .find(
                and(
                    Filters.eq(PostWithoutDetails::main.name, false),
                    Filters.eq(PostWithoutDetails::popular.name, false),
                    Filters.eq(PostWithoutDetails::sponsored.name, false),
                )
            )
            .sort(descending(PostWithoutDetails::date.name))
            .skip(skip)
            .limit(POSTS_PER_PAGE)
            .toList()
    }

    override suspend fun readPopularPosts(skip: Int): List<PostWithoutDetails> {
        return postCollection
            .withDocumentClass(PostWithoutDetails::class.java)
            .find(
                Filters.eq(PostWithoutDetails::popular.name, true),
            )
            .sort(descending(PostWithoutDetails::date.name))
            .skip(skip)
            .limit(POSTS_PER_PAGE)
            .toList()
    }

    override suspend fun subscribe(newsletter: Newsletter): String {
        val result = newsletterCollection
            .find(
                Filters.eq(Newsletter::email.name, newsletter.email)
            ).toList()
        return if (result.isNotEmpty()) {
            "You are already subscribed"
        } else {
            val newEmail = newsletterCollection
                .insertOne(newsletter)
                .wasAcknowledged()

            if (newEmail) "Successfully subscribed"
            else "Something went wrong. Please try again later."
        }
    }

    override suspend fun readSponsoredPosts(): List<PostWithoutDetails> {
        return postCollection
            .withDocumentClass(PostWithoutDetails::class.java)
            .find(
                Filters.eq(PostWithoutDetails::sponsored.name, true),
            )
            .sort(descending(PostWithoutDetails::date.name))
            .limit(2)
            .toList()
    }

    override suspend fun deleteSelectedPosts(ids: List<String>): Boolean {
        return postCollection
            .deleteMany(
                Filters.`in`("_id", ids)
            )
            .wasAcknowledged()
    }

    override suspend fun searchPostsByTittle(query: String, skip: Int): List<PostWithoutDetails> {
        return postCollection
            .withDocumentClass(PostWithoutDetails::class.java)
            .find(
                Filters.regex(PostWithoutDetails::title.name, query)
            )
            .sort(descending(PostWithoutDetails::date.name))
            .skip(skip)
            .limit(POSTS_PER_PAGE)
            .toList()
    }

    override suspend fun searchPostsByCategory(
        category: Category,
        skip: Int
    ): List<PostWithoutDetails> {
        return postCollection
            .withDocumentClass(PostWithoutDetails::class.java)
            .find(
                Filters.eq(PostWithoutDetails::category.name, category)
            )
            .sort(descending(PostWithoutDetails::date.name))
            .skip(skip)
            .limit(POSTS_PER_PAGE)
            .toList()
    }

    override suspend fun readSelectedPost(id: String): Post {
        return postCollection
            .find(
                Filters.eq("_id", id)
            )
            .toList()
            .first()
    }
}
