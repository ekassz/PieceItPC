package com.cs407.pieceitpc

import android.os.Bundle
import android.util.Log
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
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.fragment.app.viewModels


class HomeScreenFragment : Fragment() {

    private lateinit var cardRecyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter
    private lateinit var startNewBuild: Button
    private lateinit var savedContent: Button
    private lateinit var buildTuts: Button
    private lateinit var scan: ImageView
    val viewModel: UserViewModel by activityViewModels()

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

        val db = Firebase.firestore
        db.collection("pcBuilds")
            .orderBy("timeMS", Query.Direction.DESCENDING)
            .limit(3)
            .get()
            .addOnSuccessListener { document ->
                val builds = mutableListOf<CardItem>()
                for (doc in document) {
                    val data = doc.data
                    builds.add(CardItem(
                        id = doc.id,
                        imageResId = R.drawable.placeholder.toString(),
                        title = data["title"] as? String ?: "Untitled",
                        description = data["summary"] as? String ?: "No description",
                        author = data["author"] as? String ?: "Unknown"
                    ))
                }
                cardAdapter = CardAdapter(builds, this, viewModel)
                cardRecyclerView.adapter = cardAdapter
            }
            .addOnFailureListener { exception ->
                Log.e("FIREBASERROR", "error getting pc builds " + exception)
            }

        //Sample Hard-Coded Card Item Data
//        val sampleBuilds = getSampleBuilds()


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
                            Log.d("TTTTTT", "ERROR newdoc put " + id)
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
                                    Log.d("TTTTTT", "ERROR existing doc update " + doc.id)
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
