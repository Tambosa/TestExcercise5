package com.aroman.testexcercise5.di

import androidx.room.Room
import com.aroman.testexcercise5.data.local.RedditPostDao
import com.aroman.testexcercise5.data.local.RoomConst
import com.aroman.testexcercise5.data.local.RoomDb
import com.aroman.testexcercise5.data.local.RoomLocalRedditRepositoryImpl
import com.aroman.testexcercise5.data.remote.RedditFeedApi
import com.aroman.testexcercise5.data.remote.RetrofitClient
import com.aroman.testexcercise5.data.remote.RetrofitRedditRepositoryImpl
import com.aroman.testexcercise5.domain.LocalRedditRepository
import com.aroman.testexcercise5.domain.RedditRepository
import com.aroman.testexcercise5.ui.MainActivityViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object KoinModules {

    val repository = module {
        single<RedditFeedApi> {
            RetrofitClient().provideRetrofit().create(RedditFeedApi::class.java)
        }
        single<RedditRepository> { RetrofitRedditRepositoryImpl(get<RedditFeedApi>()) }

        single<RoomDb> {
            Room.databaseBuilder(
                androidApplication(),
                RoomDb::class.java,
                RoomConst.DB_NAME
            ).build()
        }
        single<RedditPostDao> {
            get<RoomDb>().RedditPostDao()
        }
        single<LocalRedditRepository> { RoomLocalRedditRepositoryImpl(get<RedditPostDao>()) }
    }

    val viewModel = module {
        viewModel { MainActivityViewModel(get<RedditRepository>(), get<LocalRedditRepository>()) }
    }
}