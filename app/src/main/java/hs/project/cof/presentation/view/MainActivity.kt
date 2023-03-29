package hs.project.cof.presentation.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import hs.project.cof.BuildConfig
import hs.project.cof.MessageAdapter
import hs.project.cof.base.ApplicationClass.Companion.SEND_BY_BOT
import hs.project.cof.base.ApplicationClass.Companion.SEND_BY_USER
import hs.project.cof.data.remote.api.chatView
import hs.project.cof.data.remote.api.chattingService
import hs.project.cof.data.remote.model.Chat
import hs.project.cof.data.remote.model.Message
import hs.project.cof.data.remote.model.RequestMessage
import hs.project.cof.databinding.ActivityMainBinding
import hs.project.cof.presentation.viewModel.ChatViewModel


class MainActivity : AppCompatActivity(), chatView {

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
            addChat(question, SEND_BY_USER)
            requestMsg(question)

            viewModel.addMessage(Message("입력 중...", SEND_BY_BOT))
            binding.mainInputMsgEt.text.clear()
            binding.mainWelcomeTv.visibility = View.GONE
        }
    }

    private fun addChat(msg: String, sendBy: Int) {
        runOnUiThread {
            viewModel.addMessage(Message(msg, sendBy))
            binding.mainChatRv.smoothScrollToPosition(binding.mainChatRv.adapter!!.itemCount)
        }
    }

    private fun addResponse(response: String) {
        viewModel.removeLastMessage()
        addChat(response, SEND_BY_BOT)
    }

    private fun requestMsg(msg: String) {
        val chat = Chat(model = "gpt-3.5-turbo",
                        messages = listOf(RequestMessage(content = msg, role = "user")))
        chattingService(this).requestMessage(BuildConfig.API_KEY, chat)
    }

    override fun onGetChatSuccess(message: String) {
        addResponse(message)
    }

    override fun onGetChatFailure(message: String) {
        addResponse(message)
    }

}