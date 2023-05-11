package yusuf.orhan.instagram.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import yusuf.orhan.instagram.Model.My_Posts
import yusuf.orhan.instagram.Model.Posts
import yusuf.orhan.instagram.databinding.ProfileRecyclerRowBinding

class ProfileAdapter(val arrayList: ArrayList<My_Posts>,val context : Context) : RecyclerView.Adapter<ProfileAdapter.Profile_View_Holder>() {
    class Profile_View_Holder(val binding : ProfileRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Profile_View_Holder {
        val binding = ProfileRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Profile_View_Holder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: Profile_View_Holder, position: Int) {
        Glide.with(context).load(arrayList[position].download_uri).into(holder.binding.imageView)
    }
}