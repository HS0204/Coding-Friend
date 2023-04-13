package hs.project.cof.presentation.view.chatList

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hs.project.cof.data.db.ChatList
import hs.project.cof.databinding.ItemChatListBinding
import hs.project.cof.presentation.viewModels.ChatListViewModel
import hs.project.cof.presentation.viewModels.ChatViewModel
import java.text.SimpleDateFormat
import java.util.*

class ChatListAdapter(
    private val onItemClicked: (Int) -> Unit,
    private val chatViewModel: ChatViewModel,
    private val listViewModel: ChatListViewModel
) : ListAdapter<ChatList, ChatListAdapter.ChatListViewHolder>(DiffCallback) {

    private var chatList = listOf<ChatList>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatListViewHolder {
        val viewHolder = ChatListViewHolder(
            ItemChatListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        return viewHolder
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        holder.bind(chatList[position], position)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    fun setChatList(newList: List<ChatList>) {
        chatList = newList
        notifyDataSetChanged()
    }

    inner class ChatListViewHolder(binding: ItemChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val container = binding.itemChatListContainerCl
        private val num = binding.itemChatListIdTv
        private val title = binding.itemChatListTitleTv
        private val redDate = binding.itemChatListDateRegTv
        private val modDate = binding.itemChatListDateModTv

        fun bind(item: ChatList, position: Int) {
            val dateFormat = SimpleDateFormat("yy.MM.dd HH:mm")

            num.text = position.plus(1).toString()
            title.apply {
                text = item.title
                isSingleLine = true
                ellipsize = TextUtils.TruncateAt.END
            }
            redDate.text = dateFormat.format(Date(item.regDate))
            modDate.text = dateFormat.format(Date(item.modDate))

            // show chatting list log
            container.setOnClickListener {
                chatViewModel.setViewModeStatus(ChatViewModel.ViewModeStatus.LOG)
                onItemClicked(chatList[position].id)
            }

            // delete chatting log
            container.setOnLongClickListener {
                AlertDialog.Builder(itemView.context)
                    .setMessage("'${item.title}' 채팅 기록을 삭제하시겠습니까?")
                    .setPositiveButton("예") { _, _ ->
                        listViewModel.removeChatLog(item.id)
                        Toast.makeText(itemView.context, "${item.title} 채팅 기록이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                        chatViewModel.clearMessageList()
                        chatViewModel.setViewModeStatus(ChatViewModel.ViewModeStatus.CHAT)
                    }
                    .setNegativeButton("아니오", null)
                    .show()

                true
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ChatList>() {
            override fun areItemsTheSame(oldItem: ChatList, newItem: ChatList): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ChatList, newItem: ChatList): Boolean {
                return oldItem == newItem
            }
        }
    }
}