package yusuf.orhan.instagram.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import yusuf.orhan.instagram.Model.User
import yusuf.orhan.instagram.View.Message_Activity
import yusuf.orhan.instagram.databinding.RecyclreRowBinding

class User_Adapter(val list: ArrayList<User>,val context : Context,val currentUserEmail : String,val gidilenEmail : String) : RecyclerView.Adapter<User_Adapter.MyHolder>(){

    class MyHolder(val binding: RecyclreRowBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding = RecyclreRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  MyHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.binding.textView.setText(list.get(position).username)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, Message_Activity::class.java)
            intent.putExtra("emailCurrentUser",currentUserEmail)
            intent.putExtra("gidilenEmail", list.get(position).email)
            holder.itemView.context.startActivity(intent)
        }
    }
}