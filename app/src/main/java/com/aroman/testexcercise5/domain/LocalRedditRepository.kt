package com.aroman.testexcercise5.domain

import com.aroman.testexcercise5.domain.entities.RedditPost
import io.reactivex.Completable
import io.reactivex.Single

interface LocalRedditRepository {
    fun savePost(post: RedditPost): Completable
    fun deletePost(post: RedditPost): Completable
    fun checkIfSaved(post: RedditPost): Single<Boolean>
    fun getAll(): Single<List<RedditPost>>
}