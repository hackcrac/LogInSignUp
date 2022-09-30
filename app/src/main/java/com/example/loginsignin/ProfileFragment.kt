package com.example.loginsignin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import com.example.loginsignin.databinding.FragmentProfileBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding:FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentProfileBinding.inflate(inflater,container,false)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val reference = FirebaseDatabase.getInstance().getReference("Users")
        val uid = currentUser!!.uid
        reference.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userProfile = snapshot.getValue(User::class.java)
                if(userProfile!=null){
                    binding.nameTextview.text=userProfile.name
                    binding.emailTextView.text=userProfile.email
                }
            }

            override fun onCancelled(error: DatabaseError) {
                view?.let {
                    Snackbar.make(
                        it, "Something went wrong.",
                        Snackbar.LENGTH_SHORT).show()
                }
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            auth.signOut()
            it.findNavController().navigate(R.id.action_profileFragment_to_logInFragment)
        }
    }

}