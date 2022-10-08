package ru.spiridonov.be.kind.presentation.adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import ru.spiridonov.be.kind.R

@BindingAdapter("errorInput")
fun bindErrorInput(textInputLayout: TextInputLayout, isError: Boolean) {
    val message =
        if (isError) textInputLayout.context.getString(R.string.error_input_name) else null
    textInputLayout.error = message
}

@BindingAdapter("welcomeUser")
fun textWelcomeUser(textView: TextView, text: String?) {
    textView.text = String.format(textView.context.resources.getString(R.string.welcome), text)
}