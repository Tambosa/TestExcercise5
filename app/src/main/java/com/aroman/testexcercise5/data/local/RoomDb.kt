package com.aroman.testexcercise5.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = arrayOf(RedditPostEntity::class), version = 1, exportSchema = false
)
abstract class RoomDb : RoomDatabase() {
    abstract fun RedditPostDao(): RedditPostDao
}