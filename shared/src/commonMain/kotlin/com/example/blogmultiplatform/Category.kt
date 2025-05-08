package com.example.blogmultiplatform

expect enum class Category : CategoryColor {
    Technology,
    Programming,
    Design
}

interface CategoryColor {
    val color: String
}
