package pl.karollisiewicz.shopping.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.inflate(@LayoutRes layoutIdRes: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutIdRes, this, attachToRoot)

fun ViewGroup.hideChildren() {
    for (i in 0..childCount)
        getChildAt(i)?.hide()
}

infix fun View.isVisibleWhen(condition: Boolean) {
    if (condition) show()
    else hide()
}

fun View.show() {
    visibility = VISIBLE
}

fun View.hide() {
    visibility = GONE
}