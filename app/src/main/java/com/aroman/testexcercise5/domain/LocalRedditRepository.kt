package com.aroman.testexcercise5.domain

import com.aroman.testexcercise5.domain.entities.RedditPost

interface LocalRedditRepository {
    suspend fun savePost(post: RedditPost)
    suspend fun deletePost(post: RedditPost)
    suspend fun checkIfSaved(post: RedditPost): Boolean
    suspend fun getAll(): List<RedditPost>
}