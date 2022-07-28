package com.example.mvvmphotosearcher.service

import com.example.mvvmphotosearcher.utils.Constants
import retrofit.GsonConverterFactory

class ApiUtilites {

    fun getApiInterface(): ApiInterface{
        val retrofit: retrofit.Retrofit = retrofit.Retrofit.Builder() // retrofit build ediyoruz
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiInterface::class.java)
    }


    fun getUrl(): ApiInterface{
        val retrofit: retrofit.Retrofit = retrofit.Retrofit.Builder() // retrofit build ediyoruz
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiInterface::class.java)
    }

}