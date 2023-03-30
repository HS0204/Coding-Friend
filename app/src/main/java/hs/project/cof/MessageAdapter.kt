package hs.project.cof

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hs.project.cof.base.ApplicationClass.Companion.SEND_BY_BOT
import hs.project.cof.base.ApplicationClass.Companion.SEND_BY_LINE
import hs.project.cof.base.ApplicationClass.Companion.SEND_BY_USER
import hs.project.cof.data.remote.model.Message
import hs.project.cof.databinding.ItemChatBotBinding
import hs.project.cof.databinding.ItemChatLineBinding
import hs.project.cof.databinding.ItemChatUserBinding

class MessageAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var messageList = listOf<Message>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        return when (viewType) {
            SEND_BY_USER -> {
                val view =
                    ItemChatUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                UserViewHolder(view)
            }
            SEND_BY_BOT -> {
                val view =
                    ItemChatBotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BotViewHolder(view)
            }
            else -> {
                val view =
                    ItemChatLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LineViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val curMsg = messageList[position]

        when (curMsg.sendBy) {
            SEND_BY_USER -> {
                (holder as UserViewHolder).bind(curMsg)
            }
            SEND_BY_BOT -> {
                (holder as BotViewHolder).bind(curMsg)
            }
            SEND_BY_LINE -> {
                (holder as LineViewHolder).bind(curMsg)
            }
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        return messageList[position].sendBy
    }

    fun setMessageList(newList: List<Message>) {
        messageList = newList
        notifyDataSetChanged()
    }

    inner class UserViewHolder(binding: ItemChatUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var userTxt = binding.itemMsgUserTv

        fun bind(item: Message) {
            userTxt.text = item.message
        }
    }

    inner class BotViewHolder(binding: ItemChatBotBinding) : RecyclerView.ViewHolder(binding.root) {
        var chatTxt = binding.itemMsgBotTv

        fun bind(item: Message) {
            chatTxt.text = item.message
        }
    }

    inner class LineViewHolder(binding: ItemChatLineBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var versionTxt = binding.itemMsgVersionTv

        fun bind(item: Message) {
            versionTxt.text = item.message
        }
    }

}