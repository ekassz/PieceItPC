package com.cs407.pieceitpc

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar


class PCNewBuild : Fragment() {
    private lateinit var pcImageView: ImageView
    private lateinit var uploadImageButton: Button
    private lateinit var buildNameEditText: EditText
    private lateinit var buildDescriptionEditText: EditText
    private lateinit var partsInputEditText: EditText
    private lateinit var submitButton: Button
    private var imageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //show back button
        setupBackNavigation()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_build, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)
        // Set up the back button functionality
        toolbar.setNavigationOnClickListener {
            // Navigate back to the previous fragment
            findNavController().navigateUp()
        }

        uploadImageButton = view.findViewById(R.id.uploadImageButton)
        pcImageView = view.findViewById(R.id.pcImageUpload)

        // Uploading PC Image Selection
        uploadImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK) {
            imageUri = data?.data
            pcImageView.setImageURI(imageUri) // Display selected image
        }
    }

    private fun setupBackNavigation() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }


}