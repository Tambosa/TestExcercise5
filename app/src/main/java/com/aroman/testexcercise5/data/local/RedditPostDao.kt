package com.aroman.testexcercise5.data.local

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface RedditPostDao {

    @Query("SELECT * FROM ${RoomConst.TABLE_REDDIT}")
    fun getAll(): Single<List<RedditPostEntity>>

    @Query("SELECT * FROM ${RoomConst.TABLE_REDDIT} WHERE name LIKE :name")
    fun getPostByName(name: String): Single<List<RedditPostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewPost(post: RedditPostEntity): Completable

    @Delete
    fun deletePost(post: RedditPostEntity): Completable
}