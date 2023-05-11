package yusuf.orhan.instagram.View

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.processNextEventInCurrentThread
import yusuf.orhan.instagram.Adapter.PostAdapter
import yusuf.orhan.instagram.Model.Posts
import yusuf.orhan.instagram.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    lateinit var binding : FragmentMainBinding
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    lateinit var adapter : PostAdapter
    val arraylist = ArrayList<Posts>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMainBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = Firebase.firestore
        println("On Create")
        loadData()
        adapter = PostAdapter(arraylist,requireContext())
        binding.recyclerview2.adapter = adapter
        binding.recyclerview2.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onResume() {
        super.onResume()
        println("On Resume")
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("On View Create")
    }
    fun loadData(){
        firestore.collection("Posts").addSnapshotListener { value, error ->
            if (error != null){
                Toast.makeText(requireContext(),"Error",Toast.LENGTH_SHORT).show()
            }else{
                if (value != null) {
                    if (!value.isEmpty) {
                        value.documents.forEach {
                            val map = it?.data
                            if (map != null) {
                                val comment = map.get("comment") as String
                                val downloaduri = map.get("downloaduri") as String
                                val user_email = map.get("user_email") as String
                                val post = Posts(user_email = user_email, comment = comment, download_uri = downloaduri)
                                arraylist.add(post)
                            }
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }
}