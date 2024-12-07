package com.cs407.pieceitpc

//import android.R
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.database.annotations.Nullable
import com.google.firebase.database.core.Context
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions


class ScanPart : Fragment() {
    private lateinit var imageHolder: ImageView
    private lateinit var textOutput: TextView
    private lateinit var scanButton: Button
    val EXTRA_INFO: String = "default"
    private var btnCapture: Button? = null
    private var imgCapture: ImageView? = null
    private val Image_Capture_Code: Int = 1
    private val REQUEST_CAMERA_PERMISSIONS = 100
    private val IMAGE_CAPTURE_CODE = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissions();

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //show back button
        // Set up the back button functionality
        setupBackNavigation()
        // Inflate the layout for this fragment
//        launchCamera()
        val view: View = inflater.inflate(R.layout.fragment_scan_parts, container, false)
        imgCapture = view.findViewById<View>(R.id.buildImage) as ImageView
        btnCapture?.setOnClickListener(View.OnClickListener {
            val cInt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cInt, Image_Capture_Code)
        })
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

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val context = this.context
        return when(item.itemId){
            R.id.logoutNav -> {
                FirebaseAuth.getInstance().signOut()
                findNavController().navigate(R.id.action_scan_to_loginOrGuest)
                Toast.makeText(context, "Logout Successful", Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageHolder = view.findViewById(R.id.buildImage)
        textOutput = view.findViewById(R.id.textOutput)
        scanButton = view.findViewById(R.id.scanButton)
        textOutput.showSoftInputOnFocus = false
        textOutput.isFocusable = false
        scanButton = view.findViewById<View>(R.id.scanButton) as Button
        scanButton.setOnClickListener { onLabel(view) }

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)

        // Set up the toolbar as the fragment's ActionBar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        // Set up the back button functionality
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp() // Navigate back to the previous fragment
        }
    }

    private fun toTextBox(label: String, value: Any) {
        textOutput.append("$label: $value\n")
    }

    fun onLabel(view: View) {
        // TODO: Implement the Basic Setup For Label Recognition
        val bitmap = (imageHolder.drawable as BitmapDrawable).bitmap
        val image = InputImage.fromBitmap(bitmap, 0)
        val options = ImageLabelerOptions.DEFAULT_OPTIONS
        val labeler: ImageLabeler = ImageLabeling.getClient(options)

        // TODO: Add Listeners for Label detection process
        labeler.process(image)
            .addOnSuccessListener { labels ->
                for (l in labels) {
                    toTextBox("Here's what I found", l.text)
                }
            }
            .addOnFailureListener {
                toTextBox(getString(R.string.error),getString(R.string.label_recognition_error))
            }
    }

    private fun checkPermissions() {
        val permission = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSIONS)
        } else {
            launchCamera()
        }
    }

    private fun launchCamera() {
         //else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, IMAGE_CAPTURE_CODE)
        //}
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            launchCamera()
            //val cIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                //startActivityForResult(cIntent, IMAGE_CAPTURE_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == Activity.RESULT_OK) {
            val bitmap = data?.extras?.get("data") as? Bitmap
            imageHolder.setImageBitmap(bitmap)
        }
    }


}
