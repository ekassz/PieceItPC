package com.cs407.pieceitpc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar


class HomeScreenFragment : Fragment() {

     private lateinit var cardRecyclerView: RecyclerView
     private lateinit var cardAdapter: CardAdapter
     private lateinit var startNewBuild: Button
     private lateinit var savedContent: Button
     private lateinit var buildTuts: Button
     private lateinit var scan: ImageView

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setHasOptionsMenu(true)

     }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home_screen, container, false)

        // Set up the toolbar
        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar?.title = getString(R.string.app_name)
        toolbar?.setNavigationIcon(R.drawable.small_app_icon)
        toolbar?.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        //RecycleView
        cardRecyclerView = view.findViewById(R.id.recyclerView)
        cardRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        //Sample Hard-Coded Card Item Data
        val sampleBuilds = getSampleBuilds()
        cardAdapter = CardAdapter(sampleBuilds)
        cardRecyclerView.adapter = cardAdapter

        startNewBuild = view.findViewById(R.id.newBuild)
        savedContent = view.findViewById(R.id.savedContent)
        buildTuts = view.findViewById(R.id.tutorials)
        scan = view.findViewById(R.id.scanImage)

        startNewBuild.setOnClickListener {
            findNavController().navigate(R.id.newBuild)
        }

        buildTuts.setOnClickListener {
            findNavController().navigate(R.id.homeScreen_to_buildHighlights)
        }
        savedContent.setOnClickListener{
            findNavController().navigate(R.id.savedContent)
        }
        scan.setOnClickListener {
            findNavController().navigate(R.id.scan)
        }

        return view
    }

     override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
         inflater.inflate(R.menu.profile_menu, menu)
         super.onCreateOptionsMenu(menu, inflater)
     }

     override fun onOptionsItemSelected(item: MenuItem): Boolean {
         val context = this.context
         return when(item.itemId){
             R.id.profileOption -> {
                 Toast.makeText(context, "Profile Option Selected", Toast.LENGTH_SHORT).show()
                 true
             }
             R.id.settingsOption -> {
                 Toast.makeText(context, "Setting Option Selected", Toast.LENGTH_SHORT).show()
                 true
             }

             else -> super.onOptionsItemSelected(item)
         }

     }
}



