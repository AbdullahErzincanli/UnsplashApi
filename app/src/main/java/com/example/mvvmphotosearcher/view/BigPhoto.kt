package com.example.mvvmphotosearcher.view

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.mvvmphotosearcher.R
import com.example.mvvmphotosearcher.utils.Constants
import com.example.mvvmphotosearcher.viewmodel.BigPhotoViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.fragment_big_photo.*
import kotlinx.android.synthetic.main.fragment_big_photo.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class BigPhoto : Fragment() {
    private lateinit var viewModel: BigPhotoViewModel
var isPhotoLiked:Boolean = false
    lateinit var profilName:String
    lateinit var profilUrl:String
    lateinit var downloadLinkUrl:String
    lateinit var downloadPhotoUrl: String
    lateinit var gelenUrl:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_big_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(BigPhotoViewModel::class.java)
        val actionBigPhotoToWebPage = BigPhotoDirections.actionBigPhotoToWebPage()

        (activity as AppCompatActivity).supportActionBar?.hide()

        //MobileAds.initialize(this) {}
        getActivity()?.let { MobileAds.initialize(it) }

       // mAdView = findViewById(R.id.adView)

        val adRequest = AdRequest.Builder().build()
        view.adView.loadAd(adRequest)


        arguments?.let {
            gelenUrl = BigPhotoArgs.fromBundle(it).url
            profilName = BigPhotoArgs.fromBundle(it).profilName
            profilUrl = BigPhotoArgs.fromBundle(it).profilUrl + "?utm_source=PhotoSearcher&utm_medium=referral"
            downloadLinkUrl = BigPhotoArgs.fromBundle(it).downloadUrl+"&client_id="+Constants.APP_ID


            viewModel.getDownloadUrl(downloadLinkUrl)
            txtProfilName.text = profilName

            Glide.with(this).load(gelenUrl).into(bigphotoiv)
        }

        txtPhoto.setOnClickListener {
            actionBigPhotoToWebPage.webSiteUrl = gelenUrl
            Navigation.findNavController(view).navigate(actionBigPhotoToWebPage)
        }


        txtProfilName.setOnClickListener {
            actionBigPhotoToWebPage.webSiteUrl = profilUrl
            Navigation.findNavController(view).navigate(actionBigPhotoToWebPage)
        }

        txtUnsplash.setOnClickListener {
            actionBigPhotoToWebPage.webSiteUrl = Constants.UNSPLASH_URL
            Navigation.findNavController(view).navigate(actionBigPhotoToWebPage)
        }

        btnLike.setOnClickListener {
            isPhotoLiked = photoLikedCheck()
            setFloatingButtonSrc()
        }

        btnDownload.setOnClickListener {
            setDownloadButtonSrc("downloading")
            downloadImage(downloadPhotoUrl)
        }
        observeLiveData()
    }


    private fun setDownloadButtonSrc(situation:String){
        if(situation == "downloading"){
            btnDownload.setImageResource(R.drawable.ic_downloading)
        }else if(situation == "downloaded"){
            btnDownload.setImageResource(R.drawable.ic_downloaddone)
        }
    }

    private fun observeLiveData(){
        viewModel.photoDownloadUrl.observe(viewLifecycleOwner, Observer { photourl ->
            photourl?.let {

                downloadPhotoUrl = it
            }
        })

        viewModel.photoDownloadLoading.observe(viewLifecycleOwner, Observer { photoLoading ->
            photoLoading?.let {
                if(photoLoading == true){

                }
            }
        })
        viewModel.photoDownloadError.observe(viewLifecycleOwner, Observer { photoError ->
            photoError?.let {
                if(photoError == true){
                }
            }
        })
    }

    fun downloadImage(imageURL: String) {
        if (!verifyPermissions()!!) {
            return
        }
        Glide.with(this)
            .load(imageURL)
            .into(object : CustomTarget<Drawable?>() {
                override fun onResourceReady(
                    resource: Drawable,
                    @Nullable transition: Transition<in Drawable?>?
                ) {
                    val bitmap = (resource as BitmapDrawable).bitmap
                    makeToastMassages("Saving Image...")

                    lifecycleScope.launch{
                        saveBitmapFile(bitmap)
                    }
                }

                override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
                override fun onLoadFailed(@Nullable errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    makeToastMassages("Failed to Download Image! Please try again later.")

                }
            })
    }


    fun verifyPermissions(): Boolean? {

        // This will return the current Status
        val permissionExternalMemory =
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionExternalMemory != PackageManager.PERMISSION_GRANTED) {
            val STORAGE_PERMISSIONS = arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            // If permission not granted then ask for permission real time.
            requestPermissions(STORAGE_PERMISSIONS, 1)
            return false
        }
        return true
    }


    private suspend fun saveBitmapFile(mBitmap: Bitmap?){
        var result = ""
        withContext(Dispatchers.IO){
            if(mBitmap != null){
                try{
                    val bytes = ByteArrayOutputStream()
                    mBitmap.compress(Bitmap.CompressFormat.PNG,90,bytes)

                    val ff = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()
                            +"/"+getString(R.string.app_name))
                    ff.mkdirs()
                    val fname:String = (System.currentTimeMillis()/1000).toString()+".png"
                    val f = File(ff,fname)

                    val fo = FileOutputStream(f)
                    fo.write(bytes.toByteArray())
                    fo.close()
                    result = f.absolutePath

                    activity?.runOnUiThread {
                        if(result.isNotEmpty()){
                            setDownloadButtonSrc("downloaded")
                            makeToastMassages("File saved successfully : $result")


                        }else{
                            makeToastMassages("Something went wrong while saving the file.")

                        }

                    }
                }
                catch (e: java.lang.Exception){
                    result = ""
                    e.printStackTrace()
                }
            }
        }
    }

    private fun goToUrl(url:String){
       // val action = BigPhotoDirections.actionBigPhotoToWebPage()
      //  action.webSiteUrl = url


      //  var uriUrl:Uri = Uri.parse(url)
      //  var launchBrowser = Intent(Intent.ACTION_VIEW,uriUrl)
      //  startActivity(launchBrowser)
    }

    private fun photoLikedCheck():Boolean{
        return !isPhotoLiked
    }

    private fun setFloatingButtonSrc(){
        if(isPhotoLiked){
            btnLike.setImageResource(R.drawable.ic_favori)
            makeToastMassages("Photo Liked!")
        }else{
            btnLike.setImageResource(R.drawable.ic_favoriborder)
            makeToastMassages("Photo Unliked!")
        }
    }

    private fun makeToastMassages(mesagges:String){
        Toast.makeText(activity,mesagges,Toast.LENGTH_SHORT).show()
    }

}