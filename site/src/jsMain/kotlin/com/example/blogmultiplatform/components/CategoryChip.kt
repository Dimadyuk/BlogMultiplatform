package com.example.blogmultiplatform.components

import androidx.compose.runtime.Composable
import com.example.blogmultiplatform.Constants.FONT_FAMILY
import com.example.blogmultiplatform.models.Category
import com.example.blogmultiplatform.models.Theme
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px

@Composable
fun CategoryChip(
    category: Category,
    darkTheme: Boolean = false,
) {
    Column(
        modifier = Modifier
            .height(32.px)
            .padding(leftRight = 14.px)
            .borderRadius(r = 100.px)
            .border(
                width = 1.px,
                style = LineStyle.Solid,
                color = if (darkTheme) Theme.entries.find { it.hex == category.color }?.rgb
                else Theme.HalfBlack.rgb,
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SpanText(
            modifier = Modifier
                .fillMaxWidth()
                .textAlign(TextAlign.Center)
                .fontFamily(FONT_FAMILY)
                .color(
                    if (darkTheme) Theme.entries.find { it.hex == category.color }?.rgb
                        ?: Theme.HalfWhite.rgb else Theme.HalfBlack.rgb
                )
                .fontSize(12.px),
            text = category.name
        )

    }
}
