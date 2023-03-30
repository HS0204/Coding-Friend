package hs.project.cof.presentation.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import hs.project.cof.MessageAdapter
import hs.project.cof.base.ApplicationClass
import hs.project.cof.base.BaseFragment
import hs.project.cof.data.remote.model.Message
import hs.project.cof.databinding.FragmentChatBinding
import hs.project.cof.presentation.viewModel.ChatViewModel

class ChatFragment : BaseFragment<FragmentChatBinding>(FragmentChatBinding::inflate) {
    private val viewModel: ChatViewModel by activityViewModels()
    private lateinit var messageAdapter: MessageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set viewModel
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setAdapter()

        // show welcome message
        binding.mainWelcomeTv.visibility = View.VISIBLE

        // observe message list changes
        viewModel.messageList.observe(viewLifecycleOwner, Observer { messageList ->
            messageAdapter.setMessageList(messageList)
            binding.mainChatRv.smoothScrollToPosition(if (messageList.size == 0) 0 else messageList.size - 1)
        })

        sendMessageListener()
        modelSettingListener()
    }

    private fun setAdapter() {
        messageAdapter = MessageAdapter()
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
            viewModel.getMessage(question)

            binding.mainInputMsgEt.text.clear()
            binding.mainWelcomeTv.visibility = View.GONE
        }
    }

    private fun modelSettingListener() {
        binding.mainActionbarSettingIb.setOnClickListener {
            val dialogFragment = SettingFragment()
            dialogFragment.show(childFragmentManager, "settings_dialog")
        }
    }
}