package com.aroman.testexcercise5.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.aroman.testexcercise5.databinding.ItemRedditPostBinding
import com.aroman.testexcercise5.domain.RedditPost

class RedditPostsAdapter(private val onItemClick: (position: Int) -> Unit) :
    RecyclerView.Adapter<RedditPostsAdapter.RedditPostViewHolder>() {

    private val data = mutableListOf<RedditPost>()

    fun addData(newData: List<RedditPost>) {
        data.addAll(newData)
        notifyDataSetChanged()
    }

    fun getData() = data

    override fun onBindViewHolder(holder: RedditPostViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener { onItemClick(position) }
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RedditPostViewHolder(
            ItemRedditPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    inner class RedditPostViewHolder(private val binding: ItemRedditPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RedditPost) = with(binding) {
            title.text = item.data.title
            selftext.text = item.data.selftext
            val imageUrl = try {
                item.data.preview.listImage[0].source.url.replace("amp;", "")
            } catch (e: Exception) {
                Log.d("@@@", e.message.toString())
                item.data.thumbnail
            }
            postImage.load(imageUrl)
            author.text = "Posted by u/" + item.data.author
            ups.text = item.data.ups.toString()
            subreddit.text = "r/" + item.data.subreddit
            numberOfComments.text = item.data.comments.toString()
        }
    }
}