package yusuf.orhan.instagram.View

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.trackPipAnimationHintView
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.grpc.internal.DnsNameResolver.SrvRecord
import yusuf.orhan.instagram.Adapter.Chat_Adapter
import yusuf.orhan.instagram.Adapter.Chats
import yusuf.orhan.instagram.databinding.ActivityMessageBinding
import java.sql.Time
import java.util.UUID

class Message_Activity : AppCompatActivity() {
    lateinit var binding : ActivityMessageBinding
    lateinit var firestore: FirebaseFirestore
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var currentEmail : String
    lateinit var gidilenEmail : String
    lateinit var arrayList: ArrayList<Chats>
    lateinit var adapter: Chat_Adapter
    lateinit var name : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = Firebase.auth
        firestore = Firebase.firestore
        var intent = getIntent()
        currentEmail = firebaseAuth.currentUser?.email.toString()
        gidilenEmail = intent.getStringExtra("gidilenEmail").toString()
        arrayList = ArrayList()
        Toast.makeText(this,"${firebaseAuth.currentUser?.email} Kullaıcısı $gidilenEmail kullanıcısına mesaj gönderiyor",Toast.LENGTH_LONG).show()
        adapter = Chat_Adapter(arrayList,this,currentEmail)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        getData()
        getUserName()

    }
    fun sendMessage(view: View){
        if (binding.editTextTextPersonName.text.isEmpty()){
            showToast("Lütfen Mesajınızı Girin")
        }else{
            val channelId = UUID.randomUUID()
            arrayList.clear()
            val message = binding.editTextTextPersonName.text.toString()
            val map = hashMapOf<String,Any>()
            map.put("gonderen",currentEmail)
            map.put("alan",gidilenEmail)
            map.put("message",message)
            map.put("date",Timestamp.now())
            map.put("channelId",channelId.toString())

            try{
                firestore.collection("Messages").document(channelId.toString()).set(map).addOnSuccessListener {
                    showToast("İs Succesfull")
                    binding.editTextTextPersonName.setText("")
                    adapter.notifyDataSetChanged()
                }.addOnFailureListener {
                    showToast("Error")
                }
            }catch (e : Exception){
                showToast(e.localizedMessage)
                println(e.localizedMessage)
                println(e.stackTrace)
            }

        }
    }
    fun getUserName(){
        firestore.collection("Users").whereEqualTo("email",gidilenEmail).addSnapshotListener { value, error ->
            if (error != null){
                showToast("${error.message}")
            }else{
                 if (value != null){
                     if(!value.isEmpty){
                         value.documents.forEach {
                             val map =  it.data
                             if (map != null){
                                 name = map.get("username") as String
                             }
                         }
                         binding.toolbar.userNameText.setText(name)
                     }
                 }
            }
        }
    }

    fun showToast(message : String){
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }
    fun loadData(){
        val query1 = firestore.collection("Messages").whereEqualTo("gonderen",currentEmail).whereEqualTo("alan",gidilenEmail).orderBy("date",Query.Direction.ASCENDING).get()
        val query2 = firestore.collection("Messages").whereEqualTo("gonderen",gidilenEmail).whereEqualTo("alan",currentEmail).orderBy("date",Query.Direction.ASCENDING).get()
        Tasks.whenAllSuccess<QuerySnapshot>(query1,query2).addOnCompleteListener {task ->
            task.addOnSuccessListener {querySnapshots ->
                querySnapshots.forEach {
                    it.documents.forEach {
                        val map = it.data
                        if(map != null){
                            val message = map.get("message") as String
                            val gonderen = map.get("gonderen") as String
                            val alan = map.get("alan") as String
                            val chats = Chats(message,gonderen,alan)
                            arrayList.add(chats)
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }.addOnFailureListener {
            showToast("${it.message}")
            println("Error ${it.localizedMessage}")
        }
    }
    override fun onPause() {
        name = "null"
        super.onPause()
    }
    fun getData(){
        firestore.collection("Messages").orderBy("date",Query.Direction.ASCENDING).addSnapshotListener { value, error ->
            if (error != null){
                showToast("${error.localizedMessage}")
            }else{
                arrayList.clear()
                if (value != null){
                    if (!value.isEmpty){
                        value.documents.forEach {
                            val map = it.data
                            if (map != null){
                                val message = map.get("message") as String
                                val gonderen = map.get("gonderen") as String
                                val alan = map.get("alan") as String
                                if (gonderen == gidilenEmail && alan == currentEmail || gonderen == currentEmail && alan == gidilenEmail ){
                                    val chats = Chats(message,gonderen,alan)
                                    arrayList.add(chats)
                                }

                            }
                            adapter.notifyDataSetChanged()
                        }

                    }else{
                        println("Second Null")
                    }
                }else{
                    showToast("First Null")
                }
            }
        }
    }
}