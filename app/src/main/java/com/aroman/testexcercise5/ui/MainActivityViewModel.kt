package com.aroman.testexcercise5.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aroman.testexcercise5.domain.LocalRedditRepository
import com.aroman.testexcercise5.domain.RedditRepository
import com.aroman.testexcercise5.domain.entities.PageKey
import com.aroman.testexcercise5.domain.entities.PagedResponse
import com.aroman.testexcercise5.domain.entities.RedditData
import com.aroman.testexcercise5.domain.entities.RedditPost
import com.aroman.testexcercise5.utils.applySchedulers
import io.reactivex.disposables.CompositeDisposable

class MainActivityViewModel(
    private val remoteRepo: RedditRepository,
    private val localRepo: LocalRedditRepository
) : ViewModel() {
    private val _liveData: MutableLiveData<PagedResponse> = MutableLiveData()
    val pageList: LiveData<PagedResponse> = _liveData

    private val compositeDisposable = CompositeDisposable()

    //region local repo

    fun getLocalPage() {
        localRepo.getAll()
            .applySchedulers()
            .subscribe({ result ->
                _liveData.postValue(PagedResponse(RedditData("", result, "")))
            }, {})
            .let { compositeDisposable.add(it) }
    }

    fun savePost(post: RedditPost) {
        localRepo.savePost(post)
            .applySchedulers()
            .subscribe({}, {})
            .let { compositeDisposable.add(it) }
    }

    fun deletePost(post: RedditPost) {
        localRepo.deletePost(post)
            .applySchedulers()
            .subscribe({
            }, {})
            .let { compositeDisposable.add(it) }
    }

    //endregion
    //region remote repo

    fun getPage(pageKey: PageKey) {
        remoteRepo.getPopularMovies(pageKey)
            .applySchedulers()
            .subscribe({ response ->
                setIsSavedValue(response)
                filterVideos(response)
                _liveData.postValue(response)
            }, {})
            .let { compositeDisposable.add(it) }
    }

    private fun setIsSavedValue(response: PagedResponse) {
        response.data.children.forEach { redditPost ->
            localRepo.checkIfSaved(redditPost)
                .applySchedulers()
                .subscribe({
                    redditPost.isSaved = it
                }, {})
                .let { compositeDisposable.add(it) }
        }
    }

    private fun filterVideos(response: PagedResponse) {
        val childrenWithoutVideos = mutableListOf<RedditPost>()
        response.data.children.forEach { redditPost ->
            if (!redditPost.data.is_video) childrenWithoutVideos.add(redditPost)
        }
        response.data.children = childrenWithoutVideos
    }

    //endregion

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }
}