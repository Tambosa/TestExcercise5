package com.aroman.testexcercise5.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aroman.testexcercise5.databinding.ItemRedditPostBinding
import com.aroman.testexcercise5.domain.RedditPost

class RedditPostsAdapter() :
    RecyclerView.Adapter<RedditPostsAdapter.RedditPostViewHolder>() {

    private val data = mutableListOf<RedditPost>()

    fun addData(newData: List<RedditPost>) {
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RedditPostViewHolder, position: Int) {
        holder.bind(data[position])
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
//            postImage.text = item.data.thumbnail
            author.text = item.data.author
            ups.text = item.data.ups.toString()
            subreddit.text = item.data.subreddit
        }
    }
}