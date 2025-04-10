package com.example.blogmultiplatform.pages.admin

import androidx.compose.runtime.Composable
import com.example.blogmultiplatform.Constants.SIDE_PANEL_WIDTH
import com.example.blogmultiplatform.components.AdminPageLayout
import com.example.blogmultiplatform.components.SearchBar
import com.example.blogmultiplatform.utils.IsUserLoggedIn
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Page(routeOverride = "/admin/myposts")
@Composable
fun MyPostsPage() {
    IsUserLoggedIn {
        MyPostsScreen()
    }
}

@Composable
fun MyPostsScreen() {
    val breakpoint = rememberBreakpoint()

    AdminPageLayout {
        Column(
            modifier = Modifier
                .margin(topBottom = 50.px)
                .fillMaxSize()
                .padding(left = if (breakpoint > Breakpoint.MD) SIDE_PANEL_WIDTH.px else 0.px),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(
                    if (breakpoint > Breakpoint.MD) 30.percent else 50.percent
                ),
                contentAlignment = Alignment.Center
            ) {
                SearchBar(
                    onEnterClick = {}
                )
            }

        }
    }
}
