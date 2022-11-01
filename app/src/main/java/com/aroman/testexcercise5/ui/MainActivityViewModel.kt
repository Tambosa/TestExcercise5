package com.aroman.testexcercise5.ui

import android.util.Log
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
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                result.forEach { redditPost ->
                    Log.v("ViewModel", "\n" + redditPost.data.name + " " + redditPost.data.title)
                }
                _liveData.postValue(PagedResponse(RedditData("", result, "")))
            }, {
                //nothing
            }).let {
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

    private val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.IO
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable ->
            handleError(throwable)
        }
    )

    fun getPage(pageKey: PageKey) {
        viewModelCoroutineScope.launch { susGetPage(pageKey) }
    }

    private suspend fun susGetPage(pageKey: PageKey) {
        withContext(Dispatchers.IO) {
            remoteRepo.getPopularMovies(pageKey).enqueue(object : Callback<PagedResponse> {
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

    private fun handleError(error: Throwable) {
        //nothing
    }

    //endregion

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        viewModelCoroutineScope.coroutineContext.cancel()
        super.onCleared()
    }
}