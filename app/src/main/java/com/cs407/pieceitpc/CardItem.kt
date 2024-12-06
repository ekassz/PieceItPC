package com.cs407.pieceitpc

import android.media.ThumbnailUtils

data class CardItem(
    val id: String,
    val imageResId: String,
    val title: String,
    val description: String,
    val author: String
)


data class video(
    val id: Int,
    val title: String,
    val author: String,
    val thumbnailUtils: ThumbnailUtils,
    val link: String
)