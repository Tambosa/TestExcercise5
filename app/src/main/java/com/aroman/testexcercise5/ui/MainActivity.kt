package com.aroman.testexcercise5.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.aroman.testexcercise5.data.RedditFeedApi
import com.aroman.testexcercise5.data.RetrofitClient
import com.aroman.testexcercise5.data.RetrofitRedditRepositoryImpl
import com.aroman.testexcercise5.databinding.ActivityMainBinding
import com.aroman.testexcercise5.domain.PageKey


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel = MainActivityViewModel(
        RetrofitRedditRepositoryImpl(
            RetrofitClient().provideRetrofit().create(RedditFeedApi::class.java)
        )
    )
    private val redditPostsAdapter = RedditPostsAdapter { position ->
        onItemClick(position)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.redditRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.redditRecyclerView.adapter = redditPostsAdapter

        viewModel.pageList.observe(this) {
            Log.d("@@@", it.data.children.toString())
            redditPostsAdapter.addData(it.data.children)
        }
        viewModel.getPage(PageKey(10, null, 10))
    }

    private fun onItemClick(position: Int) {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.reddit.com" + redditPostsAdapter.getData()[position].data.url)
        )
        startActivity(browserIntent)
    }
}