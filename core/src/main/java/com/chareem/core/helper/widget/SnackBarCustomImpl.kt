package com.chareem.core.helper.widget

import android.app.Activity
import android.view.View
import com.chareem.core.R
import com.chareem.core.util.displaySnakbar
import com.google.android.material.snackbar.Snackbar

class SnackBarCustomImpl(mType: MessageType) : SnackbarToastCustom {
    private val type = mType

    override fun getSnackbar(
        activity: Activity,
        message: String,
        duration: Int,
        label: String,
        listener: (() -> Unit)?
    ): Snackbar {
        when (type) {
            MessageType.Success -> return SnackBarSuccess.getSnackbar(activity, message, duration, label, listener)
            MessageType.Danger -> return SnackBarError.getSnackbar(activity, message, duration, label, listener)
            MessageType.Info -> return SnackBarInfo.getSnackbar(activity, message, duration, label, listener)
        }
    }
}

object SnackBarError : SnackbarToastCustom {
    override val drawableId: Int
        get() = R.drawable.ic_close
    override val colorId: Int
        get() = R.color.red_600
}

object SnackBarInfo : SnackbarToastCustom {
    override val drawableId: Int
        get() = R.drawable.ic_error_outline
    override val colorId: Int
        get() = R.color.blue_500
}

object SnackBarSuccess : SnackbarToastCustom {
    override val drawableId: Int
        get() = R.drawable.ic_done
    override val colorId: Int
        get() = R.color.green_500
}

enum class MessageType{
    Success,
    Danger,
    Info
}