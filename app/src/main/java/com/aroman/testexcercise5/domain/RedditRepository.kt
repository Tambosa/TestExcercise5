package com.aroman.testexcercise5.domain

import com.aroman.testexcercise5.domain.entities.PageKey
import com.aroman.testexcercise5.domain.entities.PagedResponse
import io.reactivex.Observable

interface RedditRepository {
    fun getPopularMovies(key: PageKey): Observable<PagedResponse>
}