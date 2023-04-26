package hs.project.cof.presentation.view.chatting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ViewFlipper
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hs.project.cof.R
import hs.project.cof.base.ApplicationClass.Companion.DialogType
import hs.project.cof.base.ApplicationClass.Companion.SendBy
import hs.project.cof.base.ApplicationClass.Companion.getDialogType
import hs.project.cof.base.ApplicationClass.Companion.getViewType
import hs.project.cof.data.model.Message
import hs.project.cof.databinding.ItemChatBotBinding
import hs.project.cof.databinding.ItemChatErrorBinding
import hs.project.cof.databinding.ItemChatLineBinding
import hs.project.cof.databinding.ItemChatUserBinding
import hs.project.cof.presentation.widget.utils.SettingDialogFragment

class MessageAdapter(private val onRetryClicked: (Message) -> Unit,
                     private val context: Context,
                     private val childFragmentManager: FragmentManager) : ListAdapter<Message, RecyclerView.ViewHolder>(DiffCallback) {

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
            getViewType(SendBy.ERROR) -> {
                val view =
                    ItemChatErrorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ErrorViewHolder(view)
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
            getViewType(SendBy.ERROR) -> {
                (holder as ErrorViewHolder).bind(curMsg)
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
        private val userTxt = binding.itemMsgUserTv

        init {
            userTxt.startAnimation(bounceAnim)
        }

        fun bind(item: Message) {
            userTxt.text = item.message
        }
    }

    inner class BotViewHolder(binding: ItemChatBotBinding) : RecyclerView.ViewHolder(binding.root) {
        private val chatTxt = binding.itemMsgBotTv
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
        private val versionTxt = binding.itemMsgVersionTv

        fun bind(item: Message) {
            versionTxt.text = item.message
        }
    }

    inner class ErrorViewHolder(binding: ItemChatErrorBinding) : RecyclerView.ViewHolder(binding.root) {
        private val chatTxt = binding.itemMsgErrorMainTv
        private val inputContainer = binding.itemMsgErrorInputContainer
        private val retryBtn = binding.itemMsgErrorRetryBtn
        private val reportBtn = binding.itemMsgErrorReportBtn

        init {
            chatTxt.startAnimation(bounceAnim)
            inputContainer.startAnimation(bounceAnim)
        }

        fun bind(item: Message) {
            chatTxt.text = item.message

            retryBtn.setOnClickListener {
                it.isEnabled = false
                onRetryClicked(item)
            }

            reportBtn.setOnClickListener {
                it.isEnabled = false
                val dialogFragment = SettingDialogFragment.newInstance(getDialogType(DialogType.REPORT))
                dialogFragment.show(childFragmentManager, "report_error_to_developer_dialog")
            }
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