package hs.project.cof.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hs.project.cof.data.remote.model.Message

class ChatViewModel : ViewModel() {

    private var _messageList = MutableLiveData<MutableList<Message>>()
    val messageList: LiveData<MutableList<Message>> = _messageList

    init {
        clearMessageList()
    }

    fun addMessage(msg: Message) {
        _messageList.value = _messageList.value?.toMutableList()?.apply { add(msg) }
    }

    fun removeLastMessage() {
        _messageList.value = _messageList.value?.toMutableList()?.apply { removeLastOrNull() }
    }

    fun clearMessageList() {
        _messageList.value = ArrayList<Message>()
    }

}