package com.test.test_karim2.feature.main.third

import android.content.Context
import androidx.lifecycle.*
import com.chareem.core.BaseVM
import com.chareem.core.data.BaseResponse
import com.chareem.core.util.NetworkUtil
import com.test.test_karim2.R
import com.test.test_karim2.Repository.globalRepository
import com.test.test_karim2.data.model.MovieByGenre
import com.test.test_karim2.data.model.MovieReview
import com.test.test_karim2.data.model.MovieVideos
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
