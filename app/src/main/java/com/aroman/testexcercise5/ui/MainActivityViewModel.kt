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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivityViewModel(
    private val remoteRepo: RedditRepository,
    private val localRepo: LocalRedditRepository
) : ViewModel() {
    private val _liveData: MutableLiveData<PagedResponse> = MutableLiveData()
    val pageList: LiveData<PagedResponse> = _liveData

    //region local repo

    private val compositeDisposable = CompositeDisposable()

    fun getLocalPage() {
        localRepo.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                _liveData.postValue(PagedResponse(RedditData("", result, "")))
            }, {}).let {
                compositeDisposable.add(it)
            }
    }

    fun savePost(post: RedditPost) {
        localRepo.savePost(post)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {}).let {
                compositeDisposable.add(it)
            }
    }

    fun deletePost(post: RedditPost) {
        localRepo.deletePost(post)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
            }, {}).let {
                compositeDisposable.add(it)
            }
    }

    //endregion
    //region remote repo

    fun getPage(pageKey: PageKey) {
        remoteRepo.getPopularMovies(pageKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                onRemoteRepoResponse(response)
                _liveData.postValue(response)
            }, {})
            .let {
                compositeDisposable.add(it)
            }
    }

    private fun onRemoteRepoResponse(response: PagedResponse) {
        response.data.children.forEach { redditPost ->
            localRepo.checkIfSaved(redditPost)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    redditPost.isSaved = it
                }, {}).let {
                    compositeDisposable.add(it)
                }
        }
    }

    //endregion

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }
}