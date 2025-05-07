package com.example.androidapp.navigation

import com.example.blogmultiplatform.models.Category as PostCategory

sealed class Screen(val route: String) {
    data object HomeScreen : Screen(route = "home_screen")
    data object CategoryScreen : Screen(route = "category_screen/{category}") {
        fun passCategory(category: PostCategory): String {
            return "category_screen/${category.name}"
        }
    }

    data object DetailScreen : Screen(route = "detail_screen/{postId}") {
        fun passPostId(postId: String): String {
            return "detail_screen/$postId"
        }
    }
}
