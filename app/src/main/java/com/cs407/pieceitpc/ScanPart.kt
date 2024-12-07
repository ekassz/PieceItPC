package com.cs407.pieceitpc

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions


class ScanPart : Fragment() {
    private lateinit var imageHolder: ImageView
    private lateinit var textOutput: TextView
    private lateinit var scanButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //show back button
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_parts, container, false)

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



        scanButton.setOnClickListener { onLabel(view) }
    }
    //might not be right but starting here
    //findNavController().popBackStack()

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
                    toTextBox("Here's what I found: ", l.text)
                }
            }
            .addOnFailureListener {
                toTextBox(getString(R.string.error),getString(R.string.label_recognition_error))
            }
    }

    /*
    fun launchCamera(view: View, context:Context) {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSIONS)
        } else {
            val cIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cIntent, IMAGE_CAPTURE_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val cIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cIntent, IMAGE_CAPTURE_CODE)
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





     */

}

/*
package com.cs407.lab9app



import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions


class ScanPart : AppCompatActivity() {
    private var imageIndex = 1
    private val maxIndex = 6
    private lateinit var imageHolder: ImageView
    private lateinit var textOutput: TextView

    companion object {
        private const val REQUEST_CAMERA_PERMISSIONS = 1
        private const val IMAGE_CAPTURE_CODE = 2
    }

    @SuppressLint("MissingInflatedId")
    // not sure why we need this because the textOutput is here?
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_scan_parts)
        imageHolder = findViewById(R.id.buildImage)
        textOutput = findViewById(R.id.textOutput)
        textOutput.showSoftInputOnFocus = false
        textOutput.isFocusable = false
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
                    toTextBox("Item", l.text)
                    toTextBox("Index", l.index)
                    toTextBox("Confidence", l.confidence)
                    toTextBox("Finished", "Object Labeling Complete\n--------\n")
                }
            }
            .addOnFailureListener {
                toTextBox(getString(R.string.error),getString(R.string.label_recognition_error))
            }
    }

    private fun toTextBox(label: String, value: Any) {
        textOutput.append("$label: $value\n")
    }

    /*
    private fun addImage(x: Float, y: Float, bounds: Rect, angle: Float, fileName: String) {
        val img = ImageView(this)
        val resID = resources.getIdentifier(fileName, "drawable", packageName)
        img.setImageResource(resID)
        val frame: FrameLayout = findViewById(R.id.frame)
        frame.addView(img)
        img.layoutParams.apply {
            height = bounds.height()
            width = bounds.width()
        }
        img.x = x - (bounds.width() / 2)
        img.y = y - (bounds.height() / 2)
        img.rotation = angle
        img.bringToFront()
    }

     */

    fun launchCamera(view: View) {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSIONS)
        } else {
            val cIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cIntent, IMAGE_CAPTURE_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val cIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cIntent, IMAGE_CAPTURE_CODE)
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


 */