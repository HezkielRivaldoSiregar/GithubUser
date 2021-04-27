package com.dicoding.githubuser

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var username: String = "",
    var name: String = "",
    var location: String = "",
    var repository: String = "",
    var company: String = "",
    var followers: String = "",
    var following: String = "",
    var avatar: String = "",
    var githubUrl: String = "",
    var id: Int = 1
) : Parcelable
