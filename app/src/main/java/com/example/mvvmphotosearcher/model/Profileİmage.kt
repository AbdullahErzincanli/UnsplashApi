package com.example.mvvmphotosearcher.model


import com.google.gson.annotations.SerializedName

data class Profileİmage(
    @SerializedName("large")
    val large: String,
    @SerializedName("medium")
    val medium: String,
    @SerializedName("small")
    val small: String
)