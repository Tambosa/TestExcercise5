package com.aroman.testexcercise5.domain

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PagedResponse(
    @SerializedName("data") val data: RedditData,
) : Parcelable

@Parcelize
data class RedditData(
    @SerializedName("after") val after: String,
    @SerializedName("children") val children: List<RedditPost>,
    @SerializedName("before") val before: String,
) : Parcelable

@Parcelize
data class RedditPost(
    @SerializedName("data") val data: RedditPostData,
) : Parcelable

@Parcelize
data class RedditPostData(
    @SerializedName("subreddit") val subreddit: String,
    @SerializedName("name") val name: String,
    @SerializedName("title") val title: String,
    @SerializedName("ups") val ups: Int,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("permalink") val url: String,
    @SerializedName("num_comments") val comments: Int,
) : Parcelable
