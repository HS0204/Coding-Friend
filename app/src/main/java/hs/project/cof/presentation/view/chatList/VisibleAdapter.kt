package hs.project.cof.presentation.view.chatList

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("isVisible")
fun isVisible(textView: TextView, isVisible: Boolean) {
    if (isVisible) {
        textView.visibility = View.VISIBLE
    } else {
        textView.visibility = View.GONE
    }
}