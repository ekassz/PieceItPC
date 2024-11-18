package com.cs407.pieceitpc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginOrGuest.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginOrGuest : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_login_or_guest, container, false)
        val continueButton : Button
        val createAccount : Button
        continueButton = view.findViewById(R.id.continueButton)
        createAccount = view.findViewById(R.id.createAccountButton)

        continueButton.setOnClickListener {
            findNavController().navigate(R.id.action_continue_to_loginFrag)
        }
        createAccount.setOnClickListener {
            findNavController().navigate(R.id.action_register_to_createAccount)
        }

        return view

    }


}