package com.example.blogmultiplatform.pages.posts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.Constants
import com.example.blogmultiplatform.Constants.POST_ID_PARAM
import com.example.blogmultiplatform.Id
import com.example.blogmultiplatform.Res
import com.example.blogmultiplatform.components.ErrorView
import com.example.blogmultiplatform.components.LoadingIndicator
import com.example.blogmultiplatform.components.NavigationItems
import com.example.blogmultiplatform.components.OverflowSidePanel
import com.example.blogmultiplatform.models.ApiResponse
import com.example.blogmultiplatform.models.Post
import com.example.blogmultiplatform.models.Theme
import com.example.blogmultiplatform.sections.HeaderSection
import com.example.blogmultiplatform.utils.fetchSelectedPost
import com.example.blogmultiplatform.utils.parseDateString
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextOverflow
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.textOverflow
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.HTMLDivElement

@Page(routeOverride = "post")
@Composable
fun PostPage() {
    val scope = rememberCoroutineScope()
    val context = rememberPageContext()
    var apiResponse by remember { mutableStateOf<ApiResponse>(ApiResponse.Idle) }
    val breakpoint = rememberBreakpoint()
    var overflowMenuOpened by remember { mutableStateOf(false) }

    val hasPostIdParam = remember(context.route) {
        context.route.params.containsKey(POST_ID_PARAM)
    }

    LaunchedEffect(context.route) {
        if (hasPostIdParam) {
            val postId = context.route.params.getValue(POST_ID_PARAM)
            apiResponse = fetchSelectedPost(id = postId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (overflowMenuOpened) {
            OverflowSidePanel(
                onMenuClose = { overflowMenuOpened = false },
                content = { NavigationItems() },
            )
        }
        HeaderSection(
            breakpoint = breakpoint,
            logo = Res.Image.LOGO,
            onMenuOpen = {
                overflowMenuOpened = true
            }
        )
        when (apiResponse) {
            is ApiResponse.Idle -> {
                LoadingIndicator()
            }

            is ApiResponse.Success -> {
                PostContent(post = (apiResponse as ApiResponse.Success).data)
                scope.launch {
                    delay(100)
                    try {
                        js("if (typeof hljs !== 'undefined') hljs.highlightAll()") as Unit
                    } catch (e: Exception) {
                        console.log("Error: ${e.message}")
                    }
                }
            }

            is ApiResponse.Error -> {
                val errorMessage = (apiResponse as ApiResponse.Error).message
                console.log("Error: $errorMessage")
                ErrorView(message = errorMessage)
            }
        }
    }
}

@Composable
fun PostContent(post: Post) {
    LaunchedEffect(key1 = post) {
        (document.getElementById(Id.postContent) as HTMLDivElement).innerHTML = post.content
    }

    Column(
        modifier = Modifier
            .margin(top = 50.px, bottom = 100.px)
            .fillMaxWidth()
            .maxWidth(800.px),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SpanText(
            modifier = Modifier
                .fillMaxWidth()
                .fontFamily(Constants.FONT_FAMILY)
                .fontSize(14.px)
                .color(Theme.HalfBlack.rgb),
            text = post.date.parseDateString()
        )
        SpanText(
            modifier = Modifier
                .fillMaxWidth()
                .margin(bottom = 20.px)
                .color(Theme.HalfBlack.rgb)
                .fontFamily(Constants.FONT_FAMILY)
                .fontSize(40.px)
                .fontWeight(FontWeight.Bold)
                .textOverflow(TextOverflow.Ellipsis)
                .styleModifier {
                    property("display", "-webkit-box")
                    property("-webkit-line-clamp", "2")
                    property("line-clamp", "2")
                    property("-webkit-box-orient", "vertical")
                },
            text = post.title
        )
        Image(
            modifier = Modifier
                .margin(bottom = 40.px)
                .fillMaxWidth()
                .maxWidth(600.px),
            src = post.thumbnail
        )
        Div(
            attrs = Modifier
                .id(Id.postContent)
                .fontFamily(Constants.FONT_FAMILY)
                .fillMaxWidth()
                .toAttrs()
        )
    }
}
