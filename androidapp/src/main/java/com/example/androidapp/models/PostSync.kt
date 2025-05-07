package com.example.androidapp.models

import com.example.blogmultiplatform.models.Category
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
open class PostSync : RealmObject {
    @PrimaryKey
    val _id: String = ""
    val author: String = ""
    val date: Long = 0L
    val title: String = ""
    val subtitle: String = ""
    val thumbnail: String = ""
    val category: String = Category.Programming.name
}
