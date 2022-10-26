package com.aroman.testexcercise5.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aroman.testexcercise5.domain.PageKey
import com.aroman.testexcercise5.domain.PagedResponse
import com.aroman.testexcercise5.domain.RedditRepository
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityViewModel(private val repo: RedditRepository) : ViewModel() {
    private val _liveData: MutableLiveData<PagedResponse> = MutableLiveData()
    val pageList: LiveData<PagedResponse> = _liveData

    fun getPage(pageKey: PageKey) {
        viewModelCoroutineScope.launch { susGetPage(pageKey) }
    }

    private suspend fun susGetPage(pageKey: PageKey) {
        repo.getPopularMovies(pageKey).enqueue(object : Callback<PagedResponse> {
            override fun onResponse(call: Call<PagedResponse>, response: Response<PagedResponse>) {
                if (response.body() != null) {
                    _liveData.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<PagedResponse>, t: Throwable) {
                //nothing
            }
        })
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
}