package com.aroman.testexcercise5.domain

import com.aroman.testexcercise5.domain.entities.PageKey
import com.aroman.testexcercise5.domain.entities.PagedResponse
import retrofit2.Call

interface RedditRepository {
    fun getPopularMovies(key: PageKey): Call<PagedResponse>
}