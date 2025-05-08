package com.example.blogmultiplatform.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.blogmultiplatform.Constants
import com.example.blogmultiplatform.Constants.POST_ID_PARAM
import com.example.blogmultiplatform.Constants.SIDE_PANEL_WIDTH
import com.example.blogmultiplatform.Id
import com.example.blogmultiplatform.components.AdminPageLayout
import com.example.blogmultiplatform.components.LinkPopup
import com.example.blogmultiplatform.components.MessagePopup
import com.example.blogmultiplatform.models.ApiResponse
import com.example.blogmultiplatform.models.Category
import com.example.blogmultiplatform.models.ControlStyle
import com.example.blogmultiplatform.models.EditorControl
import com.example.blogmultiplatform.models.Post
import com.example.blogmultiplatform.models.Theme
import com.example.blogmultiplatform.navigation.Screen
import com.example.blogmultiplatform.styles.EditorKeyStyle
import com.example.blogmultiplatform.utils.IsUserLoggedIn
import com.example.blogmultiplatform.utils.addPost
import com.example.blogmultiplatform.utils.applyControlStyle
import com.example.blogmultiplatform.utils.applyStyle
import com.example.blogmultiplatform.utils.fetchSelectedPost
import com.example.blogmultiplatform.utils.getEditor
import com.example.blogmultiplatform.utils.getSelectedText
import com.example.blogmultiplatform.utils.noBorder
import com.example.blogmultiplatform.utils.updatePost
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
import com.varabyte.kobweb.compose.ui.modifiers.onKeyDown
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.resize
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.TextArea
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import kotlin.js.Date

