package com.example.kakaomaptest_1.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "user_table")
data class User(
    @PrimaryKey val id: String,
    val password: String
): Parcelable

@Parcelize
@Entity(tableName = "post_table")
data class Post(
    @PrimaryKey(autoGenerate = true) val key: Int,
    val userCreatorId: String,
    val title: String,
    val markerType: Int,
    val lati: Double,
    val longi: Double,
    val photoUri: String,
    val text: String,
    val date: Date
): Parcelable

