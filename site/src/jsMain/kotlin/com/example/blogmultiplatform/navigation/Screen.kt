package com.example.blogmultiplatform.navigation

import com.example.blogmultiplatform.Constants.CATEGORY_PARAM
import com.example.blogmultiplatform.Constants.POST_ID_PARAM
import com.example.blogmultiplatform.Constants.QUERY_PARAM
import com.example.blogmultiplatform.Constants.UPDATE_PARAM
import com.example.blogmultiplatform.models.Category

sealed class Screen(val route: String) {
    data object Home : Screen(route = "/")
    data object AdminHome : Screen(route = "/admin/")
    data object AdminLogin : Screen(route = "/admin/login")
    data object AdminCreate : Screen(route = "/admin/create") {
        fun passPostId(id: String): String = "/admin/create?$POST_ID_PARAM=$id"
    }
    data object AdminMyPosts : Screen(route = "/admin/myposts") {
        fun searchByTitle(query: String): String = "/admin/myposts?$QUERY_PARAM=$query"
    }
    data object AdminSuccess : Screen(route = "/admin/success") {
        fun postUpdated() = "/admin/success?$UPDATE_PARAM=true"
    }
    data object Search : Screen(route = "/search/query") {
        fun searchByCategory(category: Category) =
            "/search/$QUERY_PARAM?$CATEGORY_PARAM=${category.name}"

        fun searchByTitle(query: String) = "/search/query?$QUERY_PARAM=${query}"
    }

    data object PostPage : Screen(route = "/posts/post") {
        fun getPost(id: String) = "/posts/post?$POST_ID_PARAM=$id"
    }
}
