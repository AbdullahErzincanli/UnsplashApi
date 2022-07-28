package com.example.mvvmphotosearcher.model


import com.google.gson.annotations.SerializedName

data class DownloadPhotoUrl(
    @SerializedName("url")
    val url: String
)