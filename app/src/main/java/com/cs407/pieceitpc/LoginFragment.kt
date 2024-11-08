package com.cs407.pieceitpc

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
//import com.cs407.lab5_milestone.data.NoteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.findViewTreeViewModelStoreOwner
//import com.cs407.lab5_milestone.data.User

class LoginFragment(
    private val injectedUserViewModel: UserViewModel? = null // For testing only
) : Fragment() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var errorTextView: TextView

    private lateinit var userViewModel: UserViewModel

    private lateinit var userPasswdKV: SharedPreferences
    //private lateinit var noteDB: NoteDatabase

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,

    ): View {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        //noteDB = NoteDatabase.getDatabase(requireContext())


        usernameEditText = view.findViewById(R.id.usernameEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
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
        var sharedPreferences = context.getSharedPreferences(
            getString(R.string.userPasswdKV), Context.MODE_PRIVATE)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

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

            if (enteredPass == "" || enteredUserName == "") {
                errorTextView.visibility = View.VISIBLE
            } else {
                viewLifecycleOwner.lifecycleScope.launch {
                    val success = withContext(Dispatchers.IO) {
                        getUserPasswd(enteredUserName, enteredPass)
                    }

                    if (success) {
                        // TODO: Set the logged-in user in the ViewModel (store user info) (placeholder)
                        userViewModel.setUser(
                            UserState(
                                1
                            )
                        ) // You will implement this in UserViewModel

                        // TODO: Navigate to another fragment after successful login
                        findNavController().navigate(R.id.action_loginFragment_to_noteListFragment) // Example navigation action

                    } else {
                        // TODO: Show an error message if either username or password is empty


                        errorTextView.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private suspend fun getUserPasswd(
        name: String,
        passwdPlain: String
    ): Boolean {
        //noteDB = NoteDatabase.getDatabase(requireContext())
        // TODO: Hash the plain password using a secure hashing function
        val passwordHash = hash(passwdPlain)

        // TODO: Check if the user exists in SharedPreferences (using the username as the key)
        val context = requireContext()
        val sharedPreferences = context.getSharedPreferences(
            getString(R.string.userPasswdKV), Context.MODE_PRIVATE)

        if (sharedPreferences.contains(name)) {
            // TODO: Retrieve the stored password from SharedPreferences
            val passCheck = sharedPreferences.getString(name, "DEFAULT")

            // TODO: Compare the hashed password with the stored one and return false if they don't match
            if ( !passCheck.equals(passwordHash)) {
                return false
            }
            return true

        } else {
            // TODO: If the user doesn't exist in SharedPreferences, create a new user

            // TODO: Insert the new user into the Room database (implement this in your User DAO)
            /*
            noteDB.userDao().insert(
                User(
                    userId = 0,
                    userName = name
                )
            )

             */

            // TODO: Store the hashed password in SharedPreferences for future logins
            sharedPreferences.edit().putString(name, passwordHash).apply()

            return true
        }

        // TODO: Return true if the user login is successful or the user was newly created

        return false
    }

    private fun hash(input: String): String {
        return MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
            .fold("") { str, it -> str + "%02x".format(it) }
    }
}