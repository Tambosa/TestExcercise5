package com.aroman.testexcercise5.data.local

import androidx.room.*

@Dao
interface RedditPostDao {

    @Query("SELECT * FROM ${RoomConst.TABLE_REDDIT}")
    fun getAll(): List<RedditPostEntity>

    @Query("SELECT * FROM ${RoomConst.TABLE_REDDIT} WHERE name LIKE :name")
    fun getPostByName(name: String): List<RedditPostEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewPost(post: RedditPostEntity)

    @Delete
    fun deletePost(post: RedditPostEntity)
}