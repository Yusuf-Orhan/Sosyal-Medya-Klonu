package yusuf.orhan.instagram.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import yusuf.orhan.instagram.R
import yusuf.orhan.instagram.databinding.ChatRecyclerRow2Binding
import yusuf.orhan.instagram.databinding.ChatRecyclerRowBinding
import yusuf.orhan.instagram.databinding.RecyclreRowBinding

class Chat_Adapter(val arrayList: ArrayList<Chats>,val context: Context,val currentUserEmail : String) : RecyclerView.Adapter<Chat_Adapter.ChatHolder>() {
    class ChatHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textView : TextView = itemView.findViewById(R.id.textView_Message)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        if (viewType == 1){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_recycler_row,parent,false)
            return ChatHolder(view)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_recycler_row2,parent,false)
            return ChatHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        holder.textView.setText(arrayList[position].message)
    }

    override fun getItemViewType(position: Int): Int {
        val chat = arrayList.get(position)
        if (chat.useremail.equals(currentUserEmail)){
            return 1
        }else{
            return 2
        }
    }
}