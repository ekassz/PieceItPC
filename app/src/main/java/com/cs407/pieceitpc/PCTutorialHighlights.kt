package com.cs407.pieceitpc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs407.testyoutube.YouTubeApiService
import com.cs407.testyoutube.YouTubeApiService.VideoItem
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth

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
        setHasOptionsMenu(true)
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

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val context = this.context
        return when(item.itemId){
            R.id.logoutNav -> {
                FirebaseAuth.getInstance().signOut()
                findNavController().navigate(R.id.action_PCTutorialHighlights_to_loginOrGuest)
                Toast.makeText(context, "Logout Successful", Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)

        // Set up the toolbar as the fragment's ActionBar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

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
}

}

