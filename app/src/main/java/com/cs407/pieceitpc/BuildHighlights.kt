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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_build_highlights, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                    val parts = document.get("parts") as? Map<String, String>
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
                    parts?.forEach { (partName, partDetail) ->
                        val partView = layoutInflater.inflate(R.layout.part_item, partsContainer, false)
                        val partNameView = partView.findViewById<TextView>(R.id.partNameTextView)
                        val partDetailView = partView.findViewById<TextView>(R.id.partDetailTextView)

                        partNameView.text = partName
                        partDetailView.text = partDetail

                        partsContainer?.addView(partView)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreError", "Error fetching build details", e)
            }
    }
}
