package pl.karollisiewicz.shopping.ui.common

import android.text.Editable
import android.text.TextWatcher

class TextChangedListenerAdapter(
    private val onTextChanged: (CharSequence?) -> Unit
) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        // We are only interested in text change event
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // We are only interested in text change event
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        onTextChanged(s)
    }
}