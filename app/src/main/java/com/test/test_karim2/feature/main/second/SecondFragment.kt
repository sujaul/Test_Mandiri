package com.test.test_karim2.feature.main.second

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.chareem.core.BaseFragment
import com.chareem.core.data.BaseResponse
import com.chareem.core.helper.widget.MessageType
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.test.test_karim2.R
import com.test.test_karim2.data.model.MovieByGenre
import com.test.test_karim2.data.model.MovieDetail
import com.test.test_karim2.databinding.FragmentSecondBinding
import com.test.test_karim2.feature.*
import com.test.test_karim2.feature.main.MainActivity
import com.test.test_karim2.util.gone
import com.test.test_karim2.util.visible
import com.xwray.groupie.Group
import com.xwray.groupie.GroupieAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class SecondFragment : BaseFragment<FragmentSecondBinding>() {

    val TAG = this.javaClass.simpleName
    companion object {
        val basePathImmage = "https://image.tmdb.org/t/p/w500"
    }

    private val args: SecondFragmentArgs by navArgs()
    private val navController by lazy { findNavController() }
    private val movieByGenreAdapter = GroupieAdapter()
    private val vmSecond: SecondVM by viewModel()
    private var page = 1
    private var isLoadMore = false
    private var isRefreshing = false
    private var isEmptyLoadMore = false
    private var movieByGenre: MovieByGenre? =  null

    override fun onResume() {
        super.onResume()
        val activity = mActivity as MainActivity
        activity.setTittel("${args.genreName} Movie")
    }

    override fun getTagName(): String = TAG
    override fun onCreateUI(view: View, savedInstanceState: Bundle?) {
        init()
    }

    private fun init(){
        setupRecycler()

        binding.refreshSrl.setOnRefreshListener {
            if (!isLoadMore && !isRefreshing){
                isRefreshing = true
                vmSecond.getMovieByGenre(args.genreId, 1, mContext)
            } else binding.refreshSrl.isRefreshing = false
        }

        binding.scrollNs.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight &&
                    scrollY > oldScrollY
                ) {
                   /* binding.scrollNs.clearFocus()
                    binding.movieRv.clearFocus()
                    binding.movieRv.clearOnScrollListeners()*/
                    if (!isRefreshing && !isLoadMore && !isEmptyLoadMore){
                        isLoadMore = true
                        vmSecond.getMovieByGenre(args.genreId, page+1, mContext)
                    }
                }
            }
        })

        if (movieByGenreAdapter.itemCount == 0) {
            observVm()
            vmSecond.getMovieByGenre(args.genreId, page, mContext)
        }
    }

    private fun setupRecycler(){
        val recycler = binding.movieRv
        recycler.apply {
            val linearLayout = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            layoutManager = linearLayout
            adapter = movieByGenreAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun observVm(){
        vmSecond.getMovieByGenre.observe(this) { response ->
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
                        } else {
                            val groupList = ArrayList<Group>()
                            response.data.map {
                                groupList.add(ItemMovieByGenre(it, {
                                    vmSecond.getMovieDetail(it.id, mContext)
                                }, {
                                    movieByGenre = it
                                    vmSecond.getMovieVideos(it.id, mContext)
                                }, {
                                    startActivity(ImageViewActivity.newInstance(mContext, it.release_date,
                                        it.title, "$basePathImmage${it.poster_path}"))
                                }))
                            }
                            binding.movieRv.post {
                                movieByGenreAdapter.addAll(groupList)
                            }
                        }
                        page++
                        isLoadMore = false
                        binding.loadingContent.loadingLl.gone()
                    } else {
                        binding.refreshSrl.isRefreshing = false
                        if (response.data.isNotEmpty()){
                            movieByGenreAdapter.clear()
                            val groupList = ArrayList<Group>()
                            response.data.map {
                                groupList.add(ItemMovieByGenre(it, {
                                    vmSecond.getMovieDetail(it.id, mContext)
                                }, {
                                    movieByGenre = it
                                    vmSecond.getMovieVideos(it.id, mContext)
                                }, {
                                    startActivity(ImageViewActivity.newInstance(mContext, it.release_date,
                                        it.title, "$basePathImmage${it.poster_path}"))
                                }))
                            }
                            movieByGenreAdapter.addAll(groupList)
                        }
                        binding.emptyContent.emptyRl.isVisible = response.data.isEmpty()
                        binding.movieRv.isVisible = response.data.isNotEmpty()
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

        vmSecond.getMovieDetail.observe(this) { response ->
            when (response) {
                is BaseResponse.Loading -> {
                    showDialogLoading("Please wait...")
                }
                is BaseResponse.Success -> {
                    hideDialogLoading()
                    showBottomMovieDetail(response.data)
                }
                is BaseResponse.Error -> {
                    hideDialogLoading()
                    showSnackBarMessage(response.message, MessageType.Danger)
                }
            }
        }

        vmSecond.getMovieVideos.observe(this) { response ->
            when (response) {
                is BaseResponse.Loading -> {
                    showDialogLoading("Please wait...")
                }
                is BaseResponse.Success -> {
                    hideDialogLoading()
                    val data = response.data.results
                    if (data.isEmpty()){
                        openThirdScreen(response.data.id, "")
                    } else {
                        for (movievideo in data){
                            if (movievideo.type == "Trailer" && movievideo.site == "YouTube"){
                                openThirdScreen(response.data.id, movievideo.key)
                                break
                            }
                        }
                    }
                }
                is BaseResponse.Error -> {
                    hideDialogLoading()
                    showSnackBarMessage(response.message, MessageType.Danger)
                }
            }
        }
    }

    override fun setBinding(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Boolean): FragmentSecondBinding {
        return FragmentSecondBinding.inflate(inflater, container, savedInstanceState)
    }

    fun showBottomMovieDetail(movieDetail: MovieDetail){
        val view: View = layoutInflater.inflate(R.layout.item_movie_detail, null)
        val tvTittle = view.findViewById<TextView>(R.id.title_tv)
        tvTittle.text = movieDetail.title
        val posterIv = view.findViewById<ImageView>(R.id.poster_iv)
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.ic_baseline_groups_24)
            .error(R.drawable.ic_baseline_groups_24)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)
        Glide.with(mContext)
            .load("$basePathImmage${movieDetail.poster_path}")
            .apply(options)
            .into(posterIv)
        val tvDescription = view.findViewById<TextView>(R.id.description_tv)
        tvDescription.text = movieDetail.overview
        val tvOtherDesc = view.findViewById<TextView>(R.id.other_desc_tv)
        var genre = ""
        movieDetail.genres.forEachIndexed { index, it ->
            if (index != 0) genre += ", "
            genre += it.name
        }
        var productionCompany = ""
        movieDetail.production_companies.forEachIndexed { index, it ->
            if (index != 0) productionCompany += ", "
            productionCompany += it.name
        }
        var productionCountries = ""
        movieDetail.production_countries.forEachIndexed { index, it ->
            if (index != 0) productionCountries += ", "
            productionCountries += it.name
        }
        var spokenLanguage = ""
        movieDetail.spoken_languages.forEachIndexed { index, it ->
            if (index != 0) spokenLanguage += ", "
            spokenLanguage += it.english_name
        }
        val text = StringBuilder()
        text.append("<b><font color=#000000>Genre</font><br></b><font color=#757575>$genre</font><br>")
        text.append("<b><font color=#000000>Home Page</font><br></b><font color=#757575>${movieDetail.homepage}</font><br>")
        text.append("<b><font color=#000000>Budget</font><br></b><font color=#757575>${movieDetail.budget}</font><br>")
        text.append("<b><font color=#000000>Orignal Language</font><br></b><font color=#757575>${movieDetail.original_language}</font><br>")
        text.append("<b><font color=#000000>Original Title</font><br></b><font color=#757575>${movieDetail.original_title}</font><br>")
        text.append("<b><font color=#000000>Popularity</font><br></b><font color=#757575>${movieDetail.popularity}</font><br>")
        text.append("<b><font color=#000000>Production Companys</font><br></b><font color=#757575>$productionCompany</font><br>")
        text.append("<b><font color=#000000>Production Countries</font><br></b><font color=#757575>$productionCountries</font><br>")
        text.append("<b><font color=#000000>Release Date</font><br></b><font color=#757575>${movieDetail.release_date}</font><br>")
        text.append("<b><font color=#000000>Revenue</font><br></b><font color=#757575>${movieDetail.revenue}</font><br>")
        text.append("<b><font color=#000000>Spoken Languages</font><br></b><font color=#757575>$spokenLanguage</font><br>")
        text.append("<b><font color=#000000>Status</font><br></b><font color=#757575>${movieDetail.status}</font><br>")
        text.append("<b><font color=#000000>Tagline</font><br></b><font color=#757575>${movieDetail.tagline}</font><br>")
        text.append("<b><font color=#000000>Vote Average</font><br></b><font color=#757575>${movieDetail.vote_average}</font><br>")
        text.append("<b><font color=#000000>Vote Count</font><br></b><font color=#757575>${movieDetail.vote_count}</font><br>")
        tvOtherDesc.text = HtmlCompat.fromHtml(text.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
        val dialog = BottomSheetDialog(mContext)
        dialog.setContentView(view)
        dialog.setCancelable(true)
        (view.parent as View).setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent))
        dialog.show()
    }

    private fun openThirdScreen(movieId: Int, youtubePath: String){
        val action = SecondFragmentDirections.actionSecondFragmentToThirdFragment(movieId, movieByGenre?.title ?: "", youtubePath)
        navController.navigate(action)
    }
}
