package com.aroman.testexcercise5.ui

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aroman.testexcercise5.R
import com.aroman.testexcercise5.databinding.ActivityMainBinding
import com.aroman.testexcercise5.domain.entities.PageKey
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModel()

    private val redditPostsAdapter = RedditPostsAdapter({ position ->
        onItemClick(position)
    }, { redditPostViewHolder ->
        onSaveButtonClick(redditPostViewHolder)
    })
    private var isLoading = false
    private var isOnline = true
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
            redditPostsAdapter.addData(it.data.children)
            after = it.data.after
            isLoading = false
        }
        viewModel.getPage(getStartKey())
    }

    private fun initRecyclerView() {
        binding.redditRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.redditRecyclerView.adapter = redditPostsAdapter
        binding.redditRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = binding.redditRecyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && isOnline) {
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

    private fun onSaveButtonClick(redditPostView: RedditPostsAdapter.RedditPostViewHolder): CompoundButton.OnCheckedChangeListener {
        var lastClickTime = 0L
        val debounceTime = 800L

        return CompoundButton.OnCheckedChangeListener { toggleButton, isChecked ->
            if (SystemClock.elapsedRealtime() - lastClickTime > debounceTime) {
                lastClickTime = 0L
                if (isChecked) {
                    redditPostsAdapter.getData()[redditPostView.adapterPosition].isSaved = true
                    viewModel.savePost(redditPostsAdapter.getData()[redditPostView.adapterPosition])
                    likeAnimation(toggleButton, R.color.text_color_light, R.color.reddit_active)
                } else {
                    redditPostsAdapter.getData()[redditPostView.adapterPosition].isSaved = false
                    viewModel.deletePost(redditPostsAdapter.getData()[redditPostView.adapterPosition])
                    likeAnimation(toggleButton, R.color.reddit_active, R.color.text_color_light)
                }
            }
            lastClickTime = SystemClock.elapsedRealtime()
        }
    }

    private fun likeAnimation(toggleButton: CompoundButton, from: Int, to: Int) {
        ValueAnimator().apply {
            setIntValues(getColor(from), getColor(to))
            setEvaluator(ArgbEvaluator())
            addUpdateListener {
                toggleButton.background.setTint(it.animatedValue as Int)
            }
            duration = 600
        }.start()
    }

    private fun initOnOffButtons() {
        binding.buttonLocal.setOnClickListener {
            isOnline = false
            redditPostsAdapter.clearData()
            viewModel.getLocalPage()
            it.isEnabled = false
            it.visibility = View.INVISIBLE
            binding.buttonRemote.isEnabled = true
            binding.buttonRemote.visibility = View.VISIBLE
        }

        binding.buttonRemote.setOnClickListener {
            isOnline = true
            redditPostsAdapter.clearData()
            viewModel.getPage(getStartKey())
            it.isEnabled = false
            it.visibility = View.INVISIBLE
            binding.buttonLocal.isEnabled = true
            binding.buttonLocal.visibility = View.VISIBLE
        }
    }

    companion object {
        const val TOTAL_PAGES = 5
    }
}