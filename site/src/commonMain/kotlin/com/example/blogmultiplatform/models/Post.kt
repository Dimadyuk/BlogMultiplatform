package com.example.blogmultiplatform.models

import com.example.blogmultiplatform.CategoryCommon
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    @SerialName("_id")
    val id: String = "",
    val author: String = "",
    val date: Long = 0L,
    val title: String,
    val subtitle: String,
    val thumbnail: String,
    val content: String = "",
    val category: CategoryCommon,
    val popular: Boolean = false,
    val main: Boolean = false,
    val sponsored: Boolean = false,
)

@Serializable
data class PostWithoutDetails(
    @SerialName("_id")
    val id: String = "",
    val author: String = "",
    val date: Long = 0L,
    val title: String,
    val subtitle: String,
    val thumbnail: String,
    val category: CategoryCommon,
    val popular: Boolean = false,
    val main: Boolean = false,
    val sponsored: Boolean = false,
)

