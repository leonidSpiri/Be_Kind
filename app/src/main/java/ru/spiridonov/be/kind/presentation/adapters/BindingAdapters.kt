package ru.spiridonov.be.kind.presentation.adapters

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import ru.spiridonov.be.kind.R
import java.text.SimpleDateFormat
import java.util.*

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

@BindingAdapter("emailConfirmed")
fun textEmailConfirmed(textView: TextView, isConfirmed: Boolean) {
    if (isConfirmed) {
        textView.text = textView.context.getString(R.string.email_confirmed)
        textView.setTextColor(ContextCompat.getColor(textView.context, R.color.good))
    } else {
        textView.text = textView.context.getString(R.string.email_not_confirmed)
        textView.setTextColor(ContextCompat.getColor(textView.context, R.color.bad))
    }
}

@BindingAdapter("passportConfirmed")
fun textPassportConfirmed(textView: TextView, isConfirmed: Boolean) {
    if (isConfirmed) {
        textView.text = textView.context.getString(R.string.passport_confirmed)
        textView.setTextColor(ContextCompat.getColor(textView.context, R.color.good))
    } else {
        textView.text = textView.context.getString(R.string.passport_not_confirmed)
        textView.setTextColor(ContextCompat.getColor(textView.context, R.color.bad))
    }
}

@BindingAdapter("certConfirmed")
fun textCertConfirmed(textView: TextView, isConfirmed: Boolean) {
    if (isConfirmed) {
        textView.text = textView.context.getString(R.string.certl_confirmed)
        textView.setTextColor(ContextCompat.getColor(textView.context, R.color.good))
    } else {
        textView.text = textView.context.getString(R.string.cert_not_confirmed)
        textView.setTextColor(ContextCompat.getColor(textView.context, R.color.bad))
    }
}

@BindingAdapter("date")
fun textDate(textView: TextView, date: Long) {
    textView.text = longToTime(date)
}


private fun longToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return format.format(date)
}