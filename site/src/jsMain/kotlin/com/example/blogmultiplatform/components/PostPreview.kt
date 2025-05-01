package com.example.blogmultiplatform.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.Constants
import com.example.blogmultiplatform.models.PostWithoutDetails
import com.example.blogmultiplatform.models.Theme
import com.example.blogmultiplatform.styles.PostPreviewStyle
import com.example.blogmultiplatform.utils.parseDateString
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TextOverflow
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.textOverflow
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.toModifier
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.CheckboxInput

@Composable
fun PostPreview(
    modifier: Modifier = Modifier,
    post: PostWithoutDetails,
    selectableMode: Boolean = false,
    darkTheme: Boolean = false,
    vertical: Boolean = false,
    thumbnailHeight: Int = 320,
    titleMaxLength: Int = 2,
    titleColor: CSSColorValue = Colors.Black,
    onSelect: (String) -> Unit = {},
    onDeselect: (String) -> Unit = {},
    onClick: (String) -> Unit,
) {
    var checked by remember(selectableMode) { mutableStateOf(false) }

    if (vertical) {
        Row(
            modifier = PostPreviewStyle.toModifier()
                .then(modifier)
                .onClick {
                    onClick(post.id)
                }
                .cursor(Cursor.Pointer)
        ) {
            PostContent(
                post = post,
                darkTheme = darkTheme,
                selectableMode = selectableMode,
                checked = checked,
                vertical = vertical,
                thumbnailHeight = thumbnailHeight,
                titleMaxLength = titleMaxLength,
                titleColor = titleColor,
            )
        }
    } else {
        Column(
            modifier = PostPreviewStyle.toModifier()
                .then(modifier)
                .fillMaxWidth(
                    if (darkTheme) 100.percent
                    else if (titleColor == Theme.Sponsored.rgb) 100.percent
                    else 95.percent
                )
                .margin(bottom = 24.px)
                .padding(all = if (selectableMode) 10.px else 0.px)
                .borderRadius(r = 4.px)
                .border(
                    width = if (selectableMode) 4.px else 0.px,
                    style = if (selectableMode) LineStyle.Solid else LineStyle.None,
                    color = if (checked) Theme.Primary.rgb else Theme.Grey.rgb
                )
                .cursor(Cursor.Pointer)
                .transition(
                    Transition.of(
                        property = TransitionProperty.All,
                        duration = 200.ms,
                    )
                )
                .onClick {
                    if (selectableMode) {
                        checked = !checked
                        if (checked) {
                            onSelect(post.id)
                        } else {
                            onDeselect(post.id)
                        }
                    } else {
                        onClick(post.id)
                    }
                }
        ) {
            PostContent(
                post = post,
                darkTheme = darkTheme,
                selectableMode = selectableMode,
                checked = checked,
                vertical = vertical,
                thumbnailHeight = thumbnailHeight,
                titleMaxLength = titleMaxLength,
                titleColor = titleColor,
            )
        }
    }
}

@Composable
fun PostContent(
    post: PostWithoutDetails,
    titleColor: CSSColorValue,
    thumbnailHeight: Int,
    titleMaxLength: Int,
    vertical: Boolean,
    darkTheme: Boolean = false,
    selectableMode: Boolean = false,
    checked: Boolean = false,
) {
    Image(
        modifier = Modifier
            .margin(bottom = if (darkTheme) 20.px else 16.px)
            .height(thumbnailHeight.px)
            .fillMaxWidth()
            .objectFit(ObjectFit.Cover),
        src = post.thumbnail,
        description = "Post Thumbnail Image",
    )
    Column(
        modifier = Modifier
            .thenIf(
                condition = vertical,
                other = Modifier.margin(left = 20.px)
            )
            .fillMaxWidth()
    ) {
        SpanText(
            modifier = Modifier
                .fontFamily(Constants.FONT_FAMILY)
                .fontSize(12.px)
                .color(if (darkTheme) Theme.HalfWhite.rgb else Theme.HalfBlack.rgb),
            text = post.date.parseDateString()
        )
        SpanText(
            modifier = Modifier
                .fontFamily(Constants.FONT_FAMILY)
                .fontSize(20.px)
                .fontWeight(FontWeight.Bold)
                .margin(bottom = 8.px)
                .color(if (darkTheme) Colors.White else titleColor)
                .textOverflow(TextOverflow.Ellipsis)
                .overflow(Overflow.Hidden)
                .styleModifier {
                    property("display", "-webkit-box")
                    property("-webkit-line-clamp", "$titleMaxLength")
                    property("line-clamp", "$titleMaxLength")
                    property("-webkit-box-orient", "vertical")
                },
            text = post.title
        )
        SpanText(
            modifier = Modifier
                .margin(bottom = 8.px)
                .fontFamily(Constants.FONT_FAMILY)
                .fontSize(16.px)
                .color(if (darkTheme) Colors.White else Colors.Black)
                .textOverflow(TextOverflow.Ellipsis)
                .overflow(Overflow.Hidden)
                .styleModifier {
                    property("display", "-webkit-box")
                    property("-webkit-line-clamp", "3")
                    property("line-clamp", "3")
                    property("-webkit-box-orient", "vertical")
                },
            text = post.subtitle
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CategoryChip(
                category = post.category,
                darkTheme = darkTheme,
            )
            CheckboxInput(
                checked = checked,
                attrs = Modifier
                    .visibility(if (selectableMode) Visibility.Visible else Visibility.Hidden)
                    .size(20.px)
                    .toAttrs()
            )
        }
    }
}
