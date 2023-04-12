package hs.project.cof.presentation.view.chatList

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("visibleMessage")
fun visibleMessage(textView: TextView, isVisible: Boolean) {
    Log.d("CHECK_VISIBLE","$isVisible")
    if (isVisible) {
        textView.visibility = View.VISIBLE
    } else {
        textView.visibility = View.GONE
    }
}