package com.example.loginsignin

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.loginsignin.databinding.FragmentLogInBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class LogInFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding:FragmentLogInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth=FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if(currentUser!=null){
            findNavController().navigate(R.id.action_logInFragment_to_profileFragment)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentLogInBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val emailEditText =binding.editTextEmail
        val passWordEditText = binding.editTextPassword
        binding.LogInButton.setOnClickListener {
            hideKeyboard(it)
            val emailText = emailEditText.text.toString()
            val passWordText = passWordEditText.text.toString()
            var flag=true
            if(emailText.isEmpty()){
                emailEditText.error="Please enter Email address"
                emailEditText.requestFocus()
                flag=false
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
                emailEditText.error="Please enter valid Email address"
                emailEditText.requestFocus()
                flag=false
            }
            if(passWordText.isEmpty()){
                passWordEditText.error="Please enter your password"
                passWordEditText.requestFocus()
                flag=false
            }
            if(flag){
                activity?.let { it1 ->
                    auth.signInWithEmailAndPassword(emailText, passWordText)
                        .addOnCompleteListener(it1) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                findNavController().navigate(R.id.action_logInFragment_to_profileFragment)
                            } else {
                                // If sign in fails, display a message to the user.
                                val message = task.exception?.message
                                if (message != null) {
                                    Snackbar.make(view, message,
                                        Snackbar.LENGTH_SHORT).show()
                                }
                                else{
                                    Snackbar.make(view, "Authentication failed.",
                                        Snackbar.LENGTH_SHORT).show()
                                }
                            }
                        }
                }
            }
        }
        binding.signUpText.setOnClickListener {
            it.findNavController().navigate(R.id.action_logInFragment_to_signUpFragment2)
        }
        binding.forgetTextView.setOnClickListener {
            it.findNavController().navigate(R.id.action_logInFragment_to_resetFragment2)
        }
    }

    private fun hideKeyboard(view: View){
        try{
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        catch (e:Exception){

        }
    }
}