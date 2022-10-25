package com.aroman.testexcercise5

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.aroman.testexcercise5.data.RedditFeedApi
import com.aroman.testexcercise5.data.RetrofitClient
import com.aroman.testexcercise5.data.RetrofitRedditRepositoryImpl
import com.aroman.testexcercise5.domain.PagedResponse
import com.aroman.testexcercise5.domain.RedditRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val repo: RedditRepository = RetrofitRedditRepositoryImpl(
        RetrofitClient().provideRetrofit().create(RedditFeedApi::class.java)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler().post {
            val call = repo.getPopularMovies(5, null, null)
            call.enqueue(object : Callback<PagedResponse> {
                override fun onResponse(
                    call: Call<PagedResponse>,
                    response: Response<PagedResponse>
                ) {
                    Log.d("@@@", response.body().toString())
                }

                override fun onFailure(call: Call<PagedResponse>, t: Throwable) {
                    //nothing
                }
            })
        }
    }
}