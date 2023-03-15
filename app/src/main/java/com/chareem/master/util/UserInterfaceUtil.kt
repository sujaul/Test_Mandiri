package com.chareem.master.util

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.os.Build
import android.telephony.TelephonyManager

object UserInterfaceUtil {
    fun requestFocus(view: View, window : Window) {
        if (view.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    fun showAlertDialog(context : Context) : AlertDialog.Builder{
        val builder: AlertDialog.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert)
        } else {
            builder = AlertDialog.Builder(context)
        }

        return builder
    }


    fun isSimReady(context: Context): Boolean{
        val telMgr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val simState = telMgr.getSimState()
        val con: Boolean = when (simState) {
//            TelephonyManager.SIM_STATE_ABSENT ->{false}
//            TelephonyManager.SIM_STATE_NETWORK_LOCKED ->{false }
//            TelephonyManager.SIM_STATE_PIN_REQUIRED ->{}
//            TelephonyManager.SIM_STATE_PUK_REQUIRED ->{}
//            TelephonyManager.SIM_STATE_CARD_IO_ERROR ->{false}
//            TelephonyManager.SIM_STATE_CARD_RESTRICTED->{false}
//            TelephonyManager.SIM_STATE_NOT_READY ->{false}
//            TelephonyManager.SIM_STATE_PERM_DISABLED ->{false}
            TelephonyManager.SIM_STATE_READY ->{true}
//            TelephonyManager.SIM_STATE_UNKNOWN ->{false}
            else -> false
        }
        return con
    }

    fun capitalizeFirstString(str: String): String {
        var retStr = str
        try {
            retStr = str.substring(0, 1).uppercase() + str.substring(1)
        } catch (e: Exception) { }
        return retStr
    }

    fun getStringFromIndex(index:Int, str: String): String {
        var retStr = str
        try {
            retStr = str.substring(0, 1).uppercase() + str.substring(1)
        } catch (e: Exception) { }
        return retStr
    }
}