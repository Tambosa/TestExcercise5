package com.aroman.testexcercise5.data

import com.aroman.testexcercise5.domain.PagedResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditFeedApi {
    @GET("r/funny/hot.json")
    fun getRedditPopularFeed(
        @Query("limit") limit: Int,
        @Query("after") after: String?,
        @Query("count") count: Int?,
    ): Call<PagedResponse>
}