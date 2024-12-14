package com.example.blogmultiplatform.pages.admin

import androidx.compose.runtime.Composable
import com.example.blogmultiplatform.components.AdminPageLayout
import com.example.blogmultiplatform.utils.IsUserLoggedIn
import com.varabyte.kobweb.core.Page

@Page
@Composable
fun CreatePage() {
    IsUserLoggedIn {
        CreateScreen()
    }
}

@Composable
fun CreateScreen() {
    AdminPageLayout {

    }
}
