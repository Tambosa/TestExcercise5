package com.aroman.testexcercise5.data.remote

import com.aroman.testexcercise5.domain.entities.PagedResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditFeedApi {
    @GET("r/funny/hot.json")
    fun getRedditPopularFeed(
        @Query("limit") limit: Int,
        @Query("after") after: String?,
        @Query("count") count: Int?,
    ): Observable<PagedResponse>
}