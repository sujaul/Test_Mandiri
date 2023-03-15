package com.chareem.master.feature.main.first

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.chareem.core.BaseFragment
import com.chareem.core.data.BaseResponse
import com.chareem.core.helper.widget.MessageType
import com.chareem.master.data.model.MovieGenre
import com.chareem.master.databinding.FragmentFirstBinding
import com.chareem.master.feature.ItemMovieGenre
import com.chareem.master.feature.main.MainActivity
import com.chareem.master.util.gone
import com.chareem.master.util.visible
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.koin.androidx.viewmodel.ext.android.viewModel


class FirstFragment : BaseFragment<FragmentFirstBinding>() {

    val TAG = this.javaClass.simpleName

    private val genreGroupAdapter = GroupAdapter<GroupieViewHolder>()
    private val navController by lazy { findNavController() }
    private val vmFirst: FirstVM by viewModel()

    override fun onResume() {
        super.onResume()
        val activity = mActivity as MainActivity
        activity.setTittel("Movie Genre", isVisibleBack = false)
    }

    override fun getTagName(): String = TAG
    override fun onCreateUI(view: View, savedInstanceState: Bundle?) {
        init()
    }

    private fun init(){
        setupRecycler()
        binding.genreSrl.setOnRefreshListener {
            vmFirst.getMovieGenreServer(mContext)
        }
        if (genreGroupAdapter.itemCount == 0) {
            observVm()
            vmFirst.getMovieGenre()
        }
    }

    private fun setupRecycler(){
        val recycler = binding.genreRv
        recycler.apply {
            val linearLayout = GridLayoutManager(mContext, 2)
            layoutManager = linearLayout
            adapter = genreGroupAdapter
        }
    }

    private fun observVm(){
        var isEverLoadGenreToserver = false
        vmFirst.movieGenre.observe(this) { response ->
            when (response) {
                is BaseResponse.Loading -> {
                    binding.genreSrl.isRefreshing = true
                }
                is BaseResponse.Success -> {
                    binding.genreSrl.isRefreshing = false
                    genreGroupAdapter.clear()
                    if (response.data.isEmpty()){
                        if (!isEverLoadGenreToserver){
                            vmFirst.getMovieGenreServer(mContext)
                            isEverLoadGenreToserver = true
                        }
                        binding.emptyContent.emptyRl.visible()
                        binding.genreRv.gone()
                    } else {
                        binding.emptyContent.emptyRl.gone()
                        binding.genreRv.visible()
                        response.data.map {
                            genreGroupAdapter.add(ItemMovieGenre(it) {
                                openSecondScreen(it)
                            })
                        }
                    }
                }
                is BaseResponse.Error -> {
                    binding.genreSrl.isRefreshing = false
                    showSnackBarMessage(response.message, MessageType.Danger)
                }
            }
        }

        vmFirst.movieGenreServer.observe(this) { response ->
            when (response) {
                is BaseResponse.Loading -> {
                    binding.genreSrl.isRefreshing = true
                }
                is BaseResponse.Success -> {
                    binding.genreSrl.isRefreshing = false
                }
                is BaseResponse.Error -> {
                    binding.genreSrl.isRefreshing = false
                    showSnackBarMessage(response.message, MessageType.Danger)
                }
            }
        }
    }

    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Boolean): FragmentFirstBinding {
        return FragmentFirstBinding.inflate(inflater, container, savedInstanceState)
    }

    private fun openSecondScreen(genre: MovieGenre){
        //navController.navigate(R.id.nav_second,bundle)
        val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(genre.id, genre.name)
        navController.navigate(action)
    }
}
