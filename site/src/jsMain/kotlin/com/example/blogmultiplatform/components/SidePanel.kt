package com.example.blogmultiplatform.components

import androidx.compose.runtime.Composable
import com.example.blogmultiplatform.models.Theme
import com.example.blogmultiplatform.navigation.Screen
import com.example.blogmultiplatform.styles.NavigationItemStyle
import com.example.blogmultiplatform.utils.Constants.FONT_FAMILY
import com.example.blogmultiplatform.utils.Constants.SIDE_BAR_WIDTH
import com.example.blogmultiplatform.utils.Id
import com.example.blogmultiplatform.utils.Res
import com.example.blogmultiplatform.utils.logout
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.dom.svg.Path
import com.varabyte.kobweb.compose.dom.svg.Svg
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh

@Composable
fun SidePanel() {
    val context = rememberPageContext()
    Column(
        modifier = Modifier
            .backgroundColor(Theme.Secondary.rgb)
            .padding(leftRight = 40.px, topBottom = 50.px)
            .width(SIDE_BAR_WIDTH.px)
            .height(100.vh)
            .position(Position.Fixed)
            .zIndex(9)
    ) {
        Image(
            modifier = Modifier.margin(bottom = 60.px),
            src = Res.Image.LOGO,
            description = "Logo Image"
        )
        SpanText(
            modifier = Modifier
                .margin(bottom = 30.px)
                .fontFamily(FONT_FAMILY)
                .fontSize(14.px)
                .color(Theme.HalfWhite.rgb),
            text = "Dashboard",
        )
        NavigationItem(
            modifier = Modifier.margin(bottom = 20.px),
            selected = context.route.path == Screen.AdminHome.route,
            title = "Home",
            icon = Res.PathIcon.home,
            onClick = {
                context.router.navigateTo(Screen.AdminHome.route)
            }
        )
        NavigationItem(
            modifier = Modifier.margin(bottom = 20.px),
            selected = context.route.path == Screen.AdminCreate.route,
            title = "Create Post",
            icon = Res.PathIcon.create,
            onClick = {
                context.router.navigateTo(Screen.AdminCreate.route)
            }
        )
        NavigationItem(
            modifier = Modifier.margin(bottom = 20.px),
            selected = context.route.path == Screen.AdminMyPosts.route,
            title = "My Posts",
            icon = Res.PathIcon.posts,
            onClick = {
                context.router.navigateTo(Screen.AdminMyPosts.route)
            }
        )
        NavigationItem(
            selected = false,
            title = "Logout",
            icon = Res.PathIcon.logout,
            onClick = {
                logout()
                context.router.navigateTo(Screen.AdminLogin.route)
            }
        )
    }

}

@Composable
fun NavigationItem(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    title: String = "Home",
    icon: String = Res.PathIcon.home,
    onClick: () -> Unit,
) {
    Row(
        modifier = NavigationItemStyle.toModifier()
            .then(modifier)
            .cursor(Cursor.Pointer)
            .onClick { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        val color = if (selected) Theme.Primary else Theme.White
        VectorIcon(
            modifier = Modifier.margin(right = 10.px),
            selected = selected,
            path = icon,
        )
        SpanText(
            modifier = Modifier
                .id("${Id.navigationText}")
                .fontFamily(FONT_FAMILY)
                .fontSize(16.px)
                .thenIf(
                    condition = selected,
                    other = Modifier.color(color.rgb),
                ),

            text = title
        )
    }
}

@Composable
fun VectorIcon(
    modifier: Modifier = Modifier,
    path: String,
    selected: Boolean = false,
) {
    Svg(
        attrs = modifier
            .id("${Id.svgParent}")
            .width(24.px)
            .height(24.px)
            .toAttrs {
                attr("viewBox", "0 0 24 24")
                attr("fill", "none")
            }
    ) {
        Path(
            attrs = Modifier
                .id("${Id.vectorIcon}")
                .thenIf(
                    condition = !selected,
                    other = Modifier.styleModifier {
                        property("stroke", Theme.White.hex)
                    },
                )
                .thenIf(
                    condition = selected,
                    other = Modifier.styleModifier {
                        property("stroke", Theme.Primary.hex)
                    },
                )
                .toAttrs {
                attr("d", path)
                attr("stroke-width", "2")
                attr("stroke-linecap", "round")
                attr("stroke-linejoin", "round")
            }
        )
    }
}
