package com.aroman.testexcercise5.data.local

import com.aroman.testexcercise5.domain.LocalRedditRepository
import com.aroman.testexcercise5.domain.entities.*

class RoomLocalRedditRepositoryImpl(private val dao: RedditPostDao) : LocalRedditRepository {

    override fun getAll() = dao.getAll().map { roomToLocal(it) }

    override fun savePost(post: RedditPost) = dao.insertNewPost(localToRoom(post))

    override fun deletePost(post: RedditPost) = dao.deletePost(localToRoom(post))

    override fun checkIfSaved(post: RedditPost) =
        dao.getPostByName(post.data.name).map { it.isNotEmpty() }

    private fun localToRoom(local: RedditPost): RedditPostEntity {
        return RedditPostEntity(
            name = local.data.name,
            isSaved = local.isSaved,
            subreddit = local.data.subreddit,
            title = local.data.title,
            author = local.data.author,
            selfText = local.data.selftext,
            ups = local.data.ups,
            thumbnail = local.data.thumbnail,
            url = local.data.url,
            comments = local.data.comments,
            image_url = try {
                local.data.preview.listImage[0].source.url
            } catch (e: Exception) {
                ""
            }
        )
    }

    private fun roomToLocal(roomList: List<RedditPostEntity>): List<RedditPost> {
        val returnList = mutableListOf<RedditPost>()
        for (redditPost in roomList) {
            returnList.add(
                RedditPost(
                    isSaved = redditPost.isSaved,
                    data = RedditPostData(
                        name = redditPost.name,
                        subreddit = redditPost.subreddit,
                        title = redditPost.title,
                        author = redditPost.author,
                        ups = redditPost.ups,
                        thumbnail = redditPost.thumbnail,
                        url = redditPost.url,
                        comments = redditPost.comments,
                        selftext = redditPost.selfText,
                        preview = ImagePreview(listOf(RedditImage(ImageDetails(redditPost.image_url)))),
                        is_video = false
                    )
                )
            )
        }
        return returnList
    }
}