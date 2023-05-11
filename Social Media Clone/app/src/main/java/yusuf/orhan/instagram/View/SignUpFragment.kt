package yusuf.orhan.instagram.View

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.viewmodel.CreationExtras
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import yusuf.orhan.instagram.MainActivity
import yusuf.orhan.instagram.R
import yusuf.orhan.instagram.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    lateinit var binding : FragmentSignUpBinding
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        firebaseAuth = Firebase.auth
        firestore = Firebase.firestore
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signUnButton.setOnClickListener {
            val email = binding.emailText.text.toString()
            val password = binding.passwordText.text.toString()
            val username = binding.userNameText.text.toString()
            if (email.isEmpty() || password.isEmpty() || username.isEmpty()){
                Toast.makeText(requireContext(),"Lütfen Boş Alanları Doldurun",Toast.LENGTH_SHORT).show()
            }else{
                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        val map = hashMapOf<String,String>()
                        map.put("email",email)
                        map.put("password",password)
                        map.put("username", binding.userNameText.text.toString())
                        firestore.collection("Users").add(map).addOnSuccessListener {
                            val intent = Intent(requireActivity().applicationContext,MainActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        }.addOnFailureListener { e->
                            Toast.makeText(requireContext(),e.message,Toast.LENGTH_SHORT).show()
                        }
                    }
                }.addOnFailureListener{e ->
                    Toast.makeText(requireActivity().applicationContext,"${e.message}",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}