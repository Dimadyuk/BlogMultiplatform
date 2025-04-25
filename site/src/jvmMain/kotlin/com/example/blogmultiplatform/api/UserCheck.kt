package com.example.blogmultiplatform.api

import com.example.blogmultiplatform.data.MongoDB
import com.example.blogmultiplatform.models.User
import com.example.blogmultiplatform.models.UserWithoutPassword
import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import java.nio.charset.StandardCharsets
import java.security.MessageDigest


@Api(routeOverride = "usercheck")
suspend fun userCheck(context: ApiContext) {
    try {
        val userRequest = context
            .req.getBody<User>()
        val user = userRequest?.let {
            context.data.getValue<MongoDB>().checkUserExistence(
                User(
                    username = it.username,
                    password = hashPassword(it.password),
                )
            )
        }
        if (user != null) {
            context.res.setBody(
                    UserWithoutPassword(
                        id = user.id,
                        username = user.username,
                    )
            )
        } else {
            context.res.setBody(
                    ErrorResponse("User not found")
            )
        }
    } catch (e: Exception) {
        context.logger.error("Error in userCheck: ${e.message}")
        context.res.setBody(
                ErrorResponse(e.message ?: "Unknown error")
        )
    }
}

@Api(routeOverride = "checkuserid")
suspend fun checkUserId(context: ApiContext) {
    try {
        val id = context.req.getBody<String>()
        val result = id?.let {
            context.data.getValue<MongoDB>().checkUserId(it)
        }
        if (result != null) {
            context.res.setBody(
                result
            )
        } else {
            context.res.setBody(
                false
            )
        }
    } catch (e: Exception) {
        context.logger.error("Error in checkUserId: ${e.message}")
        context.res.setBody(
            false
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
