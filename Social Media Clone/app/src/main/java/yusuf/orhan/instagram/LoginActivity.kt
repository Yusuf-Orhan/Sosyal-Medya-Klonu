package yusuf.orhan.instagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import yusuf.orhan.instagram.View.LoginFragment
import yusuf.orhan.instagram.View.SignUpFragment
import yusuf.orhan.instagram.databinding.ActivityLoginBinding
class LoginActivity : AppCompatActivity() {
    lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val firebaseAuth = Firebase.auth
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null){
            val intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        val tablist = arrayListOf<String>("Login","Sign Up")
        val adapter = ViewPagerAdapter(supportFragmentManager,lifecycle)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.loginTabLayout,binding.viewPager){tab,position ->
            tab.text = tablist[position]
        }.attach()
    }
    private class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle){

        override fun getItemCount() = 2

        override fun createFragment(position: Int): Fragment = when(position){
            0 -> LoginFragment()
            1 -> SignUpFragment()
            else -> SignUpFragment()
        }

    }
}