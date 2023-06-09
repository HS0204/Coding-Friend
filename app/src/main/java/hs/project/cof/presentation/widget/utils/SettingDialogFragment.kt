package hs.project.cof.presentation.widget.utils

import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import hs.project.cof.R
import hs.project.cof.base.ApplicationClass.Companion.DialogType
import hs.project.cof.base.ApplicationClass.Companion.getDialogType
import hs.project.cof.presentation.viewModels.ChatViewModelFactory
import hs.project.cof.presentation.viewModels.ChatViewModel

class SettingDialogFragment() : DialogFragment() {

    private val viewModel: ChatViewModel by activityViewModels {
        ChatViewModelFactory()
    }
    private lateinit var type: String
    private lateinit var builder: AlertDialog.Builder

    companion object {
        fun newInstance(type: String): SettingDialogFragment {
            val args = Bundle()
            args.putString("type", type)
            val fragment = SettingDialogFragment()
            fragment.arguments = args

            return fragment
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            settingType()
            builder = AlertDialog.Builder(it)
            buildBuilder()
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun settingType() {
        type = arguments?.getString("type").toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun buildBuilder() {
        when(type) {
            getDialogType(DialogType.VERSION) -> {
                builder.setTitle("버전을 설정해주세요")
                    .setItems(
                        R.array.version_array,
                        DialogInterface.OnClickListener { dialog, which ->
                            val modelValues = resources.getStringArray(R.array.models_array)
                            val selectedValue = modelValues[which]
                            val selectedVersion = resources.getStringArray(R.array.version_array)[which]
                            viewModel.setModel(selectedValue, selectedVersion)
                        })
            }
            getDialogType(DialogType.TEMPERATURE) -> {
                val seekBar = SeekBar(context).apply {
                    max = 20
                    min = 0
                    progress = viewModel.temperature.value!!
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    )
                }

                val minText = TextView(context).apply {
                    text = "고정적"
                    textSize = 14f
                    gravity = Gravity.CENTER_VERTICAL
                    setPadding(0, 0, 2, 0)
                }

                val maxText = TextView(context).apply {
                    text = "창의적"
                    textSize = 14f
                    gravity = Gravity.CENTER_VERTICAL
                    setPadding(2, 0, 0, 0)
                }

                builder.setTitle("챗봇의 독창성을 설정해주세요")
                    .setView(
                        LinearLayout(context).apply {
                            orientation = LinearLayout.HORIZONTAL
                            setPadding(50, 50, 50, 0)
                            addView(minText)
                            addView(seekBar)
                            addView(maxText)
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                        }
                    )
                    .setPositiveButton("확인") { _, _ ->
                        viewModel.setTemperature(seekBar.progress)
                    }
            }
            getDialogType(DialogType.RESET) -> {
                builder.setTitle("채팅 내용을 지우시겠습니까?")
                    .setPositiveButton("예") { _, _ -> viewModel.clearMessageList() }
                    .setNegativeButton("아니오", null)
            }
            getDialogType(DialogType.REPORT) -> {
                builder.setTitle("개발자에게 신고하시겠습니까?")
                    .setMessage("현재 신고 기능은 준비 중입니다.")
                    .setPositiveButton("예", null)
                    .setNegativeButton("아니오", null)
            }
            else -> {

            }
        }
    }
}