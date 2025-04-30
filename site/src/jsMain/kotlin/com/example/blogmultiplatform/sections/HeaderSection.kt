package com.example.blogmultiplatform.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.Constants.HEADER_HEIGHT
import com.example.blogmultiplatform.Constants.PAGE_WIDTH
import com.example.blogmultiplatform.Res
import com.example.blogmultiplatform.components.CategoryNavigationItems
import com.example.blogmultiplatform.components.SearchBar
import com.example.blogmultiplatform.models.Category
import com.example.blogmultiplatform.models.Theme
import com.example.blogmultiplatform.navigation.Screen
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.fa.FaBars
import com.varabyte.kobweb.silk.components.icons.fa.FaXmark
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Composable
fun HeaderSection(
    breakpoint: Breakpoint,
    selectedCategory: Category? = null,
    logo: String = Res.Image.LOGO_HOME,
    onMenuOpen: () -> Unit,
) {
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
            Header(
                breakpoint = breakpoint,
                onMenuClick = onMenuOpen,
                logo = logo,
                selectedCategory = selectedCategory,
            )
        }
    }
}

@Composable
fun Header(
    breakpoint: Breakpoint,
    logo: String,
    selectedCategory: Category?,
    onMenuClick: () -> Unit,
) {
    var fullSearchBarOpened by remember {
        mutableStateOf(false)
    }
    val context = rememberPageContext()

    Row(
        modifier = Modifier
            .fillMaxWidth(if (breakpoint > Breakpoint.MD) 80.percent else 90.percent)
            .height(HEADER_HEIGHT.px),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (breakpoint <= Breakpoint.MD) {
            if (fullSearchBarOpened) {
                FaXmark(
                    modifier = Modifier
                        .margin(right = 24.px)
                        .color(Colors.White)
                        .cursor(Cursor.Pointer)
                        .onClick {
                            fullSearchBarOpened = false
                        },
                    size = IconSize.XL
                )
            }
            if (!fullSearchBarOpened) {
                FaBars(
                    modifier = Modifier
                        .margin(right = 24.px)
                        .color(Colors.White)
                        .cursor(Cursor.Pointer)
                        .onClick {
                            onMenuClick()
                        },
                    size = IconSize.XL
                )
            }
        }
        if (!fullSearchBarOpened) {
            Image(
                modifier = Modifier
                    .margin(right = 50.px)
                    .width(if (breakpoint >= Breakpoint.SM) 100.px else 70.px)
                    .cursor(Cursor.Pointer)
                    .onClick {
                        context.router.navigateTo(Screen.Home.route)
                    },
                src = logo,
                description = "Logo image",
            )
        }
        if (breakpoint >= Breakpoint.MD) {
            CategoryNavigationItems(
                selectedCategory = selectedCategory,
            )
        }

        Spacer()

        SearchBar(
            onEnterClick = {
                //TODO
            },
            fullWidth = fullSearchBarOpened,
            darkTheme = true,
            breakpoint = breakpoint,
            onSearchIconClick = { fullSearchBarOpened = it },
        )
    }
}
