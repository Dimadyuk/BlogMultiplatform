package com.example.blogmultiplatform.models

expect class Post {
    val id: String
    val author: String
    val date: Long
    val title: String
    val subtitle: String
    val thambnail: String
    val content: String
    val category: Category
    val popular: Boolean
    val main: Boolean
    val sponsored: Boolean
}

expect class PostWithoutDetails {
    val id: String
    val author: String
    val date: Long
    val title: String
    val subtitle: String
    val thambnail: String
    val category: Category
}
