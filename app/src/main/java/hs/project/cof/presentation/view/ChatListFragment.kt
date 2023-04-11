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
import hs.project.cof.presentation.viewModels.ChatListViewModel
import hs.project.cof.presentation.viewModels.ChatListViewModelFactory
import hs.project.cof.presentation.viewModels.ChatViewModel
import hs.project.cof.presentation.viewModels.ChatViewModelFactory
import kotlinx.coroutines.launch


class ChatListFragment : BaseFragment<FragmentChatListBinding>(FragmentChatListBinding::inflate) {


    private val chatViewModel: ChatViewModel by activityViewModels {
        ChatViewModelFactory()
    }

    private val listViewModel: ChatListViewModel by activityViewModels {
        ChatListViewModelFactory(
            (activity?.application as ApplicationClass).database.ChatListDao()
        )
    }

    private lateinit var chatListAdapter: ChatListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set viewModel
        binding.lifecycleOwner = this
        binding.viewModel = listViewModel

        setAdapter()

    }

    private fun setAdapter() {

        chatListAdapter = ChatListAdapter(
            onItemClicked = { chatListId ->
                val action =
                    ChatListFragmentDirections.actionChatListFragmentToChatFragment(chatListId)
                findNavController().navigate(action)
            },
            chatViewModel = chatViewModel,
            listViewModel = listViewModel
        )
        binding.chatListRv.adapter = chatListAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.chatListRv.layoutManager = layoutManager

        lifecycle.coroutineScope.launch {
            listViewModel.getAllChatList.collect() {
                chatListAdapter.setChatList(it)
            }
        }
    }

}