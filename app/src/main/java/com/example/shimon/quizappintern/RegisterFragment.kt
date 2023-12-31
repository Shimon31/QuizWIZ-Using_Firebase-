package com.example.shimon.quizappintern

import android.content.res.Configuration
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.shimon.quizappintern.base.BaseFragment
import com.example.shimon.quizappintern.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth


class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.signUpBTN.setOnClickListener {

            var email = binding.emailET.text.toString().trim()
            var password = binding.passET.text.toString().trim()
            val name = binding.nameET.text.toString().trim()

            if (isEmailValid(email)&&isPasswordValid(password)){

                registerUser(name,email,password)
            }else
            {
                Toast.makeText(requireContext(), "Invalid Email and Password", Toast.LENGTH_SHORT).show()
            }



        }

        binding.loginTV.setOnClickListener {

            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

        }

        val textColor = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> R.color.black
            else -> R.color.black
        }
        val hintColor = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> R.color.black
            else -> R.color.black
        }
//for editText
        binding.emailET.setTextColor(ContextCompat.getColor(requireContext(), textColor))
        binding.nameET.setTextColor(ContextCompat.getColor(requireContext(), textColor))
        binding.passET.setTextColor(ContextCompat.getColor(requireContext(), textColor))

    }

    private fun registerUser(name: String, email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                // Navigate to the login page upon successful registration
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                Toast.makeText(requireContext(), "Registration Successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
//        val passwordRegex =
//            Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")
        return password.length >= 6
    }

}