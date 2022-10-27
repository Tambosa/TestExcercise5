package com.aroman.testexcercise5.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = RoomConst.TABLE_REDDIT)
data class RedditPostEntity(
    @PrimaryKey val name: String,
    val isSaved: Boolean,
    val subreddit: String,
    val title: String,
    val author: String,
    val selfText: String,
    val ups: Int,
    val thumbnail: String,
    val url: String,
    val comments: Int,
    val image_url: String
)