package hs.project.cof

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hs.project.cof.base.ApplicationClass.Companion.SEND_BY_USER
import hs.project.cof.data.remote.model.Message
import hs.project.cof.databinding.ItemChatBinding

class MessageAdapter(): RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    private var messageList = listOf<Message>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessageAdapter.ViewHolder {
        val view = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageAdapter.ViewHolder, position: Int) {
        val curMsg = messageList[position]

        // data setting
        if (curMsg.sendBy == SEND_BY_USER) {
            holder.chatContainer.visibility = View.GONE
            holder.userContainer.visibility = View.VISIBLE
            holder.userTxt.text = curMsg.message
        } else {
            holder.userContainer.visibility = View.GONE
            holder.chatContainer.visibility = View.VISIBLE
            holder.chatTxt.text = curMsg.message
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    fun setMessageList(newList: List<Message>) {
        messageList = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
        var chatContainer = binding.itemMsgChatContainer
        var chatTxt = binding.itemMsgChatTv
        var userContainer = binding.itemMsgUserContainer
        var userTxt = binding.itemMsgUserTv
    }
}