package hs.project.cof

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hs.project.cof.data.db.ChatList
import hs.project.cof.databinding.ItemChatListBinding

class ChatListAdapter(private val onItemClicked: (Int) -> Unit) : ListAdapter<ChatList, ChatListAdapter.ChatListViewHolder>(DiffCallback) {

    private var chatList = listOf<ChatList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListAdapter.ChatListViewHolder {
        val viewHolder = ChatListViewHolder(
            ItemChatListBinding.inflate(
                LayoutInflater.from( parent.context),
                parent,
                false
            )
        )

        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition

            onItemClicked(chatList[position].id)
        }
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
        var num = binding.itemChatListIdTv
        var title = binding.itemChatListTitleTv
        var version = binding.itemChatListVersionTv
        var redDate = binding.itemChatListDateRegTv
        var modDate = binding.itemChatListDateModTv

        fun bind(item: ChatList, position: Int) {
            num.text = position.plus(1).toString()
            title.text = item.title
            version.text = item.version
            redDate.text = item.regDate.toString()
            modDate.text = item.modDate.toString()
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