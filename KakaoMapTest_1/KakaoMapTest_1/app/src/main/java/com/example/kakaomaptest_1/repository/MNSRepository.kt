package com.example.kakaomaptest_1.repository

import androidx.lifecycle.LiveData
import com.example.kakaomaptest_1.data.MNSDao
import com.example.kakaomaptest_1.model.Post
import com.example.kakaomaptest_1.model.User

class MNSRepository(private val MNSDao: MNSDao) {

    val readAllUserData: LiveData<List<User>> = MNSDao.readAllUserData()
    val readAllPostData: LiveData<List<Post>> = MNSDao.readAllPostData()

    suspend fun addUser(user: User) {
        MNSDao.addUser(user)
    }

    suspend fun addPost(post: Post) {
        MNSDao.addPost(post)
    }

    suspend fun updateUser(user: User) {
        MNSDao.updateUser(user)
    }

    suspend fun updatePost(post: Post) {
        MNSDao.updatePost(post)
    }

    suspend fun getUserWithPosts(id: String) {
        MNSDao.getUserWithPosts(id)
    }

}