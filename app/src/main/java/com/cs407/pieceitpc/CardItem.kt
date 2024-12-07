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

/**
fun getSampleBuilds(): List<CardItem> {
    return listOf(
        cards(1, R.drawable.placeholder, "Gaming Beast", "This build is optimized for 4K gaming.", "User123"),
       cards(2, R.drawable.placeholder, "Budget Build", "A solid budget build for entry-level gaming.", "PCEnthusiast"),
        cards(3, R.drawable.placeholder, "Workstation", "Perfect for video editing and rendering tasks.", "ProBuilder"),
        cards(4, R.drawable.placeholder, "RGB Showoff", "Maximized RGB lighting for aesthetics.", "RGB_Fanatic")
    )
}
**/
/**


//fun getSampleYT(): List<CardItem> {
//    return listOf(
//        CardItem("1", R.drawable.placeholder, "Gaming Beast", "This build is optimized for 4K gaming.", "User123"),
//        CardItem("2", R.drawable.placeholder, "Budget Build", "A solid budget build for entry-level gaming.", "PCEnthusiast"),
//        CardItem("3", R.drawable.placeholder, "Workstation", "Perfect for video editing and rendering tasks.", "ProBuilder"),
//        CardItem("4", R.drawable.placeholder, "RGB Showoff", "Maximized RGB lighting for aesthetics.", "RGB_Fanatic")
//    )
//}
//
//fun getSampleOthers(): List<CardItem> {
//    return listOf(
//        CardItem("1", R.drawable.placeholder, "Gaming Beast", "This build is optimized for 4K gaming.", "User123"),
//        CardItem("2", R.drawable.placeholder, "Budget Build", "A solid budget build for entry-level gaming.", "PCEnthusiast"),
//        CardItem("3", R.drawable.placeholder, "Workstation", "Perfect for video editing and rendering tasks.", "ProBuilder"),
//        CardItem("4", R.drawable.placeholder, "RGB Showoff", "Maximized RGB lighting for aesthetics.", "RGB_Fanatic")
//    )
//}


fun getSampleYT(): List<CardItem> {
    return listOf(
        CardItem(1, R.drawable.placeholder, "Cool PC Video", "This build is optimized for 4K gaming.", "User123"),
        CardItem(2, R.drawable.placeholder, "Cool PC Video", "A solid budget build for entry-level gaming.", "PCEnthusiast"),
        CardItem(3, R.drawable.placeholder, "Cool PC Video", "Perfect for video editing and rendering tasks.", "ProBuilder"),
        CardItem(4, R.drawable.placeholder, "Decent PC Video", "Maximized RGB lighting for aesthetics.", "RGB_Fanatic")
    )
}

fun getSampleOthers(): List<CardItem> {
    return listOf(
        CardItem(1, R.drawable.placeholder, "Other User 1", "This build is optimized for 4K gaming.", "User123"),
        CardItem(2, R.drawable.placeholder, "Other User 2", "A solid budget build for entry-level gaming.", "PCEnthusiast"),
        CardItem(3, R.drawable.placeholder, "Other User 3", "Perfect for video editing and rendering tasks.", "ProBuilder"),
        CardItem(4, R.drawable.placeholder, "Other User 4", "Maximized RGB lighting for aesthetics.", "RGB_Fanatic")
    )
}


/**
 * fun getSampleYT(): List<CardItem> {
 *     return listOf(
 *         CardItem("1", R.drawable.placeholder, "Gaming Beast", "This build is optimized for 4K gaming.", "User123"),
 *         CardItem("2", R.drawable.placeholder, "Budget Build", "A solid budget build for entry-level gaming.", "PCEnthusiast"),
 *         CardItem("3", R.drawable.placeholder, "Workstation", "Perfect for video editing and rendering tasks.", "ProBuilder"),
 *         CardItem("4", R.drawable.placeholder, "RGB Showoff", "Maximized RGB lighting for aesthetics.", "RGB_Fanatic")
 *     )
 * }
 *
 * fun getSampleOthers(): List<CardItem> {
 *     return listOf(
 *         CardItem("1", R.drawable.placeholder, "Gaming Beast", "This build is optimized for 4K gaming.", "User123"),
 *         CardItem("2", R.drawable.placeholder, "Budget Build", "A solid budget build for entry-level gaming.", "PCEnthusiast"),
 *         CardItem("3", R.drawable.placeholder, "Workstation", "Perfect for video editing and rendering tasks.", "ProBuilder"),
 *         CardItem("4", R.drawable.placeholder, "RGB Showoff", "Maximized RGB lighting for aesthetics.", "RGB_Fanatic")
 *     )
 * }
 */