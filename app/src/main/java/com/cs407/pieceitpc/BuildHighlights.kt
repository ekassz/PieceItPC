package com.cs407.pieceitpc

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Types.NULL


class BuildHighlights : Fragment() {
    val viewModel: UserViewModel by activityViewModels()
    private val db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Firebase.firestore

        val docRef = db.collection("pcBuilds")
            .document(viewModel.getBuildVal())
        docRef.get()
            .addOnSuccessListener {document ->
                if (document != null) {
                    Log.d("TTTTTTTT", document.id + " " + document.data)
                }
            }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Show back button
        setupBackNavigation()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_build_highlights, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)
        // Set up the back button functionality
        toolbar.setNavigationOnClickListener {
            // Navigate back to the previous fragment
            findNavController().navigateUp()
        }
        val buildId = viewModel.getBuildVal()
        //build data from pcBuilds
        db.collection("pcBuilds").document(buildId).get().addOnSuccessListener {
            document ->
            if (document != null){
                val title = document.getString("title") ?: "Untitled Build"
                val summary = document.getString("summary") ?: "No description available."
                val author = document.getString("author") ?: "Unknown"
                val detailRef = document.getString("detailref")

                // Set basic build info
                view?.findViewById<TextView>(R.id.cardTitle)?.text = title
                view?.findViewById<TextView>(R.id.cardDescription)?.text = summary
                view?.findViewById<TextView>(R.id.cardAuthor)?.text = "by $author"

                // fetch Detailed parts list from pcBuildDetails
                if(!detailRef.isNullOrEmpty()){
                    fetchBuildDetails(detailRef)
                }

            }
        }
            .addOnFailureListener{ e ->
                Log.e("FirestoreError", "Error fetching build data", e)
            }
    }
    private fun fetchBuildDetails(detailRef: String) {
        db.collection("pcBuildDetails").document(detailRef).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val description = document.getString("description") ?: "No description available."
                    val title = document.getString("title") ?: "PC Build Highlight"
                    val parts = document.get("parts") as? Map<String, Any>
                    val imagePath = document.getString("imagePath") ?: "android.resource://${requireContext().packageName}/drawable/pcdefault"

                    val pcImageView = view?.findViewById<ImageView>(R.id.pcImage)

                    if (pcImageView != null) {
                        Glide.with(this)
                            .load(imagePath)
                            .placeholder(R.drawable.pcdefault)
                            .error(R.drawable.pcdefault)
                            .into(pcImageView)
                    }


                    view?.findViewById<TextView>(R.id.buildDescription)?.text = description
                    view?.findViewById<TextView>(R.id.titleTextView)?.text = title


                    // Populate parts list
                    val partsContainer = view?.findViewById<LinearLayout>(R.id.partListContainer)
                    partsContainer?.removeAllViews()
                    parts?.filterKeys { !it.endsWith("Cost") } // Filter out cost keys
                        ?.forEach { (partName, partDetail) ->
                        if (partDetail is String) {
                            // Assume parts with corresponding costs exist
                            val costKey = partName + "Cost"
                            val cost = parts[costKey] as? String ?: "N/A"

                            val partView =
                                layoutInflater.inflate(R.layout.part_item, partsContainer, false)
                            val partNameView =
                                partView.findViewById<TextView>(R.id.partNameTextView)
                            val partCostView =
                                partView.findViewById<TextView>(R.id.partDetailTextView)

                            partNameView.text = partDetail // Display the part name
                            partCostView.text = cost // Display the part cost

                            partsContainer?.addView(partView)
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreError", "Error fetching build details", e)
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
