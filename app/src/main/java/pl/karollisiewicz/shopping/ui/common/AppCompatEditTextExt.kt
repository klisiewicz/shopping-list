package pl.karollisiewicz.shopping.ui.common

import android.graphics.Paint
import androidx.appcompat.widget.AppCompatEditText

fun AppCompatEditText.strikeThrough() {
    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

fun AppCompatEditText.clearStrikeThrough() {
    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
}