package yusuf.orhan.instagram.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import yusuf.orhan.instagram.Adapter.User_Adapter
import yusuf.orhan.instagram.Model.User
import yusuf.orhan.instagram.databinding.FragmentMessageBinding

class MessageFragment : Fragment() {
    lateinit var binding : FragmentMessageBinding
    lateinit var firestore: FirebaseFirestore
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var arrayList: ArrayList<User>
    lateinit var adapter : User_Adapter
    lateinit var gidilenEmail : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMessageBinding.inflate(layoutInflater)
        firestore = Firebase.firestore
        firebaseAuth = FirebaseAuth.getInstance()
        arrayList = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()

    }
    private fun getData(){
        firestore.collection("Users").whereNotEqualTo("email",firebaseAuth.currentUser?.email).addSnapshotListener { value, error ->
            if (error != null){
                Toast.makeText(requireContext(),"${error.message}",Toast.LENGTH_LONG).show()
            }else{
                if (value != null){6
                    if (!value.isEmpty){
                        value.documents.forEach {
                            val map = it.data
                            if (map != null){
                                val username = map.get("username") as String
                                gidilenEmail = map.get("email") as String
                                val user = User(username = username, email = gidilenEmail)
                                arrayList.add(user)
                            }
                            adapter = User_Adapter(arrayList,requireContext(),firebaseAuth.currentUser?.email.toString(),gidilenEmail.toString())
                            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                            binding.recyclerView.adapter = adapter
                            adapter.notifyDataSetChanged()
                        }
                    }else{
                        Toast.makeText(requireContext(),"Boş Geldi Kardeş",Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}