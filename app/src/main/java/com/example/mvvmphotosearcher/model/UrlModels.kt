package com.example.mvvmphotosearcher.model


import com.example.mvvmphotosearcher.model.Result
import com.google.gson.annotations.SerializedName

data class UrlModels(
    @SerializedName("results")
    val results: List<Result>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)