package com.chareem.master.feature.main.third

import android.content.Context
import androidx.lifecycle.*
import com.chareem.core.BaseVM
import com.chareem.core.data.BaseResponse
import com.chareem.core.util.NetworkUtil
import com.chareem.master.R
import com.chareem.master.Repository.globalRepository
import com.chareem.master.data.model.MovieReview
import kotlinx.coroutines.launch

class ThirdVM(private val repo: globalRepository): BaseVM() {
    override fun getTagName(): String = javaClass.simpleName

    private val _getMovieReview = MutableLiveData<BaseResponse<List<MovieReview>>>()
    val getMovieReview: LiveData<BaseResponse<List<MovieReview>>> = _getMovieReview

    fun getMovieReview(movieId: Int, page: Int, context: Context){
        viewModelScope.launch {
            kotlin.runCatching {
                _getMovieReview.value = BaseResponse.Loading()
                repo.getMovieReview(movieId, page)
            }.onSuccess { data ->
                _getMovieReview.postValue(BaseResponse.Success(data))
            }.onFailure {
                if (NetworkUtil.isNetworkConnected(context))
                    _getMovieReview.postValue(BaseResponse.Error(it, 2))
                else _getMovieReview.postValue(BaseResponse.Error(it, 2, context.getString(R.string.error_connection)))
            }
        }
    }
}
