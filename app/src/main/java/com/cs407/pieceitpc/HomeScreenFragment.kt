package com.cs407.pieceitpc

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cs407.pieceitpc.databinding.FragmentHomeScreenBinding


 class HomeScreenFragment : Fragment() {
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)

     }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_screen, container, false)
    }
     override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
         inflater.inflate(R.menu.profile_menu, menu)
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



