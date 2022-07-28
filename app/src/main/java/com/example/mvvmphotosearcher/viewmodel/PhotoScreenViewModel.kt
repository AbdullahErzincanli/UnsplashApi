package com.example.mvvmphotosearcher.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmphotosearcher.model.UrlModels
import com.example.mvvmphotosearcher.service.ApiUtilites
import com.example.mvvmphotosearcher.utils.Constants
import retrofit.Callback
import retrofit.Response
import retrofit.Retrofit

class PhotoScreenViewModel:ViewModel() {
    val photos = MutableLiveData<ArrayList<UrlModels>>()
    val photoloading = MutableLiveData<Boolean>()
    val photoError  = MutableLiveData<Boolean>()
    private val photoList =arrayListOf<UrlModels>()
    val photoServiceUtilities = ApiUtilites()

    fun photolistClear(){
        photoList.clear()
    }
    fun photolistCheck():Boolean{

        return photoList[0].results.isEmpty()
    }

    fun getData(arama:String,pageNumber:String) {
        photoloading.value = true
        photolistClear()
        photoServiceUtilities.getApiInterface()
            .getPhotos(clientId = Constants.APP_ID,page=pageNumber,perPage = "20", query = arama)
            .enqueue(object : Callback<UrlModels> {
                override fun onResponse(response: Response<UrlModels>, retrofit: Retrofit?) {
                    photoList.add(response.body())
                    if(photolistCheck()){
                        photos.value = photoList
                        photoloading.value = false
                        photoError.value = false
                    }else{
                        photoloading.value = false
                        photoError.value = true
                    }
                }

                override fun onFailure(t: Throwable?) {
                    photoError.value = true
                }

            })

    }


}