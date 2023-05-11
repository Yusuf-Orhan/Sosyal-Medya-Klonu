package yusuf.orhan.instagram.View

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import yusuf.orhan.instagram.R
import yusuf.orhan.instagram.Singleton
import yusuf.orhan.instagram.databinding.FragmentAddPostBinding
import java.util.UUID

class AddPostFragment : Fragment() {

    lateinit var binding : FragmentAddPostBinding
    lateinit var firestore: FirebaseFirestore
    lateinit var storage: FirebaseStorage
    lateinit var auth: FirebaseAuth
    lateinit var permissionLauncher : ActivityResultLauncher<String>
    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var selectedImage : Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAddPostBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = Firebase.storage
        binding.materialToolbar.setOnMenuItemClickListener(this::onOptionsItemSelected)
        register()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView2.setOnClickListener {
            requestControl()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        save()
        return super.onOptionsItemSelected(item)
    }
    fun save(){
        val reference = storage.reference
        val uuid = UUID.randomUUID()
        val imagename = "$uuid.jpg"
        val imageReference = reference.child("images").child(imagename)
        if (selectedImage != null){
            imageReference.putFile(selectedImage).addOnSuccessListener {
                val downUri = imageReference.downloadUrl.addOnSuccessListener{
                    val downloaduri = it.toString()
                    val comment = binding.commentText.text.toString()
                    val date = Timestamp.now()
                    val user_email = auth.currentUser?.email
                    val hashMap = HashMap<String,Any>()
                    if (user_email != null){
                        hashMap.put("downloaduri",downloaduri)
                        hashMap.put("comment",comment)
                        hashMap.put("user_email",user_email)
                        hashMap.put("date",date)
                        firestore.collection("Posts").document(uuid.toString()).set(hashMap).addOnSuccessListener {
                            Toast.makeText(requireContext(),"Post Paylaşıldı",Toast.LENGTH_SHORT).show()
                            Singleton.uuid = uuid.toString()
                            val action = AddPostFragmentDirections.actionAddPostFragmentToMainFragment()
                            Navigation.findNavController(binding.root).navigate(action)
                        }.addOnFailureListener {
                            Toast.makeText(requireContext(),"Post Paylaşılırken Hata Oluştu",Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(requireContext(),"Null Geldi",Toast.LENGTH_SHORT).show()
                    }

                }
                println(downUri)
                Toast.makeText(requireContext(),"İşlem Başarılı",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(requireActivity().applicationContext,it.message,Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(requireContext(),"Lütfen Bir Resim Seçin",Toast.LENGTH_SHORT).show()
        }
    }
    fun register(){
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){result ->
            if (result){
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                resultLauncher.launch(intentToGallery)
            }else{
                Toast.makeText(requireContext(),"Lütfen İzin Verin",Toast.LENGTH_SHORT).show()
            }
        }
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            if (result.resultCode == RESULT_OK){
                val intent = result.data
                if (intent != null){
                    val uri = intent.data
                    if (uri != null){
                        selectedImage = uri
                        binding.imageView2.setImageURI(uri)
                    }
                }
            }
        }
    }
    fun requestControl(){
        if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //Izın Iste
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }else{
            //Galeriye Git
            val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            resultLauncher.launch(intentToGallery)
        }
    }
}