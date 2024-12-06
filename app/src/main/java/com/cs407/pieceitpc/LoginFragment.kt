package com.cs407.pieceitpc

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import java.security.MessageDigest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginFragment(
    private val injectedUserViewModel: UserViewModel? = null // For testing only
) : Fragment() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var errorTextView: TextView

    private lateinit var userViewModel: UserViewModel

    //anisha
    private val viewModel2: UserViewModel by activityViewModels()




    private lateinit var userPasswdKV: SharedPreferences
    //private lateinit var noteDB: NoteDatabase

    //used for log in
    private lateinit var auth: FirebaseAuth


    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,

    ): View {
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar)

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp() // Navigate back
        }

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
            val enteredUserName = usernameEditText.text.toString().trim()
            val enteredPass = passwordEditText.text.toString().trim()

            // Check validity of user input
            if (enteredUserName.isEmpty() || enteredPass.isEmpty()) {
                errorTextView.text = "Email and password cannot be empty."
                errorTextView.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(enteredUserName).matches()) {
                errorTextView.text = "Please enter a valid email address."
                errorTextView.visibility = View.VISIBLE
                return@setOnClickListener
            }

            // Attempt Firebase authentication
            auth.signInWithEmailAndPassword(enteredUserName, enteredPass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null) {
                            Log.d("Login", "User ID: ${user.uid}, Email: ${user.email}")
                            Toast.makeText(requireContext(), "Welcome back, ${user.email}!", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.home_screen)
                            viewModel2.setLoginUser(enteredUserName)
                        } else {
                            Log.e("LoginError", "FirebaseAuth currentUser is null after successful sign-in.")
                            errorTextView.text = "Unexpected error occurred. Please try again."
                            errorTextView.visibility = View.VISIBLE
                        }
                    } else {
                        val exceptionMessage = when (task.exception) {
                            is FirebaseAuthInvalidCredentialsException -> "Invalid password. Please try again."
                            is FirebaseAuthInvalidUserException -> "No account found with this email."
                            else -> task.exception?.message ?: "Authentication failed."
                        }
                        errorTextView.text = exceptionMessage
                        errorTextView.visibility = View.VISIBLE
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
