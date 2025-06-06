package com.example.blogmultiplatform

object Constants {
    const val FONT_FAMILY = "Roboto"
    const val SIDE_PANEL_WIDTH = 250
    const val PAGE_WIDTH = 1920
    const val HEADER_HEIGHT = 100
    const val COLLAPSED_PANEL_HEIGHT = 100
    const val HUMOR_API_URL =
        "https://api.humorapi.com/jokes/random?api-key=4a64d9015fe841d1b5ca8ec179ee5639&max-length=180"
    const val MAIN_POSTS_LIMIT = 4
    const val DATABASE_NAME = "my_blog"
    const val DATABASE_NAME_REMOTE = "MyBlog"

    const val POSTS_PER_PAGE = 8
    const val QUERY_PARAM = "query"
    const val CATEGORY_PARAM = "category"
    const val POST_ID_PARAM = "postId"
    const val SKIP_PARAM = "skip"
    const val AUTHOR_PARAM = "author"
    const val UPDATE_PARAM = "updated"
}

object Res {
    object Image {
        const val LOGO = "/logo.svg"
        const val LOGO_HOME = "logo.svg"
        const val LAUGH = "/laugh.png"
    }
    object Icon {
        const val bold = "/bold.svg"
        const val italic = "/italic.svg"
        const val link = "/link.svg"
        const val title = "/title.svg"
        const val subtitle = "/subtitle.svg"
        const val quote = "/quote.svg"
        const val code = "/code.svg"
        const val image = "/image.svg"
        const val checkmark = "/checkmark.svg"
    }
    object PathIcon {
        const val home =
            "M1 10.5215L3 8.52148M3 8.52148L10 1.52148L17 8.52148M3 8.52148V18.5215C3 18.7867 3.10536 19.0411 3.29289 19.2286C3.48043 19.4161 3.73478 19.5215 4 19.5215H7M17 8.52148L19 10.5215M17 8.52148V18.5215C17 18.7867 16.8946 19.0411 16.7071 19.2286C16.5196 19.4161 16.2652 19.5215 16 19.5215H13M7 19.5215C7.26522 19.5215 7.51957 19.4161 7.70711 19.2286C7.89464 19.0411 8 18.7867 8 18.5215V14.5215C8 14.2563 8.10536 14.0019 8.29289 13.8144C8.48043 13.6268 8.73478 13.5215 9 13.5215H11C11.2652 13.5215 11.5196 13.6268 11.7071 13.8144C11.8946 14.0019 12 14.2563 12 14.5215V18.5215C12 18.7867 12.1054 19.0411 12.2929 19.2286C12.4804 19.4161 12.7348 19.5215 13 19.5215M7 19.5215H13"
        const val create =
            "M10 7.52148V10.5215M10 10.5215V13.5215M10 10.5215H13M10 10.5215H7M19 10.5215C19 11.7034 18.7672 12.8737 18.3149 13.9656C17.8626 15.0576 17.1997 16.0497 16.364 16.8854C15.5282 17.7212 14.5361 18.3841 13.4442 18.8364C12.3522 19.2887 11.1819 19.5215 10 19.5215C8.8181 19.5215 7.64778 19.2887 6.55585 18.8364C5.46392 18.3841 4.47177 17.7212 3.63604 16.8854C2.80031 16.0497 2.13738 15.0576 1.68508 13.9656C1.23279 12.8737 1 11.7034 1 10.5215C1 8.13454 1.94821 5.84535 3.63604 4.15752C5.32387 2.4697 7.61305 1.52148 10 1.52148C12.3869 1.52148 14.6761 2.4697 16.364 4.15752C18.0518 5.84535 19 8.13454 19 10.5215Z"
        const val posts =
            "M5 3.52148H3C2.46957 3.52148 1.96086 3.7322 1.58579 4.10727C1.21071 4.48234 1 4.99105 1 5.52148V17.5215C1 18.0519 1.21071 18.5606 1.58579 18.9357C1.96086 19.3108 2.46957 19.5215 3 19.5215H13C13.5304 19.5215 14.0391 19.3108 14.4142 18.9357C14.7893 18.5606 15 18.0519 15 17.5215V5.52148C15 4.99105 14.7893 4.48234 14.4142 4.10727C14.0391 3.7322 13.5304 3.52148 13 3.52148H11M5 3.52148C5 4.05192 5.21071 4.56063 5.58579 4.9357C5.96086 5.31077 6.46957 5.52148 7 5.52148H9C9.53043 5.52148 10.0391 5.31077 10.4142 4.9357C10.7893 4.56063 11 4.05192 11 3.52148M5 3.52148C5 2.99105 5.21071 2.48234 5.58579 2.10727C5.96086 1.7322 6.46957 1.52148 7 1.52148H9C9.53043 1.52148 10.0391 1.7322 10.4142 2.10727C10.7893 2.48234 11 2.99105 11 3.52148M8 10.5215H11M8 14.5215H11M5 10.5215H5.01M5 14.5215H5.01"
        const val logout =
            "M9 13.5215L5 9.52148M5 9.52148L9 5.52148M5 9.52148H19M14 13.5215V14.5215C14 15.3171 13.6839 16.0802 13.1213 16.6428C12.5587 17.2054 11.7956 17.5215 11 17.5215H4C3.20435 17.5215 2.44129 17.2054 1.87868 16.6428C1.31607 16.0802 1 15.3171 1 14.5215V4.52148C1 3.72583 1.31607 2.96277 1.87868 2.40016C2.44129 1.83755 3.20435 1.52148 4 1.52148H11C11.7956 1.52148 12.5587 1.83755 13.1213 2.40016C13.6839 2.96277 14 3.72583 14 4.52148V5.52148"
    }
}

object Id{
    const val usernameInput = "usernameInput"
    const val passwordInput = "passwordInput"
    const val svgParent = "svgParent"
    const val vectorIcon = "vectorIcon"
    const val navigationText = "navigationText"
    const val editor = "editor"
    const val editorPreview = "editorPreview"
    const val titleInput = "titleInput"
    const val subtitleInput = "subtitleInput"
    const val thumbnailInput = "thumbnailInput"
    const val linkHrefInput = "linkHrefInput"
    const val linkTitleInput = "linkTitleInput"
    const val adminSearchBar = "adminSearchBar"
    const val emailInput = "emailInput"
    const val postContent = "postContent"
}
