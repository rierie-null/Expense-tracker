package com.example.expensetracker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterFragment : Fragment(R.layout.fragment_register) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usernameInput = view.findViewById<TextInputEditText>(R.id.usernameInput)
        val emailInput = view.findViewById<TextInputEditText>(R.id.emailInput)
        val passwordInput = view.findViewById<TextInputEditText>(R.id.passwordInput)
        val ageInput = view.findViewById<TextInputEditText>(R.id.ageInput)
        val createButton = view.findViewById<Button>(R.id.createAccountButton)
        val loginLink = view.findViewById<TextView>(R.id.loginLink)

        createButton.setOnClickListener {
            val username = usernameInput.text?.toString()?.trim().orEmpty()
            val email = emailInput.text?.toString()?.trim().orEmpty()
            val password = passwordInput.text?.toString()?.trim().orEmpty()
            val ageText = ageInput.text?.toString()?.trim().orEmpty()

            if (username.isBlank() || email.isBlank() || password.isBlank() || ageText.isBlank()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(requireContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val age = ageText.toIntOrNull()
            if (age == null) {
                Toast.makeText(requireContext(), "Please enter a valid age", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            task.exception?.message ?: "Account creation failed",
                            Toast.LENGTH_LONG
                        ).show()
                        return@addOnCompleteListener
                    }

                    val user = FirebaseAuth.getInstance().currentUser
                    if (user == null) {
                        Toast.makeText(requireContext(), "User session unavailable", Toast.LENGTH_SHORT).show()
                        return@addOnCompleteListener
                    }

                    val profile = hashMapOf(
                        "username" to username,
                        "email" to email,
                        "age" to age
                    )

                    FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(user.uid)
                        .set(profile)
                        .addOnSuccessListener {
                            user.sendEmailVerification()
                                .addOnCompleteListener { verificationTask ->
                                    FirebaseAuth.getInstance().signOut()

                                    if (verificationTask.isSuccessful) {
                                        Toast.makeText(
                                            requireContext(),
                                            "Verification email sent. Please check your inbox and confirm your account.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        (requireActivity() as AuthActivity).showLogin()
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            verificationTask.exception?.message ?: "Could not send verification email",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(
                                requireContext(),
                                exception.message ?: "Could not save profile",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }
        }

        loginLink.setOnClickListener {
            (requireActivity() as AuthActivity).showLogin()
        }
    }
}
