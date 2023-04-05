package hs.project.cof.presentation.view

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import hs.project.cof.MessageAdapter
import hs.project.cof.R
import hs.project.cof.base.ApplicationClass
import hs.project.cof.base.ApplicationClass.Companion.RESET
import hs.project.cof.base.BaseFragment
import hs.project.cof.data.remote.model.Message
import hs.project.cof.databinding.FragmentChatBinding
import hs.project.cof.presentation.viewModel.ChatViewModelFactory
import hs.project.cof.presentation.viewModel.ChatViewModel

class ChatFragment : BaseFragment<FragmentChatBinding>(FragmentChatBinding::inflate) {

    private val viewModel: ChatViewModel by activityViewModels {
        ChatViewModelFactory(
            (activity?.application as ApplicationClass).database.MessageListDao()
        )
    }

    private lateinit var messageAdapter: MessageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set viewModel
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setAdapter()

        // observe message list changes
        viewModel.messageList.observe(viewLifecycleOwner, Observer { messageList ->
            messageAdapter.setMessageList(messageList)
            binding.mainChatRv.smoothScrollToPosition(if (messageList.size == 0) 0 else messageList.size - 1)

            // reset btn visibility & anim control
            if (messageList.isNotEmpty()) {
                binding.mainActionbarResetIb.visibility = View.VISIBLE

                if (viewModel.apiStatus.value == ChatViewModel.MessageApiStatus.NONESTARTED) {
                    val anim = AnimationUtils.loadAnimation(context, R.anim.bounce)
                    binding.mainActionbarResetIb.startAnimation(anim)
                }
            } else {
                binding.mainActionbarResetIb.visibility = View.GONE
            }
        })

        // observe api status changes
        viewModel.apiStatus.observe(viewLifecycleOwner, Observer { apiStatus ->
            if (apiStatus.equals(ChatViewModel.MessageApiStatus.LOADING)) {
                viewModel.addTypingMessage()
            }
        })

        sendMessageListener()
        resetBtnListener()
        settingBtnListener()
    }

    private fun setAdapter() {
        messageAdapter = MessageAdapter(requireContext())
        binding.mainChatRv.adapter = messageAdapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        layoutManager.stackFromEnd
        binding.mainChatRv.layoutManager = layoutManager
    }

    private fun sendMessageListener() {
        binding.mainInputMsgBtn.setOnClickListener {
            val question = binding.mainInputMsgEt.text.toString().trim()
            // receive message from user
            viewModel.addMessage(Message(question, ApplicationClass.SEND_BY_USER))
            // request api
            viewModel.getMessageFromChatGPT(question)

            binding.mainInputMsgEt.text.clear()
        }
    }

    private fun settingBtnListener() {
        binding.mainActionbarSettingIb.setOnClickListener {
            moveFragment(R.id.action_chatFragment_to_settingFragment)
        }
    }

    private fun resetBtnListener() {
        binding.mainActionbarResetIb.setOnClickListener {
            val dialogFragment = SettingDialogFragment.newInstance(RESET)
            dialogFragment.show(childFragmentManager, "reset_check_dialog")
        }
    }
}