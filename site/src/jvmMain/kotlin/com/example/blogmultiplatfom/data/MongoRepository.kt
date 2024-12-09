package com.example.blogmultiplatfom.data

import com.example.blogmultiplatfom.models.User

interface MongoRepository {
    suspend fun checkUserExistence(user: User): User?
}
