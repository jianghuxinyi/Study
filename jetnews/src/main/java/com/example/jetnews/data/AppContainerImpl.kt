package com.example.jetnews.data

import android.content.Context
import com.example.jetnews.data.interests.InterestsRepository
import com.example.jetnews.data.interests.impl.FakeInterestsRepository
import com.example.jetnews.data.posts.PostsRepository
import com.example.jetnews.data.posts.impl.FakePostsRepository

interface AppContainer  {
    val postsRepository: PostsRepository
    val interestsRepository: InterestsRepository
}

class AppContainerImpl(private val applicationContext:Context):AppContainer{
    override val postsRepository: PostsRepository by lazy {
        FakePostsRepository()
    }
    override val interestsRepository: InterestsRepository by lazy {
        FakeInterestsRepository()
    }

}