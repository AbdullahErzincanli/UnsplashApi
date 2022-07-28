package com.example.mvvmphotosearcher.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.mvvmphotosearcher.R
import com.example.mvvmphotosearcher.viewmodel.PhotoScreenViewModel
import com.example.mvvmphotosearcher.viewmodel.SplashScreenViewModel


class SplashScreen : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()

        val colorDrawable = ColorDrawable(Color.parseColor("#6C757D"))//ee6c4d  335c67  590D22
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(colorDrawable)

        Handler().postDelayed({
            // Start the Intro Activity
            val action = SplashScreenDirections.actionSplashScreenToPhotoScreen()
            action.firstTimeOpen = true
            Navigation.findNavController(view).navigate(action)
        }, 4000)
    }

}