package yusuf.orhan.instagram.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import yusuf.orhan.instagram.Model.Posts
import yusuf.orhan.instagram.Singleton
import yusuf.orhan.instagram.View.MainFragmentDirections
import yusuf.orhan.instagram.databinding.RecyclerRowBinding
import java.util.BitSet

class PostAdapter(val postList : ArrayList<Posts>,val context : Context) : RecyclerView.Adapter<PostAdapter.MyHolder>() {
     class MyHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){

     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val v_holder = MyHolder(binding)
        return v_holder
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.binding.textviewEmail.setText(postList[position].user_email)
        holder.binding.textviewComment.setText(postList[position].comment)
        val downloaduri = postList[position].download_uri
        Glide.with(context).load(downloaduri).into(holder.binding.imageView4)
        holder.itemView.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToDetailsFragment()
            Navigation.findNavController(holder.binding.root).navigate(action)
            Singleton.selected_Post = postList[position]
        }

        //Picasso.get().load(downloaduri).fit().centerInside().into(holder.binding.imageView4)
        //Picasso.get().load(downloaduri).fit().centerInside().rotate(270f).into(holder.binding.imageView4)
    }
}