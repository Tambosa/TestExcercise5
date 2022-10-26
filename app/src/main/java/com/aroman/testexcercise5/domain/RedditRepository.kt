package com.aroman.testexcercise5.domain

import retrofit2.Call

interface RedditRepository {
    fun getPopularMovies(key: PageKey): Call<PagedResponse>
}