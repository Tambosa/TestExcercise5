package com.aroman.testexcercise5.domain

data class PageKey(
    val limit: Int,
    val after: String?,
    val count: Int?
)