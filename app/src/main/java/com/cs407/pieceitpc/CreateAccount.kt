package com.cs407.pieceitpc

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateAccount.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateAccount(private val injectedUserViewModel: UserViewModel? = null // For testing only)
    ): Fragment() {
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var errorTextView: TextView

    private lateinit var userViewModel: UserViewModel
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        //noteDB = NoteDatabase.getDatabase(requireContext())

        //TODO: does this go here?
        //auth = FirebaseAuth.getInstance()

        //TODO: get email
        usernameEditText = view.findViewById(R.id.usernameEditText)
        //TODO: get password
        passwordEditText = view.findViewById(R.id.passwordEditText)
        //TODO: log in button
        loginButton = view.findViewById(R.id.loginButton)
        errorTextView = view.findViewById(R.id.errorTextView)

        userViewModel = if (injectedUserViewModel != null) {
            injectedUserViewModel
        } else {
            // TODO - Use ViewModelProvider to init UserViewModel
            ViewModelProvider(requireActivity())[UserViewModel::class.java]
            UserViewModel()
        }

        // TODO - Get shared preferences from using R.string.userPasswdKV as the name
        val context = requireContext()
        //var sharedPreferences = context.getSharedPreferences(
        // getString(R.string.userPasswdKV), Context.MODE_PRIVATE)

        return view
        // Inflate the layout for this fragment
        //val view = inflater.inflate(R.layout.fragment_create_account, container, false)
        /**
        val continueButton : Button
        continueButton = view.findViewById(R.id.continueButton)

        continueButton.setOnClickListener {
        findNavController().navigate(R.id.action_continue_to_homeScreen)
        }

        return view
         **/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //TODO: does this go here?
        auth = FirebaseAuth.getInstance()

        usernameEditText.doAfterTextChanged {
            errorTextView.visibility = View.GONE
        }

        passwordEditText.doAfterTextChanged {
            errorTextView.visibility = View.GONE
        }

        // Set the login button click action
        loginButton.setOnClickListener {

            // TODO: Get the entered username and password from EditText fields
            val enteredUserName = usernameEditText.text.toString()

            val enteredPass = passwordEditText.text.toString()
            if (enteredPass.isNotEmpty() && enteredUserName.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(enteredUserName).matches() && enteredPass.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(enteredUserName, enteredPass)
                //Log.i("INFO", "after adding to database: "+auth.currentUser?.metadata?.creationTimestamp)
                errorTextView.text = "Sign-Up successful!"
                //send user to home screen
                findNavController().navigate(R.id.home_screen)
                Toast.makeText(requireContext(), "Welcome to PieceItPC, let's get building!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}