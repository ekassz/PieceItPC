package com.cs407.pieceitpc

data class CardItem(
    val id: String,
    val imageResId: String,
    val title: String,
    val description: String,
    val author: String
)

<<<<<<< HEAD
=======
// Sample data function
//fun getSampleBuilds(): List<CardItem> {
//    return listOf(
//        CardItem(1, R.drawable.placeholder, "Gaming Beast", "This build is optimized for 4K gaming.", "User123"),
//        CardItem(2, R.drawable.placeholder, "Budget Build", "A solid budget build for entry-level gaming.", "PCEnthusiast"),
//        CardItem(3, R.drawable.placeholder, "Workstation", "Perfect for video editing and rendering tasks.", "ProBuilder"),
//        CardItem(4, R.drawable.placeholder, "RGB Showoff", "Maximized RGB lighting for aesthetics.", "RGB_Fanatic")
//    )
//}

fun getSampleYT(): List<CardItem> {
    return listOf(
        CardItem("1", R.drawable.placeholder, "Gaming Beast", "This build is optimized for 4K gaming.", "User123"),
        CardItem("2", R.drawable.placeholder, "Budget Build", "A solid budget build for entry-level gaming.", "PCEnthusiast"),
        CardItem("3", R.drawable.placeholder, "Workstation", "Perfect for video editing and rendering tasks.", "ProBuilder"),
        CardItem("4", R.drawable.placeholder, "RGB Showoff", "Maximized RGB lighting for aesthetics.", "RGB_Fanatic")
    )
}

fun getSampleOthers(): List<CardItem> {
    return listOf(
        CardItem("1", R.drawable.placeholder, "Gaming Beast", "This build is optimized for 4K gaming.", "User123"),
        CardItem("2", R.drawable.placeholder, "Budget Build", "A solid budget build for entry-level gaming.", "PCEnthusiast"),
        CardItem("3", R.drawable.placeholder, "Workstation", "Perfect for video editing and rendering tasks.", "ProBuilder"),
        CardItem("4", R.drawable.placeholder, "RGB Showoff", "Maximized RGB lighting for aesthetics.", "RGB_Fanatic")
    )
}

>>>>>>> b84131f001e55d6efb216fb61d7227dfef5e5caa
