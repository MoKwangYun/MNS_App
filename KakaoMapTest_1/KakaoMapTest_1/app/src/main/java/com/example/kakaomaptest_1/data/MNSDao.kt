package com.example.kakaomaptest_1.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.kakaomaptest_1.model.Post
import com.example.kakaomaptest_1.model.User

@Dao
interface MNSDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUser(user: User)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addPost(post: Post)

    @Update
    fun updateUser(user: User)

    @Update
    fun updatePost(post: Post)

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun readAllUserData(): LiveData<List<User>>

    @Query("SELECT * FROM post_table")
    fun readAllPostData(): LiveData<List<Post>>

    @Query("SELECT * FROM post_table WHERE userCreatorId = :userid")
    fun getUserWithPosts(userid: String): List<Post>
}