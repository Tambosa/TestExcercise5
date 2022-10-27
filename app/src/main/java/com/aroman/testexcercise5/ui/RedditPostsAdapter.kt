package com.aroman.testexcercise5.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.aroman.testexcercise5.databinding.ItemRedditPostBinding
import com.aroman.testexcercise5.domain.entities.RedditPost

class RedditPostsAdapter(
    private val onItemClick: (position: Int) -> Unit,
    private val onSaveButtonClick: (position: Int) -> Unit
) :
    RecyclerView.Adapter<RedditPostsAdapter.RedditPostViewHolder>() {

    private val data = mutableListOf<RedditPost>()

    fun addData(newData: List<RedditPost>) {
        data.addAll(newData)
        notifyDataSetChanged()
    }

    fun getData() = data

    fun clearData() = data.clear()

    fun removeItemFromData(position: Int) = data.removeAt(position)

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
            ),
            onSaveButtonClick
        )

    inner class RedditPostViewHolder(
        private val binding: ItemRedditPostBinding,
        private val onSaveButtonClick: (position: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RedditPost) = with(binding) {
            setTextView(title, item.data.title)
            setTextView(selftext, item.data.selftext)
            setTextView(author, "Posted by u/" + item.data.author)
            setTextView(ups, item.data.ups.toString())
            setTextView(subreddit, "r/" + item.data.subreddit)
            setTextView(numberOfComments, item.data.comments.toString())

            val imageUrl = try {
                item.data.preview.listImage[0].source.url.replace("amp;", "")
            } catch (e: Exception) {
                Log.d("@@@", e.message.toString())
                item.data.thumbnail
            }
            postImage.load(imageUrl)

            if (item.isSaved) {
                buttonSave.text = "saved"
                buttonSave.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF5700"))
            } else {
                buttonSave.text = "save"
                buttonSave.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#A5A4A4"))
                buttonSave.setOnClickListener { onSaveButtonClick(adapterPosition) }
            }
        }

        private fun setTextView(textView: TextView, text: String) {
            if (text.isEmpty()) textView.visibility = View.GONE
            else textView.text = text
        }
    }
}