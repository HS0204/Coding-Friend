package hs.project.cof.presentation.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import hs.project.cof.base.ApplicationClass.Companion.TEMPERATURE
import hs.project.cof.base.ApplicationClass.Companion.VERSION
import hs.project.cof.base.BaseFragment
import hs.project.cof.databinding.FragmentSettingBinding
import hs.project.cof.presentation.viewModel.ChatViewModel


class SettingFragment : BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {

    private val viewModel: ChatViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // observe version changes
        viewModel.version.observe(viewLifecycleOwner, Observer { version ->
          binding.settingVersionPresentVTv.text = "현재: $version"
        })

        binding.settingVersionContainerCl.setOnClickListener {
            showDialogFragment(VERSION)
        }

        binding.settingTemperatureContainerCl.setOnClickListener {
            showDialogFragment(TEMPERATURE)
        }
    }

    private fun showDialogFragment(type: String) {
        val dialogFragment = SettingDialogFragment.newInstance(type)
        dialogFragment.show(childFragmentManager, "detail_settings_dialog")
    }
}