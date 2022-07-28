package com.example.mvvmphotosearcher.viewmodel

import androidx.lifecycle.*
import com.example.mvvmphotosearcher.model.DownloadPhotoUrl
import com.example.mvvmphotosearcher.service.ApiUtilites


import retrofit.Callback
import retrofit.Response
import retrofit.Retrofit


class BigPhotoViewModel:ViewModel() {
    val apiUtilities = ApiUtilites()
    val photoDownloadUrl = MutableLiveData<String>()
    val photoDownloadLoading = MutableLiveData<Boolean>()
    val photoDownloadError  = MutableLiveData<Boolean>()




fun getDownloadUrl(downloadLinkUrl:String){

    apiUtilities.getUrl().getDownloadUrl(downloadLinkUrl).enqueue(object : Callback<DownloadPhotoUrl>{
        override fun onResponse(response: Response<DownloadPhotoUrl>?, retrofit: Retrofit?) {
            response?.body()?.url?.let {
                photoDownloadUrl.value = it
                photoDownloadLoading.value = false
                photoDownloadError.value = false
            }
        }
        override fun onFailure(t: Throwable?) {
            photoDownloadLoading.value = true
            photoDownloadError.value = true
        }
    })
}

}