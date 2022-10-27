package com.aroman.testexcercise5.data.local

import com.aroman.testexcercise5.domain.*

class RoomLocalRedditRepositoryImpl(private val dao: RedditPostDao) : LocalRedditRepository {
    override suspend fun savePost(post: RedditPost) {
        dao.insertNewPost(localToRoom(post))
    }

    override suspend fun deletePost(post: RedditPost) {
        dao.deletePost(localToRoom(post))
    }

    override suspend fun checkIfSaved(post: RedditPost) =
        dao.getPostByName(post.data.name).isNotEmpty()

    override suspend fun getAll(): List<RedditPost> {
        val tempList = dao.getAll()
        val returnList = mutableListOf<RedditPost>()
        for (redditPostEntity in tempList) {
            returnList.add(roomToLocal(redditPostEntity))
        }
        return returnList
    }

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

    private fun roomToLocal(room: RedditPostEntity): RedditPost {
        return RedditPost(
            isSaved = room.isSaved,
            data = RedditPostData(
                name = room.name,
                subreddit = room.subreddit,
                title = room.title,
                author = room.author,
                ups = room.ups,
                thumbnail = room.thumbnail,
                url = room.url,
                comments = room.comments,
                selftext = room.selfText,
                preview = ImagePreview(listOf(RedditImage(ImageDetails(room.image_url))))
            )
        )
    }
}