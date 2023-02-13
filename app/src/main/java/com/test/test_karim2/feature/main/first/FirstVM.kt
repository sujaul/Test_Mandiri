package com.test.test_karim2.feature.main.first

import android.content.Context
import androidx.lifecycle.*
import com.chareem.core.BaseVM
import com.chareem.core.data.BaseResponse
import com.chareem.core.util.NetworkUtil
import com.test.test_karim2.R
import com.test.test_karim2.Repository.globalRepository
import com.test.test_karim2.data.model.MovieGenre
import kotlinx.coroutines.launch

class FirstVM(private val repo: globalRepository): BaseVM() {
    override fun getTagName(): String = javaClass.simpleName

    private val _movieGenre = MutableLiveData<BaseResponse<List<MovieGenre>>>()
    val movieGenre: LiveData<BaseResponse<List<MovieGenre>>> = _movieGenre

    fun getMovieGenre(){
        viewModelScope.launch {
            kotlin.runCatching {
                _movieGenre.value = BaseResponse.Loading()
                repo.getGenreLocal()
            }.onSuccess { data ->
                data.collect {
                    _movieGenre.postValue(BaseResponse.Success(it))
                }
            }.onFailure {
                _movieGenre.postValue(BaseResponse.Error(it, 2))
            }
        }
    }

    private val _movieGenreServer = MutableLiveData<BaseResponse<List<MovieGenre>>>()
    val movieGenreServer: LiveData<BaseResponse<List<MovieGenre>>> = _movieGenreServer

    fun getMovieGenreServer(context: Context){
        viewModelScope.launch {
            kotlin.runCatching {
                _movieGenreServer.value = BaseResponse.Loading()
                repo.getGenreServer()
            }.onSuccess {
                _movieGenreServer.postValue(BaseResponse.Success(listOf()))
            }.onFailure {
                if (NetworkUtil.isNetworkConnected(context))
                    _movieGenreServer.postValue(BaseResponse.Error(it, 2))
                else _movieGenreServer.postValue(BaseResponse.Error(it, 2, context.getString(R.string.error_connection)))
            }
        }
    }
}
