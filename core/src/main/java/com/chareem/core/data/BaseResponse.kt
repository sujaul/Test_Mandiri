package com.chareem.core.data

sealed class BaseResponse<out T : Any> {
    class Loading(val messsage: String? = null) : BaseResponse<Nothing>()
    class Success<out T : Any>(val data: T) : BaseResponse<T>()
    /**
     * @param case
     * 0 = Masalah Pada Permission, 1 = GPS Tidak Aktif, 2 = Koneksi Internet Bermasalah,
     * 3 = Error Bugs, 4 = Tidak Ada Data, else = Unknown Error ()
     *
     **/
    class Error(val exception: Throwable, val case: Int = 4, val message: String = if (exception.localizedMessage == null) exception.message ?: "Network is bussy, please check again later" else exception.localizedMessage!!) :
        BaseResponse<Nothing>()
}
