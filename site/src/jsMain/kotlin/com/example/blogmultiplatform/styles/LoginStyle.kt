package com.example.blogmultiplatform.styles

import com.example.blogmultiplatform.models.Theme
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.focus
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.px

val LoginInputStyle = CssStyle {
    base {
        Modifier.border(
            width = 2.px,
            style = LineStyle.Solid,
            color = Colors.Transparent
        )
            .transition(
                Transition.of(
                    property = "border",
                    duration = 300.ms
                )
            )
            .borderRadius(4.px)
    }
    focus {
        Modifier.border(
            width = 2.px,
            style = LineStyle.Solid,
            color = Theme.Primary.rgb
        )
    }
}

