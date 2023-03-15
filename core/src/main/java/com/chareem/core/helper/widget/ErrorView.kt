package com.chareem.core.helper.widget

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.view.View
import com.chareem.core.R
import com.chareem.core.databinding.ErrorViewBinding

class ErrorView : RelativeLayout {
    private var errorListener: ErrorListener? = null
    private var binding: ErrorViewBinding? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        gravity = Gravity.CENTER
        binding = ErrorViewBinding.inflate(LayoutInflater.from(context))
    }

    fun setView(isError: Boolean, message: String?, case:Int? = 0, listener: ErrorListener) {
        binding?.let {
            it.lytDisconnect.visibility = View.VISIBLE
            if (isError) {
                it.lytOffline.visibility = View.VISIBLE
                if (case == 2) it.imgError.setImageResource(R.drawable.ic_signal_wifi_off)
                else if (case == 1) it.imgError.setImageResource(R.drawable.ic_gps_off)
                else it.imgError.setImageResource(R.drawable.ic_bug_report)
                if (message != null) it.txtConnection.text = message
                it.lytOffline.setOnClickListener {
                    listener.onReloadData()
                }
            } else {
                it.lytDisconnect.visibility = View.GONE
                it.lytOffline.visibility = View.GONE
            }
        }
    }

    fun setErrorListener(errorListener: ErrorListener) {
        this.errorListener = errorListener
    }

    interface ErrorListener {
        fun onReloadData()
    }
}