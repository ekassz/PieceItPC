package com.cs407.pieceitpc

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SavedContent.newInstance] factory method to
 * create an instance of this fragment.
 */
class SavedContentOtherBuilds : Fragment(), AddToSavedContent {
        inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val buildImage: ImageView = itemView.findViewById(R.id.cardImage)
            val buildTitle: TextView = itemView.findViewById(R.id.cardTitle)
            val buildDescription: TextView = itemView.findViewById(R.id.cardDescription)
            val buildAuthor: TextView = itemView.findViewById(R.id.cardAuthor)
            val buildSavedButton: Button = itemView.findViewById(R.id.save_build_button)
        }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var othersRV: RecyclerView
    private lateinit var othersAdapt: CardAdapter
    private lateinit var savedVideos: Button
    private lateinit var buildInspo: Button
    override val viewModel: UserViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        /**
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        **/
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_saved_content_builds, container, false)

        /**
        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar?.title = getString(R.string.app_name)
        toolbar?.setNavigationIcon(R.drawable.small_app_icon)
        toolbar?.setNavigationOnClickListener {
            findNavController().navigateUp()
        }**/

        othersRV = view.findViewById(R.id.recyclerViewBuilds)
        othersRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        othersAdapt = CardAdapter(mutableListOf<CardItem>(), null, this, viewModel)
        othersRV.adapter = othersAdapt

        //TODO retrieve saved
        val db = Firebase.firestore
        db.collection("savedContent")
            //.orderBy("timeMS", Query.Direction.DESCENDING)
            .limit(3)
            .get()
            .addOnSuccessListener { documents ->
                val builds = mutableListOf<CardItem>()
                val tasks = mutableListOf<Task<DocumentSnapshot>>()
                for (doc in documents) {
                    val data = doc.data

                    builds.add(
                        CardItem(
                            id = doc.id,
                            imageResId = R.drawable.placeholder.toString(),
                            title = data["title"] as? String ?: "Untitled",
                            description = data["summary"] as? String ?: "No description",
                            author = data["author"] as? String ?: "Unknown"
                        )
                    )

                    val detailRef = data["detailref"] as? String
                    if (detailRef != null) {
                        // Fetch `pcBuildDetails` document
                        val task = db.collection("pcBuildDetails").document(detailRef).get()
                        tasks.add(task)

                        task
                            .addOnSuccessListener { detailDoc ->
                                val imagePath = detailDoc.getString("imagePath")
                                    ?: "android.resource://${requireContext().packageName}/drawable/pcdefault"
                                builds.add(
                                    CardItem(
                                        id = doc.id,
                                        imageResId = imagePath,
                                        title = data["title"] as? String ?: "Untitled",
                                        description = data["summary"] as? String
                                            ?: "No description",
                                        author = data["author"] as? String ?: "Unknown"
                                    )
                                )
                            }.addOnFailureListener { exception ->
                                Log.e("FIREBASERROR", "Error getting pc details: $exception")
                            }
                    }
                }
                // Wait for all tasks to complete
                Tasks.whenAllComplete(tasks).addOnCompleteListener {
                    othersAdapt.updateData(builds)
                    //othersAdapt = CardAdapter(builds, null, this, viewModel)
                    //othersRV.adapter = othersAdapt
                    Log.d("RecyclerView", "Adapter set with ${othersAdapt.itemCount} items.")
                    //othersAdapt.updateData(builds)

                }
            }
            .addOnFailureListener { exception ->
                Log.e("FIREBASERROR", "Error getting pc builds: $exception")
            }
        return view
    }
/**
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SavedContent.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SavedContent().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
**/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedVideos = view.findViewById(R.id.saved_videos)
        savedVideos.setOnClickListener {
        findNavController().navigate(R.id.savedContent)
        }
        buildInspo = view.findViewById(R.id.build_inspo)
        buildInspo.setOnClickListener {
        findNavController().navigate(R.id.buildInspo)
        }

    val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            // Navigate back to the previous fragment
            findNavController().navigateUp()
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

    fun addToSaveContent(id : String) : Boolean {
        val db = Firebase.firestore
        db.collection("savedContent")
            .whereEqualTo("email", viewModel.getLoginUser())
            .limit(1)
            .get()
            .addOnSuccessListener { document ->
                if (document.size() == 0) {
                    val builds : MutableList<String> = mutableListOf()
                    builds.add(id)
                    val contents = hashMapOf(
                        "email" to viewModel.getLoginUser(),
                        "savedBuilds" to builds
                    )
                    val docRef = db.collection("savedContent")
                        .add(contents)
                        .addOnSuccessListener {
                            Log.d("TTTTTT", "newdoc put " + id)
                        }
                        .addOnFailureListener{
                            Log.d("FFFFFF", "ERROR newdoc put " + id)
                        }

                }
                else {
                    for (doc in document ) {
                        var savedBuilds = doc.data["savedBuilds"] as MutableList<String>
                        if (id in savedBuilds) {
                            //put toast
                            Log.d("TTTTTT", "buildid exists " + id)
                        }
                        else {
                            savedBuilds.add(id)
                            val contents = hashMapOf(
                                "email" to viewModel.getLoginUser(),
                                "savedBuilds" to savedBuilds
                            )
                            db.collection("savedContent").document(doc.id).set(contents)
                                .addOnSuccessListener {
                                    Log.d("TTTTTT", "existing doc update " + doc.id)
                                }
                                .addOnFailureListener{
                                    Log.d("FFFFFF", "ERROR existing doc update " + doc.id)
                                }
                        }

                    }
                }


            }
            .addOnFailureListener { exception ->
                Log.e("FIREBASERROR", "error getting pc builds " + exception)
            }
        return true
    }

}