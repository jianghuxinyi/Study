package com.example.jetnews.data

import android.content.Context
import com.example.jetnews.data.interests.InterestsRepository
import com.example.jetnews.data.posts.PostsRepository

interface AppContainer  {
    val postsRepository: PostsRepository
    val interestsRepository: InterestsRepository
}

class AppContainerImpl(private val applicationContext:Context):AppContainer{
    override val postsRepository: PostsRepository
        get() = TODO("Not yet implemented")
    override val interestsRepository: InterestsRepository
        get() = TODO("Not yet implemented")

}