package com.aroman.testexcercise5.di

import com.aroman.testexcercise5.data.RedditFeedApi
import com.aroman.testexcercise5.data.RetrofitClient
import com.aroman.testexcercise5.data.RetrofitRedditRepositoryImpl
import com.aroman.testexcercise5.domain.RedditRepository
import com.aroman.testexcercise5.ui.MainActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object KoinModules {

    val repository = module {
        single<RedditFeedApi> {
            RetrofitClient().provideRetrofit().create(RedditFeedApi::class.java)
        }
        single<RedditRepository> { RetrofitRedditRepositoryImpl(get<RedditFeedApi>()) }
    }

    val viewModel = module {
        viewModel { MainActivityViewModel(get<RedditRepository>()) }
    }
}