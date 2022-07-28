package com.example.mvvmphotosearcher.model


import com.google.gson.annotations.SerializedName

data class TopicSubmissions(
    @SerializedName("wallpapers")
    val wallpapers: Wallpapers
)