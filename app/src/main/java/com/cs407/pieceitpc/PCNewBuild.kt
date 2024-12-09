package com.cs407.pieceitpc

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class PCNewBuild : Fragment() {
    private lateinit var pcImageView: ImageView
    private lateinit var uploadImageButton: Button
    private lateinit var buildNameEditText: EditText
    private lateinit var buildDescriptionEditText: EditText
    private lateinit var submitButton: Button
    private var imageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var costFields: List<EditText>
    private lateinit var partNameFields: List<EditText>
    private lateinit var totalCostText: TextView

    private val db = com.google.firebase.ktx.Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

        // Set up the toolbar as the fragment's ActionBar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

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

        // Part Names List
        partNameFields = listOf(
            view.findViewById(R.id.cpuInput),
            view.findViewById(R.id.cpu_cooler_input),
            view.findViewById(R.id.motherboard_input),
            view.findViewById(R.id.memory_input),
            view.findViewById(R.id.storage_input),
            view.findViewById(R.id.videocard_input),
            view.findViewById(R.id.case_input),
            view.findViewById(R.id.powersupply_input),
            view.findViewById(R.id.casefans_input),
            view.findViewById(R.id.custom_input)
        )

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

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }

        buildNameEditText = view.findViewById(R.id.newBuildTitle)
        buildDescriptionEditText = view.findViewById(R.id.buildDescription)
        submitButton = view.findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
            val buildName = buildNameEditText.text.toString()
            val buildDescription = buildDescriptionEditText.text.toString()
            val totalCost = totalCostText.text.toString().toDoubleOrNull() ?: 0.0
            val currentUser = FirebaseAuth.getInstance().currentUser
            val author = currentUser?.email ?: "Anonymous"
            var isValid = true
            if (buildName.isEmpty() || buildDescription.isEmpty()) {
                Toast.makeText(context, "Please fill in all required fields!", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (imageUri == null) {
                Toast.makeText(context, "Please select an image!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Firebase parts map
            val parts = mutableMapOf<String, Any?>()
            partNameFields.forEachIndexed { index, partNameField ->
                val partName = partNameField.text.toString().trim()
                val partCost = costFields[index].text.toString().trim()

                if (partName.isNotEmpty() || partCost.isNotEmpty()) {
                    when (index) {
                        0 -> {
                            parts["cpu"] = partName
                            parts["cpuCost"] = partCost
                        }

                        1 -> {
                            parts["cpuCooler"] = partName
                            parts["cpuCoolerCost"] = partCost
                        }

                        2 -> {
                            parts["motherboard"] = partName
                            parts["motherboardCost"] = partCost
                        }

                        3 -> {
                            parts["memory"] = partName
                            parts["memoryCost"] = partCost
                        }

                        4 -> {
                            parts["storage"] = partName
                            parts["storageCost"] = partCost
                        }

                        5 -> {
                            parts["videocard"] = partName
                            parts["videocardCost"] = partCost
                        }

                        6 -> {
                            parts["case"] = partName
                            parts["caseCost"] = partCost
                        }

                        7 -> {
                            parts["powersupply"] = partName
                            parts["powersupplyCost"] = partCost
                        }

                        8 -> {
                            parts["casefans"] = partName
                            parts["casefansCost"] = partCost
                        }

                        9 -> {
                            parts["custom"] = partName
                            parts["customCost"] = partCost
                        }
                    }
                } else if (partCost.isNotEmpty() && partName.isEmpty()) {
                    partNameField.error = "Part name is required if the cost is provided."
                    isValid = false
                } else if (partName.isNotEmpty() && partCost.isEmpty()) {
                    costFields[index].error = "Cost is required if part name is provided."
                    isValid = false
                }
            }

            if (!isValid) return@setOnClickListener

            // Firebase Storage - PCBuildImages
            val storageRef =
                FirebaseStorage.getInstance().getReference("PCBuildImages/${UUID.randomUUID()}.jpg")
            val stream = requireContext().contentResolver.openInputStream(imageUri!!)
            storageRef.putStream(stream!!)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        val imageUrl = downloadUrl.toString()

                        val detailRefId = db.collection("pcBuildDetails").document().id
                        val detailData = mapOf(
                            "description" to buildDescription,
                            "imagePath" to imageUrl,
                            "parts" to parts
                        )

                        db.collection("pcBuildDetails").document(detailRefId).set(detailData)
                            .addOnSuccessListener {
                                val buildData = mapOf(
                                    "author" to author,
                                    "detailref" to detailRefId,
                                    "summary" to buildDescription,
                                    "timeMS" to System.currentTimeMillis(),
                                    "title" to buildName
                                    //"image" to imageUrl
                                )

                                db.collection("pcBuilds").add(buildData)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            context,
                                            "Build saved successfully.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        findNavController().navigate(R.id.home_screen)
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("FirestoreError", "Error saving build summary", e)
                                    }
                            }
                            .addOnFailureListener { e ->
                                Log.e("FirestoreError", "Error saving build details", e)
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("StorageError", "Error uploading image", e)
                    Toast.makeText(context, "Failed to upload image!", Toast.LENGTH_SHORT).show()
                }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK) {
            imageUri = data?.data
            pcImageView.setImageURI(imageUri)
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
