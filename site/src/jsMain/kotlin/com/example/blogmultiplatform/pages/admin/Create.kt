package com.example.blogmultiplatform.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.Constants
import com.example.blogmultiplatform.Constants.SIDE_PANEL_WIDTH
import com.example.blogmultiplatform.Id
import com.example.blogmultiplatform.components.AdminPageLayout
import com.example.blogmultiplatform.models.Category
import com.example.blogmultiplatform.models.EditorKey
import com.example.blogmultiplatform.models.Post
import com.example.blogmultiplatform.models.Theme
import com.example.blogmultiplatform.styles.EditorKeyStyle
import com.example.blogmultiplatform.utils.IsUserLoggedIn
import com.example.blogmultiplatform.utils.addPost
import com.example.blogmultiplatform.utils.noBorder
import com.varabyte.kobweb.browser.file.loadDataUrlFromDisk
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.Resize
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.classNames
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.disabled
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.resize
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.forms.Input
import com.varabyte.kobweb.silk.components.forms.Switch
import com.varabyte.kobweb.silk.components.forms.SwitchSize
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.TextArea
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import kotlin.js.Date

data class CreatePageUiEvent(
    val id: String = "",
    val title: String = "",
    val subtitle: String = "",
    val thumbnail: String = "",
    val thumbnailInputSwitch: Boolean = true,
    val content: String = "",
    val category: Category = Category.Programming,
    val main: Boolean = false,
    val popular: Boolean = false,
    val sponsored: Boolean = false,
    val editorVisibility: Boolean = true,
)
@Page
@Composable
fun CreatePage() {
    IsUserLoggedIn {
        CreateScreen()
    }
}

