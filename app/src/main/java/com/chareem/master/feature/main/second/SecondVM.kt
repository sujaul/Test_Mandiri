package com.chareem.master.feature.main.second

import android.content.Context
import androidx.lifecycle.*
import com.chareem.core.BaseVM
import com.chareem.core.data.BaseResponse
import com.chareem.core.util.NetworkUtil
import com.chareem.master.R
import com.chareem.master.Repository.globalRepository
import com.chareem.master.data.model.MovieByGenre
import com.chareem.master.data.model.MovieDetail
import com.chareem.master.data.model.MovieVideosResponse
import kotlinx.coroutines.launch

class SecondVM(private val repo: globalRepository): BaseVM() {
    override fun getTagName(): String = javaClass.simpleName

    private val _getMovieByGenre = MutableLiveData<BaseResponse<List<MovieByGenre>>>()
    val getMovieByGenre: LiveData<BaseResponse<List<MovieByGenre>>> = _getMovieByGenre

    fun getMovieByGenre(genreId: Int, page: Int, context: Context){
        viewModelScope.launch {
            kotlin.runCatching {
                _getMovieByGenre.value = BaseResponse.Loading()
                repo.getMovieByGenre(genreId, page)
            }.onSuccess { data ->
                _getMovieByGenre.postValue(BaseResponse.Success(data))
            }.onFailure {
                if (NetworkUtil.isNetworkConnected(context))
                    _getMovieByGenre.postValue(BaseResponse.Error(it, 2))
                else _getMovieByGenre.postValue(BaseResponse.Error(it, 2, context.getString(R.string.error_connection)))
            }
        }
    }

    private val _getMovieDetail = MutableLiveData<BaseResponse<MovieDetail>>()
    val getMovieDetail: LiveData<BaseResponse<MovieDetail>> = _getMovieDetail

    fun getMovieDetail(movieId: Int, context: Context){
        viewModelScope.launch {
            kotlin.runCatching {
                _getMovieDetail.value = BaseResponse.Loading()
                repo.getMovieDetail(movieId)
            }.onSuccess { data ->
                _getMovieDetail.postValue(BaseResponse.Success(data))
            }.onFailure {
                if (NetworkUtil.isNetworkConnected(context))
                    _getMovieDetail.postValue(BaseResponse.Error(it, 2))
                else _getMovieDetail.postValue(BaseResponse.Error(it, 2, context.getString(R.string.error_connection)))
            }
        }
    }

    private val _getMovieVideos = MutableLiveData<BaseResponse<MovieVideosResponse>>()
    val getMovieVideos: LiveData<BaseResponse<MovieVideosResponse>> = _getMovieVideos

    fun getMovieVideos(movieId: Int, context: Context){
        viewModelScope.launch {
            kotlin.runCatching {
                _getMovieVideos.value = BaseResponse.Loading()
                repo.getMovieVideos(movieId)
            }.onSuccess { data ->
                _getMovieVideos.postValue(BaseResponse.Success(data))
            }.onFailure {
                if (NetworkUtil.isNetworkConnected(context))
                    _getMovieVideos.postValue(BaseResponse.Error(it, 2))
                else _getMovieVideos.postValue(BaseResponse.Error(it, 2, context.getString(R.string.error_connection)))
            }
        }
    }
}
