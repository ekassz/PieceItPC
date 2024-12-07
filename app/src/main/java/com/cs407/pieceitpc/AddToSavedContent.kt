package com.cs407.pieceitpc

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

interface AddToSavedContent {
    val viewModel: UserViewModel
        fun addToSavedContent(id: String): Boolean{
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