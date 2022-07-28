package com.example.mvvmphotosearcher.service

import com.example.mvvmphotosearcher.model.DownloadPhotoUrl
import com.example.mvvmphotosearcher.model.UrlModels
import retrofit.Call
import retrofit.http.GET
import retrofit.http.Query
import retrofit.http.Url

interface ApiInterface {
    @GET("search/photos/?")
    fun getPhotos(
        @Query("client_id") clientId: String,
        @Query("page") page:String,
        @Query("per_page") perPage: String,
        @Query("query") query: String
    ): Call<UrlModels>


    @GET
    fun getDownloadUrl(@Url fullUrl:String ): Call<DownloadPhotoUrl>

}