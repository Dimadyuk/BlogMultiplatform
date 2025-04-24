package com.example.blogmultiplatform.navigation

sealed class Screen(val route: String) {
    data object AdminHome : Screen(route = "/admin/")
    data object AdminLogin : Screen(route = "/admin/login")
    data object AdminCreate : Screen(route = "/admin/create") {
        fun passPostId(id: String): String = "/admin/create?postId=$id"
    }
    data object AdminMyPosts : Screen(route = "/admin/myposts") {
        fun searchByTitle(query: String): String = "/admin/myposts?query=$query"
    }
    data object AdminSuccess : Screen(route = "/admin/success")
}
