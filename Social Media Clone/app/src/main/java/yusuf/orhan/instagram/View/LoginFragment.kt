package yusuf.orhan.instagram.View

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import yusuf.orhan.instagram.MainActivity
import yusuf.orhan.instagram.R
import yusuf.orhan.instagram.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var email : String
    lateinit var password : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLoginBinding.inflate(layoutInflater)
        firebaseAuth = Firebase.auth

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginButton.setOnClickListener {view ->
            email = binding.emailText.text.toString()
            password = binding.passwordText.text.toString()
            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(requireContext(),"Boşlukları Doldurrun",Toast.LENGTH_SHORT).show()
            }else{
                if (password.length < 6){
                    Toast.makeText(requireContext(),"Parola En Az 6 Karakter Olamlıdır",Toast.LENGTH_SHORT).show()
                }else{
                    //Login
                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener { result ->
                        if (result.isSuccessful){
                            val intent = Intent(activity,MainActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        }
                    }.addOnFailureListener {error ->
                        Toast.makeText(requireContext(),"${error.message}",Toast.LENGTH_SHORT).show()
                    }

                }
            }

        }
    }

}