@Composable
fun CreateScreen() {
    val breakpoint = rememberBreakpoint()

    var uiEvent by remember {
        mutableStateOf(CreatePageUiEvent())
    }
    val scope = rememberCoroutineScope()

    AdminPageLayout {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .margin(topBottom = 50.px)
                .padding(
                    left = if (breakpoint > Breakpoint.MD) SIDE_PANEL_WIDTH.px else 0.px
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .maxWidth(700.px),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SimpleGrid(
                    numColumns = numColumns(base = 1, sm = 3)
                ) {
                    Row(
                        modifier = Modifier
                            .margin(
                                right = if (breakpoint < Breakpoint.SM) 0.px else 24.px,
                                bottom = if (breakpoint < Breakpoint.SM) 12.px else 0.px
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            modifier = Modifier
                                .margin(
                                    right = 8.px
                                ),
                            checked = uiEvent.popular,
                            onCheckedChange = {
                                uiEvent = uiEvent.copy(popular = it)
                            },
                            size = SwitchSize.LG
                        )
                        SpanText(
                            modifier = Modifier
                                .fontSize(14.px)
                                .fontFamily(Constants.FONT_FAMILY)
                                .color(Theme.HalfBlack.rgb),
                            text = "Popular"
                        )
                    }
                    Row(
                        modifier = Modifier
                            .margin(
                                right = if (breakpoint < Breakpoint.SM) 0.px else 24.px,
                                bottom = if (breakpoint < Breakpoint.SM) 12.px else 0.px
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            modifier = Modifier
                                .margin(
                                    right = 8.px
                                ),
                            checked = uiEvent.main,
                            onCheckedChange = {
                                uiEvent = uiEvent.copy(main = it)
                            },
                            size = SwitchSize.LG
                        )
                        SpanText(
                            modifier = Modifier
                                .fontSize(14.px)
                                .fontFamily(Constants.FONT_FAMILY)
                                .color(Theme.HalfBlack.rgb),
                            text = "Main"
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            modifier = Modifier
                                .margin(
                                    right = 8.px
                                ),
                            checked = uiEvent.sponsored,
                            onCheckedChange = {
                                uiEvent = uiEvent.copy(sponsored = it)
                            },
                            size = SwitchSize.LG
                        )
                        SpanText(
                            modifier = Modifier
                                .fontSize(14.px)
                                .fontFamily(Constants.FONT_FAMILY)
                                .color(Theme.HalfBlack.rgb),
                            text = "Sponsored"
                        )
                    }
                }
                Input(
                    type = InputType.Text,
                    modifier = Modifier
                        .id(Id.titleInput)
                        .fillMaxWidth()
                        .height(54.px)
                        .margin(topBottom = 12.px)
                        .padding(leftRight = 20.px)
                        .backgroundColor(Theme.LightGray.rgb)
                        .color(Colors.Black)
                        .borderRadius(r = 4.px)
                        .noBorder()
                        .fontFamily(Constants.FONT_FAMILY)
                        .fontSize(16.px),
                    placeholder = "Title",
                    value = uiEvent.title,
                    onValueChange = {
                        uiEvent = uiEvent.copy(title = it)
                    },
                )
                Input(
                    type = InputType.Text,
                    focusBorderColor = Theme.Primary.rgb,
                    modifier = Modifier
                        .id(Id.subtitleInput)
                        .fillMaxWidth()
                        .height(54.px)
                        .margin(bottom = 12.px)
                        .padding(leftRight = 20.px)
                        .backgroundColor(Theme.LightGray.rgb)
                        .color(Colors.Black)
                        .borderRadius(r = 4.px)
                        .noBorder()
                        .fontFamily(Constants.FONT_FAMILY)
                        .fontSize(16.px),
                    placeholder = "Subtitle",
                    value = uiEvent.subtitle,
                    onValueChange = {
                        uiEvent = uiEvent.copy(subtitle = it)
                    },
                )
                var expanded by remember { mutableStateOf(false) }

                DropdownMenu(
                    selectedCategory = uiEvent.category,
                    onCategorySelect = {
                        expanded = false
                        uiEvent = uiEvent.copy(category = it)
                    },
                    onCategoryClick = {
                        expanded = !expanded
                    },
                    expanded = expanded
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .margin(topBottom = 12.px),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Switch(
                        modifier = Modifier
                            .margin(
                                right = 8.px
                            ),
                        checked = uiEvent.thumbnailInputSwitch,
                        onCheckedChange = {
                            uiEvent = uiEvent.copy(thumbnailInputSwitch = it)
                        },
                        size = SwitchSize.MD
                    )
                    SpanText(
                        modifier = Modifier
                            .fontSize(14.px)
                            .fontFamily(Constants.FONT_FAMILY)
                            .color(Theme.HalfBlack.rgb),
                        text = "Paste an image URL instead"
                    )
                }
                ThumbnailUploader(
                    thumbnail = uiEvent.thumbnail,
                    thumbnailInputEnabled = !uiEvent.thumbnailInputSwitch,
                    onThumbnailSelect = { filename, file ->
                        (document
                            .getElementById(Id.thumbnailInput) as HTMLInputElement)
                            .value = filename

                        uiEvent = uiEvent.copy(thumbnail = filename)
                    },
                    onValueChanged = {
                        uiEvent = uiEvent.copy(thumbnail = it)
                    }
                )
                EditorControls(
                    breakpoint = breakpoint,
                    editorVisibility = uiEvent.editorVisibility,
                    onEditorVisibilityChange = {
                        uiEvent = uiEvent.copy(editorVisibility = !uiEvent.editorVisibility)
                    }
                )
                Editor(editorVisibility = uiEvent.editorVisibility)

                CreateButton(
                    onClick = {
                        uiEvent = uiEvent.copy(
                            title = (document
                                .getElementById(Id.titleInput) as HTMLInputElement)
                                .value,
                            subtitle = (document
                                .getElementById(Id.subtitleInput) as HTMLInputElement)
                                .value,
                            content = (document
                                .getElementById(Id.editor) as HTMLTextAreaElement)
                                .value,
                        )
                        if (!uiEvent.thumbnailInputSwitch) {
                            uiEvent = uiEvent.copy(
                                thumbnail = (document
                                    .getElementById(Id.thumbnailInput) as HTMLInputElement)
                                    .value,
                            )
                        }


                        if (uiEvent.title.isNotBlank() &&
                            uiEvent.subtitle.isNotBlank() &&
                            uiEvent.thumbnail.isNotBlank() &&
                            uiEvent.content.isNotBlank()
                        ) {
                            scope.launch {
                                val result = addPost(
                                    Post(
                                        author = localStorage.getItem("username").toString(),
                                        date = Date.now().toLong(),
                                        title = uiEvent.title,
                                        subtitle = uiEvent.subtitle,
                                        thambnail = uiEvent.thumbnail,
                                        content = uiEvent.content,
                                        category = uiEvent.category,
                                        popular = uiEvent.popular,
                                        main = uiEvent.main,
                                        sponsored = uiEvent.sponsored
                                    )
                                )
                                if (result) {
                                    uiEvent = CreatePageUiEvent()
                                    println("Success")
                                }
                            }
                        } else {
                            println("Please fill all the fields")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun DropdownMenu(
    selectedCategory: Category,
    onCategorySelect: (Category) -> Unit,
    onCategoryClick: () -> Unit,
    expanded: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.px)
            .backgroundColor(Theme.LightGray.rgb)
            .color(Colors.Black)
            .cursor(Cursor.Pointer)
            .onClick { onCategoryClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(leftRight = 20.px),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SpanText(
                modifier = Modifier
                    .fontFamily(Constants.FONT_FAMILY)
                    .fontSize(16.px)
                    .color(Colors.Black),
                text = selectedCategory.name
            )
            Box(
                modifier = Modifier
                    .classNames("dropdown-toggle")
            )
        }
    }
    if (expanded) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.px,
                    style = LineStyle.Solid,
                    color = Colors.LightGray
                )
                .borderRadius(r = 4.px)
                .backgroundColor(Theme.LightGray.rgb),
            horizontalAlignment = Alignment.Start
        ) {
            Category.entries.forEach {
                DropdownItem(it.name) { onCategorySelect(it) }
            }
        }
    }
}

@Composable
fun DropdownItem(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.px)
            .cursor(Cursor.Pointer)
            .onClick { onClick() },
        contentAlignment = Alignment.CenterStart
    ) {
        SpanText(
            text = text,
            modifier = Modifier
                .fontFamily(Constants.FONT_FAMILY)
                .fontSize(16.px)
                .color(Colors.Black)
        )
    }
}

