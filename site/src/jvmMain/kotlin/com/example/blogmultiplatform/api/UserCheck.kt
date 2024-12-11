package com.example.blogmultiplatform.api

import com.example.blogmultiplatform.data.MongoDB
import com.example.blogmultiplatform.models.User
import com.example.blogmultiplatform.models.UserWithoutPassword
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.charset.StandardCharsets
import java.security.MessageDigest


@Api(routeOverride = "usercheck")
suspend fun userCheck(context: ApiContext) {
    val json = Json {
        ignoreUnknownKeys = true
    }

    try {
        val userRequest = context
            .req.body?.decodeToString()?.let {
                json.decodeFromString<User>(it)
            }
        val user = userRequest?.let {
            context.data.getValue<MongoDB>().checkUserExistence(
                User(
                    username = it.username,
                    password = hashPassword(it.password),
                )
            )
        }
        if (user != null) {
            context.res.setBodyText(
                json.encodeToString(
                    UserWithoutPassword(
                        id = user.id,
                        username = user.username,
                    )
                )
            )
        } else {
            context.res.setBodyText(
                json.encodeToString(
                    ErrorResponse("User not found")
                )
            )
        }
    } catch (e: Exception) {
        context.logger.error("Error in userCheck: ${e.message}")
        context.res.setBodyText(
            json.encodeToString(
                ErrorResponse(e.message ?: "Unknown error")
            )
        )
    }
}

private fun hashPassword(password: String): String {
    val messageDigest = MessageDigest.getInstance("SHA-256")
    val hashBytes = messageDigest.digest(password.toByteArray(StandardCharsets.UTF_8))
    val hexString = StringBuffer()
    for (byte in hashBytes) {
        hexString.append(String.format("%02x", byte))
    }
    return hexString.toString()
}
