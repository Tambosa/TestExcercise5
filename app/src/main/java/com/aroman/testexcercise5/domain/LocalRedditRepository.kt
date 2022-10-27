package com.aroman.testexcercise5.domain

interface LocalRedditRepository {
    suspend fun savePost(post: RedditPost)
    suspend fun deletePost(post: RedditPost)
    suspend fun checkIfSaved(post: RedditPost): Boolean
    suspend fun getAll(): List<RedditPost>
}