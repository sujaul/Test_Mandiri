package com.chareem.master.feature.main.third

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.chareem.core.BaseFragment
import com.chareem.core.data.BaseResponse
import com.chareem.core.helper.widget.MessageType
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.chareem.master.R
import com.chareem.master.databinding.FragmentThirdBinding
import com.chareem.master.feature.ItemReview
import com.chareem.master.feature.main.MainActivity
import com.chareem.master.util.gone
import com.chareem.master.util.visible
import com.xwray.groupie.Group
import com.xwray.groupie.GroupieAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.ArrayList


class ThirdFragment : BaseFragment<FragmentThirdBinding>() {

    val TAG = this.javaClass.simpleName

    private val vmThird: ThirdVM by viewModel()
    private val movieReviewAdapter = GroupieAdapter()
    /** AndroidX navigation arguments */
    private val args: ThirdFragmentArgs by navArgs()
    private var activity: MainActivity? = null
    private var page = 1
    private var isLoadMore = false
    private var isRefreshing = false
    private var isEmptyLoadMore = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = mActivity as MainActivity
    }

    override fun onResume() {
        super.onResume()
        activity?.setTittel("Movie Triler & Review")
    }

    override fun getTagName(): String = TAG
    override fun onCreateUI(view: View, savedInstanceState: Bundle?) {
        init()
    }

    private fun init(){
        setupRecycler()
        binding.titleTv.text = args.movieName
        binding.refreshSrl.setOnRefreshListener {
            if (!isLoadMore && !isRefreshing){
                isRefreshing = true
                vmThird.getMovieReview(args.movieId, 1, mContext)
            } else binding.refreshSrl.isRefreshing = false
        }
        binding.scrollNs.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight &&
                    scrollY > oldScrollY
                ) {
                    /*binding.scrollNs.clearFocus()
                    binding.reviewRv.clearFocus()
                    binding.reviewRv.clearOnScrollListeners()*/
                    if (!isRefreshing && !isLoadMore && !isEmptyLoadMore){
                        isLoadMore = true
                        vmThird.getMovieReview(args.movieId, page+1, mContext)
                    }
                }
            }
        })
        if (movieReviewAdapter.itemCount == 0) {
            observVm()
            vmThird.getMovieReview(args.movieId, page, mContext)
            lifecycle.addObserver(binding.videoYtp)
        }
        binding.videoYtp.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = args.youtubePath
                youTubePlayer.loadVideo(videoId, 0F)
            }
        })
    }

    private fun setupRecycler(){
        val recycler = binding.reviewRv
        recycler.apply {
            val linearLayout = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            layoutManager = linearLayout
            adapter = movieReviewAdapter
        }
    }

    private fun observVm(){
        vmThird.getMovieReview.observe(this) { response ->
            when (response) {
                is BaseResponse.Loading -> {
                    if (isLoadMore) binding.loadingContent.loadingLl.visible()
                    else binding.refreshSrl.isRefreshing = true
                }
                is BaseResponse.Success -> {
                    if (isLoadMore) {
                        if (response.data.isEmpty()){
                            isEmptyLoadMore = true
                            showSnackBarMessage(getString(R.string.no_data_to_load), MessageType.Info)
                        } else response.data.map {
                            val groupList = ArrayList<Group>()
                            response.data.map {
                                groupList.add(ItemReview(it))
                            }
                            binding.reviewRv.post {
                                movieReviewAdapter.addAll(groupList)
                            }
                        }
                        page++
                        isLoadMore = false
                        binding.loadingContent.loadingLl.gone()
                    } else {
                        binding.refreshSrl.isRefreshing = false
                        if (response.data.isNotEmpty()){
                            movieReviewAdapter.clear()
                            response.data.map {
                                movieReviewAdapter.add(ItemReview(it))
                            }
                        }
                        binding.emptyContent.emptyRl.isVisible = response.data.isEmpty()
                        binding.reviewRv.isVisible = response.data.isNotEmpty()
                        page = 1
                        isRefreshing = false
                    }
                }
                is BaseResponse.Error -> {
                    isRefreshing = false
                    isLoadMore = false
                    if (isLoadMore) binding.loadingContent.loadingLl.gone()
                    else binding.refreshSrl.isRefreshing = false
                    showSnackBarMessage(response.message, MessageType.Danger)
                }
            }
        }
    }

    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Boolean): FragmentThirdBinding {
        return FragmentThirdBinding.inflate(inflater, container, savedInstanceState)
    }
}
