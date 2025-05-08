package com.example.blogmultiplatform.sections

import androidx.compose.runtime.Composable
import com.example.blogmultiplatform.Constants
import com.example.blogmultiplatform.components.PostsView
import com.example.blogmultiplatform.models.PostWithoutDetails
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.px

@Composable
fun PostsSection(
    breakpoint: Breakpoint,
    title: String? = null,
    posts: List<PostWithoutDetails>,
    showMoreVisibility: Boolean,
    onShowMore: () -> Unit,
    onClick: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .margin(topBottom = 50.px)
            .maxWidth(Constants.PAGE_WIDTH.px),
        contentAlignment = Alignment.TopCenter,
    ) {
        PostsView(
            breakpoint = breakpoint,
            title = title,
            posts = posts,
            showMoreVisible = showMoreVisibility,
            onShowMore = onShowMore,
            onClick = onClick,
        )
    }
}
