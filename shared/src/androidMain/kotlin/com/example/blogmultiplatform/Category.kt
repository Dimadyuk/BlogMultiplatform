package com.example.blogmultiplatform

import kotlinx.serialization.Serializable

@Serializable
actual enum class Category(override val color: String) : CategoryColor {
    Technology(color = ""),
    Programming(color = ""),
    Design(color = "")
}
