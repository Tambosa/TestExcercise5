package com.aroman.testexcercise5.data

import com.aroman.testexcercise5.domain.PagedResponse
import com.aroman.testexcercise5.domain.RedditRepository
import retrofit2.Call

class RetrofitRedditRepositoryImpl(private val api: RedditFeedApi) : RedditRepository {
    override fun getPopularMovies(
        limit: Int,
        after: String?,
        count: Int?
    ): Call<PagedResponse> {
        return api.getRedditPopularFeed(limit, after, count)
    }
}