@Composable
fun ThumbnailUploader(
    thumbnail: String,
    thumbnailInputEnabled: Boolean,
    onThumbnailSelect: (String, String) -> Unit,
    onValueChanged: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .margin(bottom = 20.px)
            .height(54.px),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Input(
            type = InputType.Text,
            modifier = Modifier
                .id(Id.thumbnailInput)
                .fillMaxWidth()
                .height(54.px)
                .margin(topBottom = 12.px)
                .margin(right = 20.px)
                .padding(leftRight = 20.px)
                .backgroundColor(Theme.LightGray.rgb)
                .color(Colors.Black)
                .borderRadius(r = 4.px)
                .noBorder()
                .fontFamily(Constants.FONT_FAMILY)
                .fontSize(16.px)
                .thenIf(
                    condition = thumbnailInputEnabled,
                    other = Modifier.disabled()
                ),
            placeholder = "Thumbnail",
            value = thumbnail,
            onValueChange = {
                onValueChanged(it)
            },
        )
        Button(
            modifier = Modifier
                .fillMaxHeight()
                .padding(leftRight = 24.px)
                .backgroundColor(if (thumbnailInputEnabled) Theme.Primary.rgb else Theme.LightGray.rgb)
                .color(if (thumbnailInputEnabled) Colors.White else Theme.HalfBlack.rgb)
                .noBorder()
                .borderRadius(r = 4.px)
                .thenIf(
                    condition = !thumbnailInputEnabled,
                    other = Modifier.disabled()
                ),
            onClick = {
                document.loadDataUrlFromDisk(
                    accept = "image/png, image/jpeg",
                    onLoad = {
                        onThumbnailSelect(filename, it)
                    }
                )
            }
        ) {
            SpanText(
                text = "Upload"
            )
        }
    }
}

