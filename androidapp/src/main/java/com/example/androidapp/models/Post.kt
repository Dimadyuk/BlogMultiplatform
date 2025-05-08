package com.example.androidapp.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class Post : RealmObject {
    @PrimaryKey
    val _id: String = ""
    val author: String = ""
    val date: Long = 0L
    val title: String = ""
    val subtitle: String = ""
    val thumbnail: String = ""
    val category: String = Category.Programming.name
}
