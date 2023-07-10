package com.example.jetnews.data.posts

import com.example.jetnews.model.Post
import com.example.jetnews.model.PostsFeed
import kotlinx.coroutines.flow.Flow
import com.example.jetnews.data.Result


interface PostsRepository {

    suspend fun getPost(postId: String): Result<Post>

    suspend fun getPostsFeed(): Result<PostsFeed>

    fun observeFavorites(): Flow<Set<String>>

    fun observePostsFeed(): Flow<PostsFeed?>

    suspend fun toggleFavorite(postId: String)
}