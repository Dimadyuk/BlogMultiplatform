package com.example.blogmultiplatform.pages.admin

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.core.Page
import org.jetbrains.compose.web.dom.Text


@Page
@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .backgroundColor(Colors.White)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Home"
        )
    }
}
