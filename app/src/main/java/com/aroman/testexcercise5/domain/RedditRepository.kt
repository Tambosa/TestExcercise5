package com.aroman.testexcercise5.domain

import retrofit2.Call

interface RedditRepository {
    fun getPopularMovies(
        limit: Int,
        after: String?,
        count: Int?,
    ): Call<PagedResponse>
}