package com.cs407.pieceitpc

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs407.testyoutube.YouTubeApiService
import com.cs407.testyoutube.YouTubeApiService.VideoItem
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PCTutorialHighlights.newInstance] factory method to
 * create an instance of this fragment.
 */
class PCTutorialHighlights : Fragment() {

    private val youtubeService : YouTubeApiService by lazy { YouTubeApiService() }
    private lateinit var searchEditText: EditText
    private lateinit var enterButton: Button
    private lateinit var videoRV : RecyclerView
    private lateinit var videoAdapter: VideoAdapter
    private val viewModel: UserViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //back button
        setupBackNavigation()
        val view = inflater.inflate(R.layout.fragment_p_c_tutorial_highlights, container, false)

        enterButton = view.findViewById(R.id.enterButton)
        searchEditText = view.findViewById(R.id.search_bar_text)

        var searchEntry = ""

        //actually get the string
        enterButton.setOnClickListener{
            searchEntry = searchEditText.text.toString()
            //TODO does this all go inside here?
            videoRV = view.findViewById(R.id.recyclerView)
            videoRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            fetchAndDisplayVideos(searchEntry)
        }


        return view
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_p_c_tutorial_highlights, container, false)
    }
    
    //might not be right but starting here
    //need to call "findNavController().popBackStack()" at some point, when loading data to present to screen
    private fun setupBackNavigation() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)

        // Set up the back button functionality
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp() // Navigate back to the previous fragment
        }
    }


    private fun fetchAndDisplayVideos(searchFor: String) {
        // Launch coroutine to fetch videos
        viewLifecycleOwner.lifecycleScope.launch {
            val videoItems = youtubeService.searchVideos(searchFor)
            if (videoItems.isNotEmpty()) {
                setupRecyclerView(videoItems)
            } else {
                Toast.makeText(requireContext(), "No videos found", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setupRecyclerView(videoItems: List<VideoItem>) {
        videoAdapter = VideoAdapter(videoItems, this, viewModel)
        videoRV.adapter = videoAdapter
    }

    fun addToSavedVideos(video: YouTubeApiService.VideoItem): Boolean {
        val userID = viewModel.getLoginUser()
        val db = Firebase.firestore
        //Log.e("Firestore", "viewModel.getLoginUser(): "+viewModel.getLoginUser())
        db.collection("savedVideos")
            .whereEqualTo("email", viewModel.getLoginUser())
            .limit(1)
            .get()
            .addOnSuccessListener { document ->
                if (document.isEmpty) {
                    //no saved videos yet
                    val newUserDoc = db.collection("savedVideos").document(viewModel.getLoginUser())
                    //new video doc is made for each new video to be added
                    val userVideoDoc = newUserDoc.collection("videos").document(video.id)
                    val savedVideos : MutableList<YouTubeApiService.VideoItem> = mutableListOf()
                    //create map of the video item
                    val videoInfo = hashMapOf(
                        "id" to video.id,
                        "title" to video.title,
                        "description" to video.description,
                        "thumbnailUrl" to video.thumbnailUrl,
                        "publishedAt" to video.publishedAt
                    )
                    savedVideos.add(video)
                    val docRef = db.collection("savedVideos").document(viewModel.getLoginUser()).collection("videos")
                        .add(videoInfo)
                        .addOnSuccessListener {
                            Log.d("TTTTTT", "newdoc put " + id)
                        }
                        .addOnFailureListener{
                            Log.d("FFFFF", "ERROR newdoc put " + id)
                        }
                }
                else {
                    //should be one doc returns only one user-email doc
                    for (doc in document ) {
                        //returns list of video ids ?
                        var savedVideos = doc.data["savedVideos"] as MutableList<String>
                        //TODO another firebase search to variables here not directly on saved videos
                        if (video.id in savedVideos) {
                            //put toast
                            Log.d("TTTTTT", "video exists " + id)
                        }
                        else {
                            //possibly change next line
                            savedVideos.add(video.id)
                            val videoInfo = hashMapOf(
                                "id" to video.id,
                                "title" to video.title,
                                "description" to video.description,
                                "thumbnailUrl" to video.thumbnailUrl,
                                "publishedAt" to video.publishedAt
                            )
                            db.collection("savedContent").document(viewModel.getLoginUser()).collection("videos").document(doc.id).set(videoInfo)
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
                Log.e("FIREBASERROR", "error getting all saved videos " + exception)
            }
        return true
    }
}