package com.cs407.pieceitpc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class ScanPart : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //show back button
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_parts, container, false)

    }


    //might not be right but starting here
    //findNavController().popBackStack()



}