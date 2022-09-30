package com.example.loginsignin

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.example.loginsignin.databinding.FragmentResetBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class ResetFragment : Fragment() {
    private lateinit var auth:FirebaseAuth
    private lateinit var binding: FragmentResetBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentResetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val editTextEmail = binding.editTextEmailReset
        binding.ResetButton.setOnClickListener { it ->
            hideKeyboard(it)
            val emailText=editTextEmail.text.toString()
            var flag=true
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
            if(flag){
                auth.sendPasswordResetEmail(emailText).addOnCompleteListener { task->
                    if(task.isSuccessful){
                        Snackbar.make(view,"Check your Email to reset password",Snackbar.LENGTH_SHORT).show()
                    }
                    else{
                        val message = task.exception?.message
                        if(message!=null){
                            Snackbar.make(view,message,Snackbar.LENGTH_SHORT).show()
                        }
                        else{
                            Snackbar.make(view,"Something went wrong, try again",Snackbar.LENGTH_SHORT).show()
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
}