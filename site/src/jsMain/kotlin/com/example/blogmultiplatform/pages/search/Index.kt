package com.example.blogmultiplatform.pages.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.Constants
import com.example.blogmultiplatform.Res
import com.example.blogmultiplatform.components.CategoryNavigationItems
import com.example.blogmultiplatform.components.OverflowSidePanel
import com.example.blogmultiplatform.models.ApiListResponse
import com.example.blogmultiplatform.models.Category
import com.example.blogmultiplatform.models.PostWithoutDetails
import com.example.blogmultiplatform.sections.HeaderSection
import com.example.blogmultiplatform.sections.PostsSection
import com.example.blogmultiplatform.utils.searchPostsByCategory
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.px

@Page(routeOverride = "query")
@Composable
fun SearchPage() {
    val context = rememberPageContext()
    val breakpoint = rememberBreakpoint()
    val scope = rememberCoroutineScope()

    var overflowOpened by remember { mutableStateOf(false) }
    var showMorePosts by remember { mutableStateOf(false) }
    var postsToSkip by remember { mutableStateOf(0) }
    val searchedPosts = remember { mutableStateListOf<PostWithoutDetails>() }
    val hasCategoryParam = remember(key1 = context.route) {
        context.route.params.containsKey("category")
    }
    val value = remember(key1 = context.route) {
        if (hasCategoryParam) {
            context.route.params.getValue("category")
        } else {
            ""
        }
    }
    LaunchedEffect(key1 = context.route) {
        if (hasCategoryParam) {
            searchPostsByCategory(
                category = Category.valueOf(value),
                skip = 0,
                onSuccess = { response ->
                    if (response is ApiListResponse.Success) {
                        searchedPosts.clear()
                        postsToSkip = 0
                        searchedPosts.addAll(response.data)
                        postsToSkip += Constants.POSTS_PER_PAGE
                        showMorePosts = response.data.size >= Constants.POSTS_PER_PAGE
                        println(response.data)
                        println(response.data.size)
                    }
                },
                onError = {},
            )
        }
    }
    if (overflowOpened) {
        OverflowSidePanel(
            onMenuClose = { overflowOpened = false },
            content = { CategoryNavigationItems(vertical = true) }
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {

        HeaderSection(
            breakpoint = breakpoint,
            selectedCategory = Category.valueOf(value),
            logo = Res.Image.LOGO,
            onMenuOpen = { overflowOpened = true }
        )
        if (hasCategoryParam) {
            SpanText(
                modifier = Modifier
                    .fillMaxWidth()
                    .margin(
                        top = 50.px,
                        bottom = 40.px
                    )
                    .textAlign(TextAlign.Center)
                    .fontFamily(Constants.FONT_FAMILY)
                    .fontSize(36.px),
                text = value
            )
        }
        PostsSection(
            breakpoint = breakpoint,
            posts = searchedPosts,
            showMoreVisibility = showMorePosts,
            onShowMore = {
                scope.launch {
                    searchPostsByCategory(
                        category = Category.valueOf(value),
                        skip = postsToSkip,
                        onSuccess = { response ->
                            if (response is ApiListResponse.Success) {
                                if (response.data.isNotEmpty()) {
                                    if (response.data.size < Constants.POSTS_PER_PAGE) {
                                        showMorePosts = false
                                    }
                                    searchedPosts.clear()
                                    searchedPosts.addAll(response.data)
                                    postsToSkip += Constants.POSTS_PER_PAGE
                                } else {
                                    showMorePosts = false
                                }
                            }
                        },
                        onError = { }
                    )
                }
            },
            onClick = {}
        )
    }
}
