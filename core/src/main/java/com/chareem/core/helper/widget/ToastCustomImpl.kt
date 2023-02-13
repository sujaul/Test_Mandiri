package com.chareem.core.helper.widget

import android.app.Activity
import android.widget.Toast
import com.chareem.core.R

class ToastCustomImpl(pType: MessageType): SnackbarToastCustom {
    private var type = pType
    override fun getToast(activity: Activity, message: String, duration: Int): Toast {
        return when (type) {
            MessageType.Success -> ToastCustomSuccess.getToast(activity, message, duration)
            MessageType.Danger -> ToastCustomError.getToast(activity, message, duration)
            MessageType.Info -> ToastCustomInfo.getToast(activity, message, duration)
        }
    }
}

object ToastCustomError : SnackbarToastCustom {
    override val drawableId: Int
        get() = R.drawable.ic_close
    override val colorId: Int
        get() = R.color.red_600
}

object ToastCustomInfo : SnackbarToastCustom {
    override val drawableId: Int
        get() = R.drawable.ic_error_outline
    override val colorId: Int
        get() = R.color.blue_600
}

object ToastCustomSuccess : SnackbarToastCustom {
    override val drawableId: Int
        get() = R.drawable.ic_done
    override val colorId: Int
        get() = R.color.green_500
}