@Composable
fun EditorControls(
    modifier: Modifier = Modifier,
    breakpoint: Breakpoint,
    editorVisibility: Boolean,
    onEditorVisibilityChange: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        SimpleGrid(
            modifier = Modifier.fillMaxWidth(),
            numColumns = numColumns(base = 1, sm = 2)
        ) {
            Row(
                modifier = Modifier
                    .backgroundColor(Theme.LightGray.rgb)
                    .borderRadius(r = 4.px)
                    .height(54.px),
            ) {
                EditorKey.entries.forEach {
                    EditorKeyView(it)
                }
            }
            Box(
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(
                    modifier = Modifier
                        .height(54.px)
                        .thenIf(
                            condition = breakpoint < Breakpoint.SM,
                            other = Modifier.fillMaxWidth()
                        )
                        .margin(topBottom = if (breakpoint < Breakpoint.SM) 10.px else 0.px)
                        .padding(leftRight = 24.px)
                        .borderRadius(r = 4.px)
                        .backgroundColor(
                            if (editorVisibility) Theme.LightGray.rgb
                            else Theme.Primary.rgb
                        )
                        .color(
                            if (editorVisibility) Colors.DarkGray
                            else Colors.White
                        )
                        .noBorder(),
                    onClick = {
                        onEditorVisibilityChange()
                    }
                ) {
                    SpanText(
                        modifier = Modifier
                            .fontFamily(Constants.FONT_FAMILY)
                            .fontWeight(FontWeight.Medium)
                            .fontSize(14.px),
                        text = "Preview"
                    )
                }
            }
        }
    }
}

@Composable
fun EditorKeyView(key: EditorKey) {

    Box(
        modifier = EditorKeyStyle.toModifier()
            .fillMaxHeight()
            .padding(leftRight = 12.px)
            .borderRadius(r = 4.px)
            .cursor(Cursor.Pointer)
            .onClick { },
        contentAlignment = Alignment.Center
    ) {
        Image(
            src = key.icon,
            description = key.name,
        )
    }
}

@Composable
fun Editor(editorVisibility: Boolean) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextArea(
            attrs = Modifier
                .id(Id.editor)
                .fillMaxWidth()
                .height(400.px)
                .maxHeight(400.px)
                .resize(Resize.None)
                .margin(top = 8.px)
                .padding(all = 20.px)
                .backgroundColor(Theme.LightGray.rgb)
                .borderRadius(r = 4.px)
                .noBorder()
                .fontFamily(Constants.FONT_FAMILY)
                .fontSize(16.px)
                .visibility(if (editorVisibility) Visibility.Visible else Visibility.Hidden)
                .toAttrs {
                    attr("placeholder", "Type here...")
                }
        )
        Div(
            attrs = Modifier
                .id(Id.editorPreview)
                .fillMaxWidth()
                .height(400.px)
                .maxHeight(400.px)
                .resize(Resize.None)
                .margin(top = 8.px)
                .padding(all = 20.px)
                .backgroundColor(Theme.LightGray.rgb)
                .borderRadius(r = 4.px)
                .noBorder()
                .visibility(if (editorVisibility) Visibility.Hidden else Visibility.Visible)
                .overflow(Overflow.Auto)
                .scrollBehavior(ScrollBehavior.Smooth)
                .toAttrs()
        ) {

        }
    }
}

@Composable
fun CreateButton(onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.px)
            .margin(top = 24.px)
            .backgroundColor(Theme.Primary.rgb)
            .color(Colors.White)
            .borderRadius(r = 4.px)
            .noBorder()
            .fontFamily(Constants.FONT_FAMILY)
            .fontSize(16.px),
        onClick = { onClick.invoke() }
    ) {
        SpanText(text = "Create")
    }
}
