package hs.project.cof.presentation.view

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import hs.project.cof.R
import hs.project.cof.presentation.viewModel.ChatViewModel

class SettingFragment : DialogFragment() {

    private val viewModel: ChatViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("버전을 선택해주세요")
                .setItems(
                    R.array.version_array,
                    DialogInterface.OnClickListener { dialog, which ->
                        val modelValues = resources.getStringArray(R.array.models_array)
                        val selectedValue = modelValues[which]
                        val selectedVersion = resources.getStringArray(R.array.version_array)[which]
                        viewModel.setModel(selectedValue, selectedVersion)
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}