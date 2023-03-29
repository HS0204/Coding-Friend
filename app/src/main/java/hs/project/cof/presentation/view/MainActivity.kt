package hs.project.cof.presentation.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import hs.project.cof.MessageAdapter
import hs.project.cof.base.ApplicationClass.Companion.SEND_BY_USER
import hs.project.cof.data.remote.model.Message
import hs.project.cof.databinding.ActivityMainBinding
import hs.project.cof.presentation.viewModel.ChatViewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: ChatViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set viewModel
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // set adapter
        setAdapter()
        
        // observe message list changes
        viewModel.messageList.observe(this, Observer { messageList ->
            messageAdapter.setMessageList(messageList)
            binding.mainChatRv.smoothScrollToPosition(if (messageList.size == 0) 0 else messageList.size - 1)
        })

        // send message
        sendMessage()
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.clearMessageList()
        binding.mainWelcomeTv.visibility = View.VISIBLE
    }

    private fun setAdapter() {
        messageAdapter = MessageAdapter()
        binding.mainChatRv.adapter = messageAdapter
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        layoutManager.stackFromEnd
        binding.mainChatRv.layoutManager = layoutManager
    }

    private fun sendMessage() {
        binding.mainInputMsgBtn.setOnClickListener {
            val question = binding.mainInputMsgEt.text.toString().trim()
            // receive message from user
            viewModel.addMessage(Message(question, SEND_BY_USER))

            // request api
            viewModel.getMessage(question)

            binding.mainInputMsgEt.text.clear()
            binding.mainWelcomeTv.visibility = View.GONE
        }
    }

}