data class CreatePageUiState(
    val id: String = "",
    val title: String = "",
    val subtitle: String = "",
    val thumbnail: String = "",
    val thumbnailInputEnabled: Boolean = false,
    val content: String = "",
    val category: Category = Category.Programming,
    val buttonText: String = "Create",
    val main: Boolean = false,
    val popular: Boolean = false,
    val sponsored: Boolean = false,
    val editorVisibility: Boolean = true,
    val messagePopup: Boolean = false,
    val linkPopup: Boolean = false,
    val imagePopup: Boolean = false,
) {
    fun reset() = this.copy(
        id = "",
        title = "",
        subtitle = "",
        thumbnail = "",
        thumbnailInputEnabled = false,
        content = "",
        category = Category.Programming,
        buttonText = "Create",
        main = false,
        popular = false,
        sponsored = false,
        editorVisibility = true,
        messagePopup = false,
        linkPopup = false,
        imagePopup = false
    )
}

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

    var uiState by remember {
        mutableStateOf(CreatePageUiState())
    }
    val scope = rememberCoroutineScope()
    val context = rememberPageContext()

    val hasPostIdParam = remember(key1 = context.route) {
        context.route.params.containsKey(POST_ID_PARAM)
    }
    LaunchedEffect(hasPostIdParam) {
        if (hasPostIdParam) {
            val postId = context.route.params[POST_ID_PARAM] ?: ""
            val response = fetchSelectedPost(postId)
            if (response is ApiResponse.Success) {
                println(response.data)
                (document.getElementById(Id.editor) as HTMLTextAreaElement).value =
                    response.data.content

                uiState = uiState.copy(
                    id = response.data.id,
                    title = response.data.title,
                    subtitle = response.data.subtitle,
                    thumbnail = response.data.thumbnail,
                    content = response.data.content,
                    category = response.data.category as Category,
                    buttonText = "Update",
                    main = response.data.main,
                    popular = response.data.popular,
                    sponsored = response.data.sponsored
                )
            }
        } else {
            (document.getElementById(Id.editor) as HTMLTextAreaElement).value = ""
            uiState = uiState.reset()
        }
    }

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
                            checked = uiState.popular,
                            onCheckedChange = {
                                uiState = uiState.copy(popular = it)
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
                            checked = uiState.main,
                            onCheckedChange = {
                                uiState = uiState.copy(main = it)
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
                            checked = uiState.sponsored,
                            onCheckedChange = {
                                uiState = uiState.copy(sponsored = it)
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
                    value = uiState.title,
                    onValueChange = {
                        uiState = uiState.copy(title = it)
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
                    value = uiState.subtitle,
                    onValueChange = {
                        uiState = uiState.copy(subtitle = it)
                    },
                )
                var expanded by remember { mutableStateOf(false) }

                DropdownMenu(
                    selectedCategory = uiState.category,
                    onCategorySelect = {
                        expanded = false
                        uiState = uiState.copy(category = it)
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
                        checked = uiState.thumbnailInputEnabled,
                        onCheckedChange = {
                            uiState = uiState.copy(thumbnailInputEnabled = it)
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
                    thumbnail = uiState.thumbnail,
                    thumbnailInputEnabled = !uiState.thumbnailInputEnabled,
                    onThumbnailSelect = { filename, file ->
                        (document
                            .getElementById(Id.thumbnailInput) as HTMLInputElement)
                            .value = filename

                        uiState = uiState.copy(thumbnail = file)
                        println("file - $file")
                        println("filename -$filename")
                    },
                    onValueChanged = {
                        uiState = uiState.copy(thumbnail = it)
                    }
                )
                EditorControls(
                    breakpoint = breakpoint,
                    editorVisibility = uiState.editorVisibility,
                    onEditorVisibilityChange = {
                        uiState = uiState.copy(editorVisibility = !uiState.editorVisibility)
                    },
                    onLinkClick = {
                        uiState = uiState.copy(linkPopup = true)
                    },
                    onImageClick = {
                        uiState = uiState.copy(imagePopup = true)
                    },
                )
                Editor(editorVisibility = uiState.editorVisibility)

                CreateButton(
                    text = uiState.buttonText,
                    onClick = {
                        uiState = uiState.copy(
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
                        println("thumbnailInputEnabled = ${uiState.thumbnailInputEnabled}")

                        if (!uiState.thumbnailInputEnabled) {
                            uiState = uiState.copy(
                                thumbnail = (document
                                    .getElementById(Id.thumbnailInput) as HTMLInputElement)
                                    .value,
                            )
                        }


                        if (uiState.title.isNotBlank() &&
                            uiState.subtitle.isNotBlank() &&
                            uiState.thumbnail.isNotBlank() &&
                            uiState.content.isNotBlank()
                        ) {
                            scope.launch {
                                if (hasPostIdParam) {
                                    val result = updatePost(
                                        Post(
                                            id = uiState.id,
                                            title = uiState.title,
                                            subtitle = uiState.subtitle,
                                            thumbnail = uiState.thumbnail,
                                            content = uiState.content,
                                            category = uiState.category,
                                            popular = uiState.popular,
                                            main = uiState.main,
                                            sponsored = uiState.sponsored
                                        )
                                    )
                                    if (result) {
                                        context.router.navigateTo(Screen.AdminSuccess.postUpdated())
                                    }
                                } else {
                                    val result = addPost(
                                        Post(
                                            author = localStorage.getItem("username").toString(),
                                            date = Date.now().toLong(),
                                            title = uiState.title,
                                            subtitle = uiState.subtitle,
                                            thumbnail = uiState.thumbnail,
                                            content = uiState.content,
                                            category = uiState.category,
                                            popular = uiState.popular,
                                            main = uiState.main,
                                            sponsored = uiState.sponsored
                                        )
                                    )
                                    if (result) {
                                        context.router.navigateTo(Screen.AdminSuccess.route)
                                    }
                                }
                            }
                        } else {
                            scope.launch {
                                uiState = uiState.copy(
                                    messagePopup = true
                                )
                                delay(2000)
                                uiState = uiState.copy(
                                    messagePopup = false
                                )
                            }
                        }
                    }
                )
            }
        }
    }
    if (uiState.messagePopup) {
        MessagePopup(
            message = "Please fill all the fields",
            onDismiss = { uiState = uiState.copy(messagePopup = false) }
        )
    }
    if (uiState.linkPopup) {
        LinkPopup(
            onDismiss = { uiState = uiState.copy(linkPopup = false) },
            onAddClick = { href, title ->
                applyStyle(
                    ControlStyle.Link(
                        selectedText = getSelectedText(),
                        href = href,
                        title = title
                    )
                )
            },
            editorControl = EditorControl.Link,
        )
    }
    if (uiState.imagePopup) {
        LinkPopup(
            onDismiss = { uiState = uiState.copy(imagePopup = false) },
            onAddClick = { imageUrl, description ->
                applyStyle(
                    ControlStyle.Image(
                        selectedText = getSelectedText(),
                        imageUrl = imageUrl,
                        desc = description,
                    )
                )
            },
            editorControl = EditorControl.Image,
        )
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
    onEditorVisibilityChange: () -> Unit,
    onLinkClick: () -> Unit,
    onImageClick: () -> Unit,
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
                EditorControl.entries.forEach {
                    EditorControlView(
                        control = it,
                        onClick = {
                            applyControlStyle(
                                editorControl = it,
                                onLinkClick = onLinkClick,
                                onImageClick = onImageClick,
                            )
                        }
                    )
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
                        document.getElementById(Id.editorPreview)?.innerHTML = getEditor().value
                        js("hljs.highlightAll()") as Unit
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
fun EditorControlView(
    control: EditorControl,
    onClick: () -> Unit,
) {
    Box(
        modifier = EditorKeyStyle.toModifier()
            .fillMaxHeight()
            .padding(leftRight = 12.px)
            .borderRadius(r = 4.px)
            .cursor(Cursor.Pointer)
            .onClick { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            src = control.icon,
            description = control.name,
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
                .onKeyDown {
                    if (it.code == "Enter" && it.shiftKey) {
                        applyStyle(
                            ControlStyle.Break(
                                selectedText = getSelectedText(),
                            )
                        )
                    }
                }
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
                .color(Color.black)
                .borderRadius(r = 4.px)
                .noBorder()
                .visibility(if (editorVisibility) Visibility.Hidden else Visibility.Visible)
                .overflow(Overflow.Auto)
                .scrollBehavior(ScrollBehavior.Smooth)
                .toAttrs()
        )
    }
}

@Composable
fun CreateButton(
    text: String,
    onClick: () -> Unit
) {
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
        SpanText(text = text)
    }
}
