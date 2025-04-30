package com.example.blogmultiplatform.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.Constants
import com.example.blogmultiplatform.components.CategoryNavigationItems
import com.example.blogmultiplatform.components.OverflowSidePanel
import com.example.blogmultiplatform.models.ApiListResponse
import com.example.blogmultiplatform.models.PostWithoutDetails
import com.example.blogmultiplatform.sections.HeaderSection
import com.example.blogmultiplatform.sections.MainSection
import com.example.blogmultiplatform.sections.NewsletterSection
import com.example.blogmultiplatform.sections.PostsSection
import com.example.blogmultiplatform.sections.SponsoredPostsSection
import com.example.blogmultiplatform.utils.fetchLatestPosts
import com.example.blogmultiplatform.utils.fetchMainPosts
import com.example.blogmultiplatform.utils.fetchPopularPosts
import com.example.blogmultiplatform.utils.fetchSponsoredPosts
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.coroutines.launch

@Page
@Composable
fun HomePage() {
    val breakpoint = rememberBreakpoint()
    var overflowMenuOpened by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    var mainPosts by remember { mutableStateOf<ApiListResponse>(ApiListResponse.Idle) }

    val sponsoredPosts = remember { mutableStateListOf<PostWithoutDetails>() }

    val latestPosts = remember { mutableStateListOf<PostWithoutDetails>() }
    var latestPostsToSkip by remember { mutableStateOf(0) }
    var showMoreLatest by remember { mutableStateOf(false) }

    val popularPosts = remember { mutableStateListOf<PostWithoutDetails>() }
    var popularPostsToSkip by remember { mutableStateOf(0) }
    var showMorePopular by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        fetchMainPosts(
            onSuccess = {
                mainPosts = it
                println("Main posts - $mainPosts")
            },
            onError = { }
        )
        fetchLatestPosts(
            skip = latestPostsToSkip,
            onSuccess = {
                if (it is ApiListResponse.Success) {
                    latestPosts.addAll(it.data)
                    latestPostsToSkip += Constants.POSTS_PER_PAGE
                    if (it.data.size >= Constants.POSTS_PER_PAGE) {
                        showMoreLatest = true
                    }
                }
                println("Latest posts - $it")
            },
            onError = { }
        )
        fetchSponsoredPosts(
            onSuccess = {
                if (it is ApiListResponse.Success) {
                    sponsoredPosts.addAll(it.data)
                }
                println("Sponsored posts - $it")
            },
            onError = { }
        )
        fetchPopularPosts(
            skip = popularPostsToSkip,
            onSuccess = {
                if (it is ApiListResponse.Success) {
                    popularPosts.addAll(it.data)
                    popularPostsToSkip += Constants.POSTS_PER_PAGE
                    if (it.data.size >= Constants.POSTS_PER_PAGE) {
                        showMorePopular = true
                    }
                }
                println("Popular posts - $it")
            },
            onError = { }
        )
    }
    if (overflowMenuOpened) {
        OverflowSidePanel(
            onMenuClose = { overflowMenuOpened = false },
            content = {
                CategoryNavigationItems(vertical = true)
            },
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HeaderSection(
            breakpoint = breakpoint,
            onMenuOpen = {
                overflowMenuOpened = true
            }
        )
        MainSection(
            breakpoint = breakpoint,
            posts = mainPosts,
        )
        PostsSection(
            breakpoint = breakpoint,
            title = "Latest Posts",
            posts = latestPosts,
            showMoreVisibility = showMoreLatest,
            onShowMore = {
                scope.launch {
                    fetchLatestPosts(
                        skip = latestPostsToSkip,
                        onSuccess = { response ->
                            if (response is ApiListResponse.Success) {
                                if (response.data.isNotEmpty()) {
                                    if (response.data.size < Constants.POSTS_PER_PAGE) {
                                        showMoreLatest = false
                                    }
                                    latestPosts.addAll(response.data)
                                    latestPostsToSkip += Constants.POSTS_PER_PAGE
                                } else {
                                    showMoreLatest = false
                                }
                            }
                        },
                        onError = { }
                    )
                }
            },
            onClick = {

            },
        )
        SponsoredPostsSection(
            breakpoint = breakpoint,
            posts = sponsoredPosts,
            onClick = {

            },
        )
        PostsSection(
            breakpoint = breakpoint,
            title = "Popular Posts",
            posts = popularPosts,
            showMoreVisibility = showMorePopular,
            onShowMore = {
                scope.launch {
                    fetchPopularPosts(
                        skip = popularPostsToSkip,
                        onSuccess = { response ->
                            if (response is ApiListResponse.Success) {
                                if (response.data.isNotEmpty()) {
                                    if (response.data.size < Constants.POSTS_PER_PAGE) {
                                        showMorePopular = false
                                    }
                                    popularPosts.addAll(response.data)
                                    popularPostsToSkip += Constants.POSTS_PER_PAGE
                                } else {
                                    showMorePopular = false
                                }
                            }
                        },
                        onError = { }
                    )
                }
            },
            onClick = {

            },
        )
        NewsletterSection(
            breakpoint = breakpoint
        )
    }
}
