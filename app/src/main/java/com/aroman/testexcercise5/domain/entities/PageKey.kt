package com.aroman.testexcercise5.domain.entities

data class PageKey(
    val limit: Int,
    val after: String?,
    val count: Int?
)