package com.example.blogmultiplatform.sections

import androidx.compose.runtime.Composable
import com.example.blogmultiplatform.Constants.FONT_FAMILY
import com.example.blogmultiplatform.Constants.HEADER_HEIGHT
import com.example.blogmultiplatform.Constants.PAGE_WIDTH
import com.example.blogmultiplatform.Res
import com.example.blogmultiplatform.components.SearchBar
import com.example.blogmultiplatform.models.Category
import com.example.blogmultiplatform.models.Theme
import com.example.blogmultiplatform.styles.CategoryItemStyle
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Composable
fun HeaderSection(breakpoint: Breakpoint) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(Theme.Secondary.rgb),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .backgroundColor(Theme.Secondary.rgb)
                .maxWidth(PAGE_WIDTH.px),
            contentAlignment = Alignment.TopCenter
        ) {
            Header(breakpoint = breakpoint)
        }
    }
}

@Composable
fun Header(breakpoint: Breakpoint) {
    Row(
        modifier = Modifier
            .fillMaxWidth(if (breakpoint > Breakpoint.MD) 80.percent else 90.percent)
            .height(HEADER_HEIGHT.px),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .margin(right = 50.px)
                .width(if (breakpoint >= Breakpoint.SM) 100.px else 70.px)
                .cursor(Cursor.Pointer)
                .onClick {
                    //TODO
                },
            src = Res.Image.LOGO_HOME,
            description = "Logo image",
        )
        if (breakpoint >= Breakpoint.LG) {
            Category.entries.forEach { category ->
                Link(
                    modifier = CategoryItemStyle.toModifier()
                        .margin(right = 24.px)
                        .fontFamily(FONT_FAMILY)
                        .fontSize(16.px)
                        .fontWeight(FontWeight.Medium)
                        .textDecorationLine(TextDecorationLine.None)
                        .onClick {
                            //TODO
                        },
                    path = "",
                    text = category.name
                )
            }
        }
        Spacer()
        SearchBar(onEnterClick = {
            //TODO
        })
    }
}
