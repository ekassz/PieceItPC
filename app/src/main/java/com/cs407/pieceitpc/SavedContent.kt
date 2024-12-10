package com.cs407.pieceitpc

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs407.testyoutube.YouTubeApiService
import com.cs407.testyoutube.YouTubeApiService.VideoItem
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SavedContent.newInstance] factory method to
 * create an instance of this fragment.
 */
class SavedContent : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var youtubeRV: RecyclerView
    private lateinit var youtubeAdapt: SavedVideosAdapter
    private lateinit var buildInspo: Button
    private lateinit var savedVideos: Button
    private val youtubeService : YouTubeApiService by lazy { YouTubeApiService() }
    private lateinit var videoRV : RecyclerView
     val viewModel: UserViewModel by activityViewModels()
    private var savedVideoList = mutableListOf<VideoItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBackNavigation()
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_saved_content, container, false)

        //button to saved pcs
        buildInspo = view.findViewById(R.id.build_inspo)
        //when clicked take to here
        buildInspo.setOnClickListener{
            findNavController().navigate(R.id.buildInspo)
        }


        videoRV = view.findViewById(R.id.recyclerViewYoutube)
        videoRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        youtubeAdapt = SavedVideosAdapter(mutableListOf<VideoItem>(), this, viewModel)
        videoRV.adapter = youtubeAdapt

        val db = Firebase.firestore

        //get saved content and the document with the user based email
        db.collection("savedVideos")
            .whereEqualTo("email", viewModel.loginUser.value.email)
            .limit(1)
            .get()
            .addOnSuccessListener { usersSavedDoc ->
                //only one usersSavedDoc
                Log.i("INFO", "usersSavedDoc: "+usersSavedDoc.toString())//i get this log
                Log.i("INFO", "usersSavedDoc length: "+usersSavedDoc.size())
                if (usersSavedDoc != null) {
                    Log.i("INFO", "usersaved doc not null") //i get this log
                    for (doc in usersSavedDoc ) {
                        //get list of all video ids
                        val tasks = mutableListOf<Task<QuerySnapshot>>()
                        db.collection("savedVideos").document(doc.id).collection("videos")
                            .get()
                            .addOnSuccessListener { videoListID ->
                                Log.i("INFO", "videoList document id"+videoListID.toString())
                                if(videoListID != null){
                                    for(vid in videoListID){
                                        var task = db.collection("savedVideos").document(doc.id).collection("videos").get()
                                        tasks.add(task)
                                        task
                                            .addOnSuccessListener { videoListID ->
                                                if (videoListID != null) {
                                                    for (vid in videoListID) {
                                                        savedVideoList.add(
                                                            VideoItem(
                                                                id = vid["id"].toString(),
                                                                title = vid["title"].toString(),
                                                                description = vid["description"].toString(),
                                                                thumbnailUrl = vid["thumbnailUrl"].toString(),
                                                                publishedAt = vid["publishedAt"].toString()
                                                            )
                                                        )
                                                    }
                                                }
                                            }.addOnFailureListener { exception ->
                                                Log.e(
                                                    "FIREBASERROR",
                                                    "Error getting pc details: $exception"
                                                )
                                            }

                                    }
                                }

                            }
                        Tasks.whenAllComplete(tasks).addOnCompleteListener {
                            setupRecyclerView(savedVideoList)
                            Log.d(
                                "RecyclerView",
                                "Adapter set with ${youtubeAdapt.itemCount} items."
                            )
                        }
                    }
                    // Wait for all tasks to complete
                }
            }
        return view
    }


    private fun setupBackNavigation() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buildInspo = view.findViewById(R.id.build_inspo)
        buildInspo.setOnClickListener{
            findNavController().navigate(R.id.buildInspo)
        }
        savedVideos = view.findViewById(R.id.saved_videos)
        savedVideos.setOnClickListener{
            findNavController().navigate(R.id.savedContent)
        }

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)

        // Set up the toolbar as the fragment's ActionBar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        // Set up the back button functionality
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp() // Navigate back to the previous fragment
        }
    }


    private fun setupRecyclerView(videoItems: List<VideoItem>) {
        if(videoItems!= null) {
            youtubeAdapt = SavedVideosAdapter(videoItems, this, viewModel)
            videoRV.adapter = youtubeAdapt
        }
    }

}