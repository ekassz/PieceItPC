package com.cs407.pieceitpc

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.bumptech.glide.Glide



class BuildHighlights : Fragment() {
    val viewModel: UserViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Firebase.firestore

        val docRef = db.collection("pcBuilds")
            .document(viewModel.getBuildVal())
        docRef.get()
            .addOnSuccessListener {document ->
                if (document != null) {
//                    Log.d("TTTTTTTT", document.id + " " + document.data)
                    val buildData = document.data
                    if (buildData != null) {
                        val detailRef = buildData["detailref"] as String?
                        getBuildNoParts(buildData)
                        if (!detailRef.isNullOrEmpty()) {
                            getBuildDetails(db,detailRef)
                        }

                    }
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

    private fun getBuildDetails(database : FirebaseFirestore, detailRef: String) {
        val detailsRef = database.collection("pcBuildDetails")
            .document(detailRef)

        detailsRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val detailsData = document.data
                    if (detailsData != null) {
                        getBuildDetails(detailsData)

                    }
                }
                else {
                    Log.e("ERROR", "details doc problem")
                }
            }
            .addOnFailureListener { excep ->
                Log.e("ERROR", "error getting details " + excep)
            }
    }

    private fun getBuildNoParts(data: Map<String,Any>) {
        val view = this.view
        val title = data["title"] as String? ?: "N/A"
        val author = data["author"] as String? ?: "N/A"

        val titleTextView = view?.findViewById<TextView>(R.id.build_title)
        val authorTextView = view?.findViewById<TextView>(R.id.build_author)

        titleTextView?.text = title
        authorTextView?.text = author

    }

    private fun getBuildDetails(data:Map<String,Any>) {
        val view = this.view
        val parts = data["parts"] as? Map<String, Any> ?: emptyMap()

        val imagePath = data["imagePath"] as? String
        val pcImageView = view?.findViewById<ImageView>(R.id.pcImage)

        if (!imagePath.isNullOrEmpty()) {
            // Use Glide to load the image into the ImageView
            pcImageView?.let {
                Glide.with(this)
                    .load(imagePath)
                    .placeholder(R.drawable.pcdefault)
                    .error(R.drawable.pcdefault)
                    .into(it)
            }
        } else {
            // Set a default image if imagePath is null or empty
            pcImageView?.setImageResource(R.drawable.pcdefault)
        }

        val description = data["description"] as String? ?: "N/A"
        val case = parts["case"] as String? ?: "N/A"
        val caseCost = parts["caseCost"] as String? ?: "N/A"
        val caseFans = parts["casefans"] as String? ?: "N/A"
        val caseFansCost = parts["casefansCost"] as String? ?: "N/A"
        val cpu = parts["cpu"] as String? ?: "N/A"
        val cpuCooler = parts["cpuCooler"] as String? ?: "N/A"
        val cpuCoolerCost = parts["cpuCoolerCost"] as String? ?: "N/A"
        val cpuCost = parts["cpuCost"] as String? ?: "N/A"
        val custom = parts["custom"] as String? ?: "N/A"
        val customCost = parts["customCost"] as String? ?: "N/A"
        val memory = parts["memory"] as String? ?: "N/A"
        val memoryCost = parts["memoryCost"] as String? ?: "N/A"
        val motherboard = parts["motherboard"] as String? ?: "N/A"
        val motherboardCost = parts["motherboardCost"] as String? ?: "N/A"
        val powersupply = parts["powersupply"] as String? ?: "N/A"
        val powersupplyCost = parts["powersupplyCost"] as String? ?: "N/A"
        val storage = parts["storage"] as String? ?: "N/A"
        val storageCost = parts["storageCost"] as String? ?: "N/A"
        val videocard = parts["videocard"] as String? ?: "N/A"
        val videocardCost = parts["videocardCost"] as String? ?: "N/A"

        val descriptiontv = view?.findViewById<TextView>(R.id.buildDescription)
        val casetv = view?.findViewById<TextView>(R.id.part_case)
        val casetvCost = view?.findViewById<TextView>(R.id.part_caseCost)
        val casefantv = view?.findViewById<TextView>(R.id.part_caseFans)
        val casefantvCost = view?.findViewById<TextView>(R.id.part_caseFansCost)
        val cputv = view?.findViewById<TextView>(R.id.part_cpu)
        val cputvCost = view?.findViewById<TextView>(R.id.part_cpuCost)
        val cpuCoolertv = view?.findViewById<TextView>(R.id.part_cpuCooler)
        val cpuCoolertvCost = view?.findViewById<TextView>(R.id.part_cpuCoolerCost)
        val customtv = view?.findViewById<TextView>(R.id.part_custom)
        val customtvCost = view?.findViewById<TextView>(R.id.part_customCost)
        val memorytv = view?.findViewById<TextView>(R.id.part_memory)
        val memorytvCost = view?.findViewById<TextView>(R.id.part_memoryCost)
        val motherboardtv = view?.findViewById<TextView>(R.id.part_motherboard)
        val motherboardtvCost = view?.findViewById<TextView>(R.id.part_motherboardCost)
        val powersupplytv = view?.findViewById<TextView>(R.id.part_powerSupply)
        val powersupplytvCost = view?.findViewById<TextView>(R.id.part_powerSupplyCost)
        val storagetv = view?.findViewById<TextView>(R.id.part_storage)
        val storagetvCost = view?.findViewById<TextView>(R.id.part_storageCost)
        val videocardv = view?.findViewById<TextView>(R.id.part_videocard)
        val videocardvCost = view?.findViewById<TextView>(R.id.part_videocardCost)

        descriptiontv?.text = description
        casetv?.text = case
        casetvCost?.text = "Cost: " + caseCost
        casefantv?.text = caseFans
        casefantvCost?.text = "Cost: " + caseFansCost
        cputv?.text = cpu
        cputvCost?.text = "Cost: " + cpuCost
        cpuCoolertv?.text = cpuCooler
        cpuCoolertvCost?.text = "Cost: " + cpuCoolerCost
        customtv?.text = custom
        customtvCost?.text = "Cost: " + customCost
        memorytv?.text = memory
        memorytvCost?.text = "Cost: " + memoryCost
        motherboardtv?.text = motherboard
        motherboardtvCost?.text = "Cost: " + motherboardCost
        powersupplytv?.text = powersupply
        powersupplytvCost?.text = "Cost: " + powersupplyCost
        storagetv?.text = storage
        storagetvCost?.text = "Cost: " + storageCost
        videocardv?.text = videocard
        videocardvCost?.text = "Cost: " + videocardCost

        var totalCost = 0.0
        parts.forEach { (key, value) ->
            if (key.endsWith("Cost") && value is String) {
                totalCost += value.toDoubleOrNull() ?: 0.0
            }
        }
        val totalCostTextView = view?.findViewById<TextView>(R.id.part_totalCost)
        totalCostTextView?.text = "Total Cost: " + String.format("$%.2f", totalCost)

    }

}