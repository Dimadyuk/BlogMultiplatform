package com.example.blogmultiplatform.navigation

import com.example.blogmultiplatform.models.Category

sealed class Screen(val route: String) {
    data object Home : Screen(route = "/")
    data object AdminHome : Screen(route = "/admin/")
    data object AdminLogin : Screen(route = "/admin/login")
    data object AdminCreate : Screen(route = "/admin/create") {
        fun passPostId(id: String): String = "/admin/create?postId=$id"
    }
    data object AdminMyPosts : Screen(route = "/admin/myposts") {
        fun searchByTitle(query: String): String = "/admin/myposts?query=$query"
    }
    data object AdminSuccess : Screen(route = "/admin/success") {
        fun postUpdated() = "/admin/success?updated=true"
    }
    data object Search : Screen(route = "/search/query") {
        fun searchByCategory(category: Category) = "/search/query?category=${category.name}"
        fun searchByTitle(query: String) = "/search/query?query=${query}"
    }
}
