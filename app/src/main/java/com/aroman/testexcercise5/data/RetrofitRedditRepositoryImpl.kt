package com.aroman.testexcercise5.data

import com.aroman.testexcercise5.domain.PageKey
import com.aroman.testexcercise5.domain.PagedResponse
import com.aroman.testexcercise5.domain.RedditRepository
import retrofit2.Call

class RetrofitRedditRepositoryImpl(private val api: RedditFeedApi) : RedditRepository {
    override fun getPopularMovies(
        key: PageKey
    ): Call<PagedResponse> {
        return api.getRedditPopularFeed(
            limit = key.limit, after = key.after, count = key.count
        )
    }
}