package yusuf.orhan.instagram.View

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import yusuf.orhan.instagram.Adapter.ProfileAdapter
import yusuf.orhan.instagram.LoginActivity
import yusuf.orhan.instagram.Model.My_Posts
import yusuf.orhan.instagram.Model.Posts
import yusuf.orhan.instagram.R
import yusuf.orhan.instagram.Singleton
import yusuf.orhan.instagram.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    lateinit var binding : FragmentProfileBinding
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    val postList = ArrayList<My_Posts>()
    lateinit var adapter : ProfileAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentProfileBinding.inflate(layoutInflater)
        firebaseAuth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        getData()
        adapter = ProfileAdapter(postList,requireContext())
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(),3)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageFilterButton.setOnClickListener {
            signOut()
        }
    }
    fun getData(){
        if (Singleton.selected_email != null){
            firestore.collection("Posts").whereEqualTo("user_email",Singleton.selected_email).addSnapshotListener { value, error ->
                if (error != null){
                    Toast.makeText(requireContext(),error.message,Toast.LENGTH_LONG).show()
                    println("Error : ${error.message}")
                }else{
                    if (value != null){
                        if (!value.isEmpty){
                            value.documents.forEach {
                                val map = it.data
                                if (map != null){
                                    val downloaduri = map.get("downloaduri")  as String
                                    val user_email = map.get("user_email") as String
                                    binding.userNameText.setText(user_email)
                                    val myPosts = My_Posts(downloaduri)
                                    postList.add(myPosts)
                                }
                            }
                            adapter.notifyDataSetChanged()
                            Singleton.selected_email = null

                        }
                    }
                }
            }
        }else{
            firestore.collection("Posts").whereEqualTo("user_email",firebaseAuth.currentUser?.email).addSnapshotListener { value, error ->
                if (error != null){
                    Toast.makeText(requireContext(),error.message,Toast.LENGTH_LONG).show()
                    println("Error : ${error.message}")
                }else{
                    if (value != null){
                        if (!value.isEmpty){
                            value.documents.forEach {
                                val map = it.data
                                if (map != null){
                                    val downloaduri = map.get("downloaduri")  as String
                                    val user_email = map.get("user_email") as String
                                    binding.userNameText.setText(user_email)
                                    val myPosts = My_Posts(downloaduri)
                                    postList.add(myPosts)
                                }
                            }
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }

    }
    private fun signOut(){
        firebaseAuth.signOut().run {
            val intent = Intent(requireActivity().applicationContext,LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }
}