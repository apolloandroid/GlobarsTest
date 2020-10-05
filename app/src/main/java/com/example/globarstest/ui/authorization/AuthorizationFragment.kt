package com.example.globarstest.ui.authorization

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.globarstest.R
import com.example.globarstest.databinding.FragmentAuthorizationBinding
import com.example.globarstest.di.AppModule
import com.example.globarstest.di.DaggerAppComponent
import com.example.globarstest.util.hideKeyboard
import javax.inject.Inject


class AuthorizationFragment : Fragment() {
    lateinit var binding: FragmentAuthorizationBinding

    @Inject
    lateinit var viewModel: AuthorizationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_authorization, container, false)
        binding.lifecycleOwner = this
        initObservers()
        setTextChangedListeners()
        setOnKeyListeners()
        binding.buttonEnter.setOnClickListener { viewModel.onEnterButtonClicked() }
        return binding.root
    }

    private fun injectFragment() {
        val applicationComponent = DaggerAppComponent.builder()
            .appModule(AppModule(requireContext()))
            .build()
        applicationComponent?.injectAuthorizationFragment(this)
    }

    private fun initObservers() {
        viewModel.authorizationResponse.observe(viewLifecycleOwner, Observer {
            viewModel.checkUserNameAndPassword()
        })
        viewModel.navigateToMap.observe(viewLifecycleOwner, Observer { event ->
            if (event) navigateToMapFragment()
        })
        viewModel.showErrorMessage.observe(viewLifecycleOwner, Observer { event ->
            if (event) showErrorAuthorizationDialog()
        })
        viewModel.showNoInternetConnectionMessage.observe(viewLifecycleOwner, Observer { event ->
            if (event) showNoInternetConnectionMessage()
        })
        viewModel.hideKeyBoard.observe(viewLifecycleOwner, Observer { event ->
            if (event) binding.root.hideKeyboard()
        })
        viewModel.enterButtonDisabled.observe(viewLifecycleOwner, Observer { event ->
            binding.buttonEnter.isEnabled = !event
        })
    }

    private fun setTextChangedListeners() {
        binding.editUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onEditUserNameTextChanged(s.toString().trim())
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.afterEditUserNameTextChanged()
            }
        })
        binding.editPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(
                password: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                viewModel.onEditPasswordTextChanged((password.toString().trim()))
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.afterEditPasswordTextChanged()
            }
        })
    }

    private fun setOnKeyListeners() {
        binding.editPassword.setOnKeyListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                viewModel.onEnterButtonClicked()
            }
            false
        }
    }

    private fun navigateToMapFragment() {
        if (findNavController().currentDestination?.id == R.id.authorizationFragment) {
            val bundle = Bundle().apply { putSerializable(viewModel.TOKEN_TAG, viewModel.token) }
            findNavController().navigate(R.id.action_authorizationFragment_to_mapFragment, bundle)
        }
    }

    private fun showErrorAuthorizationDialog() {
        val errorAuthorizationFragment = ErrorAuthorizationFragment()
        errorAuthorizationFragment.show(
            requireActivity().supportFragmentManager,
            ErrorAuthorizationFragment.TAG
        )
    }

    private fun showNoInternetConnectionMessage() {
        Toast.makeText(
            requireContext(),
            getString(R.string.no_internet_connection_message_text),
            Toast.LENGTH_LONG
        ).show()
    }
}