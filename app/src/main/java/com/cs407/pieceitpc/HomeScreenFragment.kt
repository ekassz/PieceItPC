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
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot


class HomeScreenFragment : Fragment(), AddToSavedContent {

    private lateinit var cardRecyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter
    private lateinit var startNewBuild: Button
    private lateinit var savedContent: Button
    private lateinit var buildTuts: Button
    private lateinit var scan: ImageView
    override val viewModel: UserViewModel by activityViewModels()

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
            .addOnSuccessListener { documents ->
                val builds = mutableListOf<CardItem>()
                val tasks = mutableListOf<Task<DocumentSnapshot>>()
                for (doc in documents) {
                    val data = doc.data
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
                    cardAdapter = CardAdapter(builds, this, null, viewModel)
                    cardRecyclerView.adapter = cardAdapter
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FIREBASERROR", "Error getting pc builds: $exception")
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
            findNavController().navigate(R.id.action_home_screen_to_PCTutorialHighlights)
        }
        savedContent.setOnClickListener{
            findNavController().navigate(R.id.savedContent)
        }
        scan.setOnClickListener {
            findNavController().navigate(R.id.scan)
        }

        return view
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
                findNavController().navigate(R.id.action_home_screen_to_loginOrGuest)
                Toast.makeText(context, "Logout Successful", Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun addToSavedContent(id: String): Boolean {
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
                            Log.d("FFFFF", "ERROR newdoc put " + id)
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
