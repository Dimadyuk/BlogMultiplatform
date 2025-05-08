package com.example.androidapp.models

import com.example.blogmultiplatform.CategoryCommon
import kotlinx.serialization.Serializable

@Serializable
enum class Category(override val color: String) : CategoryCommon {
    Technology(color = ""),
    Programming(color = ""),
    Design(color = ""),
}
