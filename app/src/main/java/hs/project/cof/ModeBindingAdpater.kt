package hs.project.cof

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import hs.project.cof.presentation.viewModel.ChatViewModel

@BindingAdapter("inputContainerVisible")
fun inputContainerVisible(inputContainer: ConstraintLayout, modeStatus: ChatViewModel.ViewModeStatus) {
    when(modeStatus) {
        ChatViewModel.ViewModeStatus.CHAT -> {
            inputContainer.visibility = View.VISIBLE
        }
        else -> {
            inputContainer.visibility = View.GONE
        }
    }
}