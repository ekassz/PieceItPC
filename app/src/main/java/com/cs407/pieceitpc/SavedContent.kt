package com.cs407.pieceitpc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar

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
    private lateinit var youtubeAdapt: CardAdapter
    private lateinit var buildInspo: Button
    private lateinit var savedVideos: Button

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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_saved_content, container, false)

        youtubeRV = view.findViewById(R.id.recyclerViewYoutube)
        youtubeRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        //val sampleYT = getSampleYT()
        //youtubeAdapt = CardAdapter(sampleYT)
        youtubeRV.adapter = youtubeAdapt
        return view
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

        // Set up the back button functionality
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp() // Navigate back to the previous fragment
        }
    }

}