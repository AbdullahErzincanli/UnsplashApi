package com.example.mvvmphotosearcher.model


import com.google.gson.annotations.SerializedName

data class TopicSubmissionsX(
    @SerializedName("architecture")
    val architecture: Architecture,
    @SerializedName("nature")
    val nature: Nature,
    @SerializedName("travel")
    val travel: Travel,
    @SerializedName("wallpapers")
    val wallpapers: WallpapersX
)