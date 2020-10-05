package com.example.globarstest.ui.authorization

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import android.app.AlertDialog.Builder
import com.example.globarstest.R


class ErrorAuthorizationFragment : DialogFragment() {

    companion object {
        val TAG = ErrorAuthorizationFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_error_authorization, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = Builder(requireContext())
        builder.setTitle(getString(R.string.error_message_title_text))
            .setMessage(getString(R.string.error_message_text))
            .setPositiveButton(getString(R.string.error_message_button_text)) { dialog, id ->
                dialog.cancel()
            }
        return builder.create()
    }
}