package yusuf.orhan.instagram.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import yusuf.orhan.instagram.Model.Posts
import yusuf.orhan.instagram.R
import yusuf.orhan.instagram.Singleton
import yusuf.orhan.instagram.databinding.ActivityLoginBinding
import yusuf.orhan.instagram.databinding.ActivityMainBinding
import yusuf.orhan.instagram.databinding.FragmentDetailsBinding

class Details_Fragment : Fragment() {
    lateinit var binding : FragmentDetailsBinding
    lateinit var binding2: ActivityMainBinding
    lateinit var selected_post : Posts
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        binding2 = ActivityMainBinding.inflate(layoutInflater)
        selected_post = Singleton.selected_Post
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(requireActivity()).load(selected_post.download_uri).into(binding.imageView6)
        binding.textComment.setText(selected_post.comment)
        binding.textEmail.setText(selected_post.user_email)
        binding.gotoProfile.setOnClickListener {view ->
            val action  = Details_FragmentDirections.actionDetailsFragmentToProfileFragment()
            Navigation.findNavController(binding.root).navigate(action)
            Singleton.selected_email = selected_post.user_email
            Singleton.controller = "old"
        }
    }
}