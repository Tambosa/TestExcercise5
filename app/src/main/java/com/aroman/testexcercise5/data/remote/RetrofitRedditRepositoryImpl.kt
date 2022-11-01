package com.aroman.testexcercise5.data.remote

import com.aroman.testexcercise5.domain.entities.PageKey
import com.aroman.testexcercise5.domain.entities.PagedResponse
import com.aroman.testexcercise5.domain.RedditRepository
import io.reactivex.Observable
import retrofit2.Call

class RetrofitRedditRepositoryImpl(private val api: RedditFeedApi) : RedditRepository {
    override fun getPopularMovies(
        key: PageKey
    ): Observable<PagedResponse> {
        return api.getRedditPopularFeed(
            limit = key.limit, after = key.after, count = key.count
        )
    }
}