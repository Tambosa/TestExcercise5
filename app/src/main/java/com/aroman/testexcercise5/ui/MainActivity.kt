package com.aroman.testexcercise5.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aroman.testexcercise5.databinding.ActivityMainBinding
import com.aroman.testexcercise5.domain.PageKey
import com.aroman.testexcercise5.domain.RedditPost
import com.aroman.testexcercise5.utils.leftSwipeHelper
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModel()

    private val redditPostsAdapter = RedditPostsAdapter({ position ->
        onItemClick(position)
    }, { position ->
        onSaveButtonClick(position)
    })
    private var isLoading = false
    private var after: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        initViewModel()
        initOnOffButtons()
    }

    private fun initViewModel() {
        viewModel.pageList.observe(this) {
            Log.d("@@@", it.data.children.toString())
            val redditPostsList = it.data.children
            for (post in redditPostsList) {
                post.isSaved = checkIfSaved(post)
            }
            redditPostsAdapter.addData(it.data.children)
            after = it.data.after
            isLoading = false
        }
        viewModel.getPage(getStartKey())
    }

    private fun checkIfSaved(post: RedditPost) = viewModel.checkIfSaved(post)

    private fun initRecyclerView() {
        binding.redditRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.redditRecyclerView.adapter = redditPostsAdapter
        binding.redditRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = binding.redditRecyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= TOTAL_PAGES) {
                        isLoading = true
                        loadNextPage()
                    }
                }
            }
        })
    }

    private fun loadNextPage() {
        val nextKey = getNextKey()
        viewModel.getPage(nextKey)
    }

    private fun getStartKey() = PageKey(10, null, 10)

    private fun getNextKey() = PageKey(10, after, 10)

    private fun onItemClick(position: Int) {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.reddit.com" + redditPostsAdapter.getData()[position].data.url)
        )
        startActivity(browserIntent)
    }

    private fun onSaveButtonClick(position: Int) {
        redditPostsAdapter.getData()[position].isSaved = true
        redditPostsAdapter.notifyItemChanged(position)
        viewModel.savePost(redditPostsAdapter.getData()[position])
    }

    private fun initOnOffButtons() {
        binding.buttonLocal.setOnClickListener {
            Log.d("@@@", "buttonLocal: ")
            redditPostsAdapter.clearData()
            viewModel.getLocalPage()
            it.isEnabled = false
            it.visibility = View.INVISIBLE
            binding.buttonRemote.isEnabled = true
            binding.buttonRemote.visibility = View.VISIBLE

            binding.redditRecyclerView.leftSwipeHelper { viewHolder ->
                viewModel.deletePost(redditPostsAdapter.getData()[viewHolder.adapterPosition])
                redditPostsAdapter.removeItemFromData(viewHolder.adapterPosition)
                redditPostsAdapter.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }

        binding.buttonRemote.setOnClickListener {
            Log.d("@@@", "buttonRemote: ")
            redditPostsAdapter.clearData()
            viewModel.getPage(getStartKey())
            it.isEnabled = false
            it.visibility = View.INVISIBLE
            binding.buttonLocal.isEnabled = true
            binding.buttonLocal.visibility = View.VISIBLE
            binding.redditRecyclerView.leftSwipeHelper {}
        }
    }

    companion object {
        const val TOTAL_PAGES = 5
    }
}