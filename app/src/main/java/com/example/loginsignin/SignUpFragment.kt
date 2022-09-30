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
import android.widget.Toast.makeText
import com.example.loginsignin.databinding.FragmentSignUpBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.make
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance();
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =FragmentSignUpBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editTextName = binding.editTextName
        val editTextEmail = binding.editTextEmailSignUp
        val editTextPassword = binding.editTextPasswordSignUp
        binding.SignUpButton.setOnClickListener {
            hideKeyboard(it)
            val emailText = editTextEmail.text.toString()
            val passWordText = editTextPassword.text.toString()
            val nameText = editTextName.text.toString()
            var flag=true
            if(nameText.isEmpty()){
                editTextName.error="Please enter your name"
                editTextName.requestFocus()
                flag=false
            }
            if(emailText.isEmpty()){
                editTextEmail.error="Please enter Email address"
                editTextEmail.requestFocus()
                flag=false
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
                editTextEmail.error="Please enter valid Email address"
                editTextEmail.requestFocus()
                flag=false
            }
            if(passWordText.length<6){
                editTextPassword.error="Minimum 6 characters allowed"
                editTextPassword.requestFocus()
                flag=false
            }

            if(flag){
                activity?.let { it1 ->
                    auth.createUserWithEmailAndPassword(emailText, passWordText)
                        .addOnCompleteListener(it1) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("SignUp", "createUserWithEmail:success")
                                val user:User = User(nameText,emailText)
                                val currentUser = auth.currentUser
                                FirebaseDatabase.getInstance().getReference("Users").child(currentUser!!.uid).setValue(user).addOnCompleteListener(
                                    OnCompleteListener {
                                        if(it.isSuccessful){
                                            Snackbar.make(view, "Account created Successfully.",
                                                Snackbar.LENGTH_SHORT).show()
                                            auth.signOut()
                                        }
                                        else{
                                            Snackbar.make(view, "Authentication failed.",
                                                Snackbar.LENGTH_SHORT).show()
                                        }
                                    })
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("SignUp", "createUserWithEmail:failure", task.exception)
                                val message = task.exception?.message
                                if(message!=null){
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
    }

    private fun hideKeyboard(view: View){
        try{
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        catch (e:Exception){

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        auth.signOut()
    }
}