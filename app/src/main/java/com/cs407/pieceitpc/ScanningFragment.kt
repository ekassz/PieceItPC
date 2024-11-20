package com.cs407.pieceitpc

//import com.cs407.lab5_milestone.data.NoteDatabase
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.ml.vision.common.FirebaseVisionImage


//import com.cs407.lab5_milestone.data.User

class ScanningFragment(
    private val injectedUserViewModel: UserViewModel? = null // For testing only
) : Fragment() {

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,

        ): View {
        val view = inflater.inflate(R.layout.scanning_screen, container, false)
        //val image = FirebaseVisionImage.fromBitmap(placeholder)
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        // Create a new Canvas object using the Bitmap
        val canvas = Canvas(bitmap)
        // Draw the View into the Canvas
        view.draw(canvas)


        val image = FirebaseVisionImage.fromBitmap(bitmap)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


    }

    private class YourImageAnalyzer : ImageAnalysis.Analyzer {

        override fun analyze(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                // Pass image to an ML Kit Vision API
                // ...
            }
        }
    }

}