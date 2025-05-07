package com.example.androidapp.data

import com.example.androidapp.models.PostSync
import com.example.androidapp.util.Constants.CONNECTION_STRING
import com.example.androidapp.util.RequestState
import io.realm.kotlin.Realm
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
            val config = SyncConfiguration.Builder(
                user = user,
                schema = setOf(PostSync::class)
            ).initialSubscriptions { realm ->
                add(
                    query = realm.query(PostSync::class),
                    name = "Blog Posts",
                )
            }
                .build()
            realm = Realm.open(config)
        }
    }

    override fun readAllPosts(): Flow<RequestState<List<PostSync>>> {
        return if (user != null) {
            try {
                realm.query(PostSync::class)
                    .asFlow()
                    .map { result ->
                        RequestState.Success(result.list)
                    }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(e.message.toString())) }
            }
        } else {
            flow { emit(RequestState.Error("User is not logged in")) }
        }
    }
}
