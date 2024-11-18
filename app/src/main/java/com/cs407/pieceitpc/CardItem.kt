package com.cs407.pieceitpc

data class CardItem(
    val id: Int,
    val imageResId: Int,
    val title: String,
    val description: String,
    val author: String
)

// Sample data function
fun getSampleBuilds(): List<CardItem> {
    return listOf(
        CardItem(1, R.drawable.placeholder, "Gaming Beast", "This build is optimized for 4K gaming.", "User123"),
        CardItem(2, R.drawable.placeholder, "Budget Build", "A solid budget build for entry-level gaming.", "PCEnthusiast"),
        CardItem(3, R.drawable.placeholder, "Workstation", "Perfect for video editing and rendering tasks.", "ProBuilder"),
        CardItem(4, R.drawable.placeholder, "RGB Showoff", "Maximized RGB lighting for aesthetics.", "RGB_Fanatic")
    )
}

