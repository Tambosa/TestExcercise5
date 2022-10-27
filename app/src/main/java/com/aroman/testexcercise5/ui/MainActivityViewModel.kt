package com.aroman.testexcercise5.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aroman.testexcercise5.domain.*
import com.aroman.testexcercise5.domain.entities.PageKey
import com.aroman.testexcercise5.domain.entities.PagedResponse
import com.aroman.testexcercise5.domain.entities.RedditData
import com.aroman.testexcercise5.domain.entities.RedditPost
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityViewModel(
    private val repo: RedditRepository,
    private val localRepo: LocalRedditRepository
) : ViewModel() {
    private val _liveData: MutableLiveData<PagedResponse> = MutableLiveData()
    val pageList: LiveData<PagedResponse> = _liveData

    fun getLocalPage() {
        viewModelCoroutineScope.launch { susGetLocalPage() }
    }

    private suspend fun susGetLocalPage() {
        withContext(Dispatchers.IO) {
            _liveData.postValue(PagedResponse(RedditData("", localRepo.getAll(), "")))
        }
    }

    fun getPage(pageKey: PageKey) {
        viewModelCoroutineScope.launch { susGetPage(pageKey) }
    }

    private suspend fun susGetPage(pageKey: PageKey) {
        withContext(Dispatchers.IO) {
            repo.getPopularMovies(pageKey).enqueue(object : Callback<PagedResponse> {
                override fun onResponse(
                    call: Call<PagedResponse>,
                    response: Response<PagedResponse>
                ) {
                    if (response.body() != null) {
                        _liveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<PagedResponse>, t: Throwable) {
                    //nothing
                }
            })
        }
    }

    private val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.IO
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable ->
            handleError(throwable)
        }
    )

    private fun handleError(error: Throwable) {
        //nothing
    }

    override fun onCleared() {
        super.onCleared()
        viewModelCoroutineScope.coroutineContext.cancel()
    }

    fun checkIfSaved(post: RedditPost): Boolean {
        var isSaved = false
        runBlocking {
            isSaved = susCheckIfSaved(post)
        }
        return isSaved
    }

    private suspend fun susCheckIfSaved(post: RedditPost): Boolean {
        return withContext(Dispatchers.IO) {
            localRepo.checkIfSaved(post)
        }
    }

    fun savePost(post: RedditPost) {
        viewModelCoroutineScope.launch { susSavePost(post) }
    }

    private suspend fun susSavePost(post: RedditPost) {
        return withContext(Dispatchers.IO) {
            localRepo.savePost(post)
        }
    }

    fun deletePost(post: RedditPost) {
        viewModelCoroutineScope.launch { susDeletePost(post) }
    }

    private suspend fun susDeletePost(post: RedditPost) {
        return withContext(Dispatchers.IO) {
            localRepo.deletePost(post)
        }
    }
}