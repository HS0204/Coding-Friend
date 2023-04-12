package hs.project.cof.presentation.view.chatting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ViewFlipper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hs.project.cof.R
import hs.project.cof.base.ApplicationClass.Companion.SendBy
import hs.project.cof.base.ApplicationClass.Companion.getViewType
import hs.project.cof.data.remote.model.Message
import hs.project.cof.databinding.ItemChatBotBinding
import hs.project.cof.databinding.ItemChatLineBinding
import hs.project.cof.databinding.ItemChatUserBinding

class MessageAdapter(val context: Context) : ListAdapter<Message, RecyclerView.ViewHolder>(
    DiffCallback
) {

    private var messageList = listOf<Message>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        return when (viewType) {
            getViewType(SendBy.USER) -> {
                val view =
                    ItemChatUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                UserViewHolder(view)
            }
            getViewType(SendBy.VERSION) -> {
                val view =
                    ItemChatLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LineViewHolder(view)
            }
            else -> {
                val view =
                    ItemChatBotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BotViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val curMsg = messageList[position]

        when (curMsg.sendBy) {
            getViewType(SendBy.USER) -> {
                (holder as UserViewHolder).bind(curMsg)
            }
            getViewType(SendBy.VERSION) -> {
                (holder as LineViewHolder).bind(curMsg)
            }
            else -> {
                (holder as BotViewHolder).bind(curMsg)
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

    val bounceAnim: Animation = AnimationUtils.loadAnimation(context, R.anim.bounce)

    inner class UserViewHolder(binding: ItemChatUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var userTxt = binding.itemMsgUserTv

        init {
            userTxt.startAnimation(bounceAnim)
        }

        fun bind(item: Message) {
            userTxt.text = item.message
        }
    }

    inner class BotViewHolder(binding: ItemChatBotBinding) : RecyclerView.ViewHolder(binding.root) {
        var chatTxt = binding.itemMsgBotTv
        private val typingIndicator: ViewFlipper = binding.typingIndicator

        init {
            chatTxt.startAnimation(bounceAnim)
        }

        fun bind(item: Message) {
            chatTxt.text = item.message

            if (item.sendBy == getViewType(SendBy.TYPING)) {
                typingIndicator.apply {
                    visibility = View.VISIBLE
                    startFlipping()
                    flipInterval = 200
                }
            } else {
                typingIndicator.apply {
                    visibility = View.GONE
                    stopFlipping()
                }
            }
        }
    }

    inner class LineViewHolder(binding: ItemChatLineBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var versionTxt = binding.itemMsgVersionTv

        fun bind(item: Message) {
            versionTxt.text = item.message
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem.message == newItem.message
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }
        }
    }


}