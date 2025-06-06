package com.example.androidapp.data

import com.example.androidapp.models.Category
import com.example.androidapp.models.Post
import com.example.androidapp.util.Constants.CONNECTION_STRING
import com.example.androidapp.util.RequestState
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

object MongoSync : MongoSyncRepository {
    private val app = App.create(CONNECTION_STRING)
    private val user = app.currentUser
    private lateinit var realm: Realm

    init {
        configureTheRealm()
    }

    override fun configureTheRealm() {
        if (user != null) {
            val config = SyncConfiguration.Builder(user, setOf(Post::class))
                .initialSubscriptions {
                    add(
                        query = it.query(Post::class),
                        name = "Blog Posts"
                    )
                }
                .build()
            realm = Realm.open(config)
        }
    }

    override fun readAllPosts(): Flow<RequestState<List<Post>>> {
        return if (user != null) {
            try {
                realm.query(Post::class)
                    .asFlow()
                    .map { result ->
                        RequestState.Success(data = result.list)
                    }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(Exception(e.message))) }
            }
        } else {
            flow { emit(RequestState.Error(Exception("User not authenticated."))) }
        }
    }

    override fun searchPostsByTitle(query: String): Flow<RequestState<List<Post>>> {
        return if (user != null) {
            try {
                realm.query<Post>(query = "title CONTAINS[c] $0", query)
                    .asFlow()
                    .map { result ->
                        RequestState.Success(data = result.list)
                    }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(Exception(e.message))) }
            }
        } else {
            flow { emit(RequestState.Error(Exception("User not authenticated."))) }
        }
    }

    override fun searchPostsByCategory(category: Category): Flow<RequestState<List<Post>>> {
        return if (user != null) {
            try {
                realm.query<Post>(query = "category == $0", category.name)
                    .asFlow()
                    .map { result ->
                        RequestState.Success(data = result.list)
                    }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(Exception(e.message))) }
            }
        } else {
            flow { emit(RequestState.Error(Exception("User not authenticated."))) }
        }
    }
}
