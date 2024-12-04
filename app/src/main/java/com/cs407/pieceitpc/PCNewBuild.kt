package com.cs407.pieceitpc

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.util.UUID


class PCNewBuild : Fragment() {
    private lateinit var pcImageView: ImageView
    private lateinit var uploadImageButton: Button
    private lateinit var buildNameEditText: EditText
    private lateinit var buildDescriptionEditText: EditText
    private lateinit var partsInputEditText: EditText
    private lateinit var submitButton: Button
    private var imageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var costFields: List<EditText>
    private lateinit var totalCostText: TextView

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

        //Total Cost Setup
        costFields = listOf(
            view.findViewById(R.id.input_cpu_cost),
            view.findViewById(R.id.input_cpu_cooler_cost),
            view.findViewById(R.id.motherboard_input_cost),
            view.findViewById(R.id.memory_input_cost),
            view.findViewById(R.id.storage_input_cost),
            view.findViewById(R.id.videocard_input_cost),
            view.findViewById(R.id.case_input_cost),
            view.findViewById(R.id.powersupply_input_cost),
            view.findViewById(R.id.casefans_input_cost),
            view.findViewById(R.id.custom_input_cost)
        )

        totalCostText = view.findViewById(R.id.totalCostValue)

        costFields.forEach { editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    updateTotalCost()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }

    private fun updateTotalCost() {
        var total = 0.0

        costFields.forEach { editText ->
            val cost = editText.text.toString().toDoubleOrNull() ?: 0.0
            total += cost
        }

        totalCostText.text = String.format("%.2f", total)
    }
        // Submit New PC Build



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
