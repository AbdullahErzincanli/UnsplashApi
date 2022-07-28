package com.example.mvvmphotosearcher.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mvvmphotosearcher.R
import kotlinx.android.synthetic.main.fragment_web_page.*


class WebPage : Fragment() {
    lateinit var webSiteLink:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            webSiteLink = WebPageArgs.fromBundle(it).webSiteUrl
        }

        wvSite.loadUrl(webSiteLink)

    }

}