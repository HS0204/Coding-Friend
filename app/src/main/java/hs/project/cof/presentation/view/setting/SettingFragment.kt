package hs.project.cof.presentation.view.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import hs.project.cof.base.ApplicationClass.Companion.getDialogType
import hs.project.cof.base.ApplicationClass.Companion.DialogType
import hs.project.cof.base.BaseFragment
import hs.project.cof.databinding.FragmentSettingBinding
import hs.project.cof.presentation.viewModels.ChatViewModelFactory
import hs.project.cof.presentation.viewModels.ChatViewModel
import hs.project.cof.presentation.widget.utils.SettingDialogFragment


class SettingFragment : BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {

    private val viewModel: ChatViewModel by activityViewModels {
        ChatViewModelFactory()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // observe version changes
        viewModel.version.observe(viewLifecycleOwner, Observer { version ->
          binding.settingVersionPresentVTv.text = "현재: $version"
        })

        binding.settingVersionContainerCl.setOnClickListener {
            showDialogFragment(getDialogType(DialogType.VERSION))
        }

        binding.settingTemperatureContainerCl.setOnClickListener {
            showDialogFragment(getDialogType(DialogType.TEMPERATURE))
        }
    }

    private fun showDialogFragment(type: String) {
        val dialogFragment = SettingDialogFragment.newInstance(type)
        dialogFragment.show(childFragmentManager, "detail_settings_dialog")
    }
}