package com.example.mvvmphotosearcher.view


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mvvmphotosearcher.R
import com.example.mvvmphotosearcher.adapter.PhotosAdapter
import com.example.mvvmphotosearcher.model.UrlModels
import com.example.mvvmphotosearcher.viewmodel.PhotoScreenViewModel
import kotlinx.android.synthetic.main.fragment_photo_screen.*
import kotlin.collections.ArrayList
import kotlin.random.Random


class PhotoScreen : Fragment() {
    private lateinit var viewModel: PhotoScreenViewModel
    private var currentPage:Int = 1
    private var maxPage: Int = 100
    private var lastSearch:String = ""
    private var randomWords = arrayOf<String>("landscape","new","planet","cities","flowers","last","decoration","setup")
    private var currentSearch:String = randomWords[Random(System.nanoTime()).nextInt(0,7)]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val colorDrawable = ColorDrawable(Color.parseColor("#6C757D"))//ee6c4d  335c67  590D22
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(colorDrawable)
        viewModel = ViewModelProvider(this).get(PhotoScreenViewModel::class.java)

        firstTimeOpen()

        btnSearch.setOnClickListener {
            if(editBoxCheck()){
                currentPageReset()
                currentPageWriter()
                getPhotos(currentSearch)
            }



        }

        btnOpenGallery.setOnClickListener {
            openGallery()
        }

        edtSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if(editBoxCheck()){
                    currentPageReset()
                    currentPageWriter()
                    getPhotos(currentSearch)
                }


                return@OnEditorActionListener true
            }
            false
        })

        btnRight.setOnClickListener {
            if(currentSearchCheck()){
                currentPageIncreament()
                currentPageCheck()
                currentPageWriter()
                getPhotos(currentSearch)
            }


            //getPhotos(currentSearch)

        }

        btnLeft.setOnClickListener {
            if(currentSearchCheck()){
                currentPageDecreament()
                currentPageCheck()
                currentPageWriter()
                getPhotos(currentSearch)
            }

            //getPhotos(currentSearch)
        }
        currentPageWriter()
        observeLiveData()
    }


    fun firstTimeOpen(){
        if(lastSearch.isNullOrBlank()){
            if(currentSearchCheck()){
                currentPageReset()
                currentPageWriter()
                getPhotos(currentSearch)
            }
        }else{
            return
        }
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

    private fun openGallery(){
        if(!verifyPermissions()!!){
            return
        }
        var showImage = Intent(Intent.ACTION_VIEW)
        showImage.type="image/*"
        showImage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(showImage)
    }

    private fun editBoxCheck():Boolean{
        if(edtSearch.text.isNullOrBlank()){
            makeToastMessagges("You must write something.")
            return false
        }else{
            setCurrentSearch()
            return true
        }
    }
    private fun currentSearchCheck():Boolean{
        if(currentSearch.isNullOrBlank()){
            makeToastMessagges("You must write something.")
            return false
        }else{
            return true
        }
    }

    private fun setCurrentSearch(){
        currentSearch = edtSearch.text.toString()
    }

    private fun currentPageReset(){
        currentPage = 1
    }
    private fun currentPageIncreament(){
        currentPage++
    }
    private fun currentPageDecreament(){
        currentPage --
    }
    private fun currentPageCheck(){
        if(currentPage<1) currentPageReset()
        if(currentPage>maxPage) currentPageSetter()
    }
    private fun currentPageSetter(){
        currentPage = maxPage
    }
    private fun currentPageWriter(){
        txtPage.text = currentPage.toString()+"/"+maxPage.toString()
    }
    private fun getPhotos(searchingText:String){
        viewModel.getData(searchingText,currentPage.toString())
    }

    private fun buttonsAreEnable(){
        btnRight.isEnabled = true
        btnLeft.isEnabled = true
        btnSearch.isEnabled = true
    }

    private fun buttonsAreDisable(){
        btnRight.isEnabled = false
        btnLeft.isEnabled = false
        btnSearch.isEnabled = false
    }

    fun addPhotoToAdapter(photos: ArrayList<UrlModels>){
            recyclerView.apply {
                layoutManager = GridLayoutManager(context,3)
                adapter = PhotosAdapter(photos)
            }
    }

    private fun checkPhotoSize(photos: ArrayList<UrlModels>){
        maxPage = photos[0].total/20
        currentPageCheck()
        currentPageWriter()
    }
    private fun setLastSearch(){
        lastSearch = currentSearch
    }
    private fun recoveSearching(){
        currentSearch = lastSearch
    }
private fun checkLastSearch():Boolean{
    return !lastSearch.isNullOrBlank()
}
   private fun observeLiveData(){
       viewModel.photos.observe(viewLifecycleOwner, Observer { photos ->
           photos?.let {
              checkPhotoSize(it)
               setLastSearch()
               addPhotoToAdapter(it)
               buttonsAreEnable()
           }
       })

       viewModel.photoloading.observe(viewLifecycleOwner, Observer { photoLoading ->
           photoLoading?.let {
               if(photoLoading == true){
                   makeToastMessagges("Searching Please Wait.")
                  buttonsAreDisable()
               }
           }
       })
       viewModel.photoError.observe(viewLifecycleOwner, Observer { photoError ->
           photoError?.let {
               if(photoError == true){
                   makeToastMessagges("Searching Failed.")
                   if(checkLastSearch()){
                       recoveSearching()
                       getPhotos(currentSearch)
                   }
                   buttonsAreEnable()
               }
           }
       })

    }

    private fun makeToastMessagges(mesagges:String){
        Toast.makeText(activity,mesagges,Toast.LENGTH_LONG).show()
    }

}