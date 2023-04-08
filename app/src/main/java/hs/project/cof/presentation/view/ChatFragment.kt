package hs.project.cof.presentation.view

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import hs.project.cof.MessageAdapter
import hs.project.cof.R
import hs.project.cof.base.ApplicationClass
import hs.project.cof.base.ApplicationClass.Companion.getViewType
import hs.project.cof.base.ApplicationClass.Companion.getViewName
import hs.project.cof.base.ApplicationClass.Companion.SendBy
import hs.project.cof.base.ApplicationClass.Companion.DialogType
import hs.project.cof.base.ApplicationClass.Companion.getDialogType
import hs.project.cof.base.BaseFragment
import hs.project.cof.data.db.ChatList
import hs.project.cof.data.remote.model.Message
import hs.project.cof.databinding.FragmentChatBinding
import hs.project.cof.presentation.viewModel.ChatListViewModel
import hs.project.cof.presentation.viewModel.ChatListViewModelFactory
import hs.project.cof.presentation.viewModel.ChatViewModelFactory
import hs.project.cof.presentation.viewModel.ChatViewModel
import java.util.*

class ChatFragment : BaseFragment<FragmentChatBinding>(FragmentChatBinding::inflate) {

    private val chatViewModel: ChatViewModel by activityViewModels {
        ChatViewModelFactory()
    }
    private val listViewModel: ChatListViewModel by activityViewModels {
        ChatListViewModelFactory(
            (activity?.application as ApplicationClass).database.ChatListDao()
        )
    }

    private val currentDate = Date()
    private val currentTimeMillis = currentDate.time

    private val argsFromList: ChatFragmentArgs by navArgs()
    private lateinit var messageAdapter: MessageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set viewModel
        binding.lifecycleOwner = this
        binding.chatViewModel = chatViewModel
        binding.listViewModel = listViewModel

        setAdapter()

        // observe message list changes
        chatViewModel.messageList.observe(viewLifecycleOwner, Observer { messageList ->
            messageAdapter.setMessageList(messageList)
            binding.mainChatRv.smoothScrollToPosition(if (messageList.size == 0) 0 else messageList.size - 1)

            // reset, save btn visibility & anim control
            if (messageList.isNotEmpty()) {
                binding.mainActionbarResetIb.visibility = View.VISIBLE
                binding.mainActionbarChatSaveIb.visibility = View.VISIBLE

                val anim = AnimationUtils.loadAnimation(context, R.anim.bounce)

                if (chatViewModel.apiStatus.value == ChatViewModel.MessageApiStatus.NONESTARTED) {
                    binding.mainActionbarResetIb.startAnimation(anim)
                    binding.mainActionbarChatSaveIb.startAnimation(anim)
                }
            } else {
                binding.mainActionbarResetIb.visibility = View.GONE
                binding.mainActionbarChatSaveIb.visibility = View.GONE
            }
        })

        // observe api status changes
        chatViewModel.apiStatus.observe(viewLifecycleOwner, Observer { apiStatus ->
            when (apiStatus) {
                ChatViewModel.MessageApiStatus.LOADING -> {
                    chatViewModel.addTypingMessage()
                }
                ChatViewModel.MessageApiStatus.NONESTARTED -> {}
                else -> {   // DONE or ERROR
                    if (chatViewModel.messageList.value!!.size > 0 && chatViewModel.viewModeStatus.value == ChatViewModel.ViewModeStatus.LOG) {
                        listViewModel.updateChatList(argsFromList.chatListId, currentTimeMillis, chatViewModel.messageList.value!!)
                    }
                }
            }
        })

        // check view mode
        if (chatViewModel.viewModeStatus.value == ChatViewModel.ViewModeStatus.LOG && argsFromList.chatListId != 0) {
            retrieveMessageList()
        }

        sendMessageListener()
        resetBtnListener()
        chatListBtnListener()
        settingBtnListener()

        /**
         * 버튼 클릭해야 저장되는 임시 코드 for 테스트
         */
        binding.mainActionbarChatSaveIb.setOnClickListener {
            val chatList = ChatList(
                regDate = currentTimeMillis,
                modDate = currentTimeMillis,
                title = chatViewModel.messageList.value!![0].message,
                version = getViewName(chatViewModel.messageList.value!![0].sendBy),
                chatList = chatViewModel.messageList.value.orEmpty().toList()
            )
            listViewModel.insertChatList(chatList)
        }
    }

    private fun setAdapter() {
        messageAdapter = MessageAdapter(requireContext())
        binding.mainChatRv.adapter = messageAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        layoutManager.stackFromEnd
        binding.mainChatRv.layoutManager = layoutManager
    }

    private fun retrieveMessageList() {
        chatViewModel.clearMessageList()
        argsFromList.chatListId.let { id ->
            listViewModel.retrieveChatList(id).observe(this.viewLifecycleOwner) {
                chatViewModel.retrieveMessageListFromList(it.chatList)
            }
        }
    }

    private fun sendMessageListener() {
        binding.mainInputMsgBtn.setOnClickListener {
            val question = binding.mainInputMsgEt.text.toString().trim()
            // receive message from user
            chatViewModel.addMessage(Message(question, getViewType(SendBy.USER)))
            // request api
            chatViewModel.getMessageFromChatGPT(question)

            binding.mainInputMsgEt.text.clear()
        }
    }

    private fun settingBtnListener() {
        binding.mainActionbarSettingIb.setOnClickListener {
            moveFragment(R.id.action_chatFragment_to_settingFragment)
        }
    }

    private fun chatListBtnListener() {
        binding.mainActionbarChatListIb.setOnClickListener {
            moveFragment(R.id.action_chatFragment_to_chatListFragment)
        }
    }

    private fun resetBtnListener() {
        binding.mainActionbarResetIb.setOnClickListener {
            chatViewModel.setViewModeStatus(ChatViewModel.ViewModeStatus.CHAT)
            val dialogFragment = SettingDialogFragment.newInstance(getDialogType(DialogType.RESET))
            dialogFragment.show(childFragmentManager, "reset_check_dialog")
        }
    }
}