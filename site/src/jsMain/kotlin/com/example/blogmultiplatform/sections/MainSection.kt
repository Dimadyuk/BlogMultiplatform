package com.example.blogmultiplatform.sections

import androidx.compose.runtime.Composable
import com.example.blogmultiplatform.Constants
import com.example.blogmultiplatform.components.PostPreview
import com.example.blogmultiplatform.models.ApiListResponse
import com.example.blogmultiplatform.models.PostWithoutDetails
import com.example.blogmultiplatform.models.Theme
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Composable
fun MainSection(
    breakpoint: Breakpoint,
    posts: ApiListResponse,
    onClick: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(Theme.Secondary.rgb),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(Constants.PAGE_WIDTH.px),
            contentAlignment = Alignment.Center,
        ) {
            when (posts) {
                is ApiListResponse.Error -> {}
                is ApiListResponse.Idle -> {}
                is ApiListResponse.Success -> {
                    MainPosts(
                        posts = posts.data,
                        breakpoint = breakpoint,
                        onClick = onClick
                    )
                }
            }
        }
    }
}

@Composable
fun MainPosts(
    breakpoint: Breakpoint,
    posts: List<PostWithoutDetails>,
    onClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(if (breakpoint >= Breakpoint.MD) 80.percent else 90.percent)
            .margin(topBottom = 50.px)
    ) {
        if (breakpoint == Breakpoint.XL) {
            PostPreview(
                post = posts.first(),
                thumbnailHeight = 640,
                darkTheme = true,
                onClick = { onClick(posts.first().id) },
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth(80.percent)
                    .margin(left = 20.px)
            ) {
                posts.drop(1).forEach { post ->
                    PostPreview(
                        post = post,
                        darkTheme = true,
                        vertical = true,
                        thumbnailHeight = 200,
                        titleMaxLength = 1,
                        onClick = { onClick(post.id) },
                    )
                }
            }
        } else if (breakpoint >= Breakpoint.LG) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .margin(right = 20.px),
                contentAlignment = Alignment.Center,
            ) {
                PostPreview(
                    post = posts.first(),
                    darkTheme = true,
                    onClick = { onClick(posts.first().id) },
                )
            }
            PostPreview(
                post = posts[1],
                darkTheme = true,
                onClick = { onClick(posts[1].id) },
            )
        } else {
            PostPreview(
                post = posts.first(),
                thumbnailHeight = 640,
                darkTheme = true,
                onClick = { onClick(posts.first().id) },
            )
        }
    }
}
