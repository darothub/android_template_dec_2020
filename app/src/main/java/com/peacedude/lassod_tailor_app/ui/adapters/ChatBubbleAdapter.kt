package com.peacedude.lassod_tailor_app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peacedude.lassod_tailor_app.R
import com.peacedude.lassod_tailor_app.ui.CHATRECEIVER
import com.peacedude.lassod_tailor_app.ui.ChatMessage
import com.peacedude.lassod_tailor_app.ui.CHATSENDER
import com.vanniktech.emoji.EmojiTextView

class ChatBubbleAdapter(var list: List<ChatMessage>, var listener:(ChatMessage)->Unit):RecyclerView.Adapter<ChatBubbleAdapter.ChatBubbleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatBubbleViewHolder {
        if(viewType == CHATSENDER){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.single_chat_customer_item, parent, false)
            return ChatBubbleViewHolder(view)
        }
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_chat_outgoing_item, parent, false)
        return ChatBubbleViewHolder(view)

    }

    override fun onBindViewHolder(holder: ChatBubbleViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item, listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setChatMessageList(list: List<ChatMessage?>?){
        this.list = list as List<ChatMessage>
        notifyDataSetChanged()
    }
    fun getMessageAt(position: Int):ChatMessage{
        return list.get(position)
    }


    override fun getItemViewType(position: Int): Int {
        val currentItem = list[position]
        if(currentItem.sender == CHATSENDER){
            return CHATSENDER
        }
        return CHATRECEIVER
    }

    class ChatBubbleViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val messageTv = itemView.findViewById(R.id.single_chat_message_tv)?:itemView.findViewById<EmojiTextView>(R.id.single_chat_outgoing_message_tv)

        fun bind(chatMessage: ChatMessage, listener: (ChatMessage) -> Unit){
            messageTv.text = chatMessage.message
            itemView.setOnClickListener {
                listener(chatMessage)
            }
        }
    }
}