package hs.project.cof

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import hs.project.cof.presentation.viewModel.ChatViewModel

@BindingAdapter("sendingTextVisible")
fun sendingTextVisible(sendingText: TextView, apiStatus: ChatViewModel.MessageApiStatus) {
    when(apiStatus) {
        ChatViewModel.MessageApiStatus.LOADING -> {
            sendingText.visibility = View.VISIBLE
        }
        ChatViewModel.MessageApiStatus.ERROR -> {
            sendingText.visibility = View.GONE
        }
        ChatViewModel.MessageApiStatus.DONE -> {
            sendingText.visibility = View.GONE
        }
        else -> {
            sendingText.visibility = View.GONE
        }
    }
}