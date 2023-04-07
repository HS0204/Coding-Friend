package hs.project.cof.presentation.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hs.project.cof.ChatListAdapter
import hs.project.cof.base.ApplicationClass
import hs.project.cof.base.BaseFragment
import hs.project.cof.databinding.FragmentChatListBinding
import hs.project.cof.presentation.viewModel.ChatListViewModel
import hs.project.cof.presentation.viewModel.ChatListViewModelFactory
import kotlinx.coroutines.launch


class ChatListFragment : BaseFragment<FragmentChatListBinding>(FragmentChatListBinding::inflate) {

    private val viewModel: ChatListViewModel by activityViewModels {
        ChatListViewModelFactory(
            (activity?.application as ApplicationClass).database.ChatListDao()
        )
    }

    private lateinit var chatListAdapter: ChatListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set viewModel
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setAdapter()

    }

    private fun setAdapter() {

        chatListAdapter = ChatListAdapter { chatList ->
            val action = ChatListFragmentDirections.actionChatListFragmentToChatFragment(chatList)
            findNavController().navigate(action)
        }
        binding.chatListRv.adapter = chatListAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.chatListRv.layoutManager = layoutManager

        lifecycle.coroutineScope.launch {
            viewModel.getAllChatList().collect() {
                chatListAdapter.setChatList(it)
            }
        }
    }

}