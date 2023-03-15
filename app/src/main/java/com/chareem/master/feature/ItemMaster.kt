package com.chareem.master.feature

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.chareem.master.R
import com.chareem.master.data.model.*
import com.chareem.master.databinding.ItemGenreBinding
import com.chareem.master.databinding.ItemMovieBinding
import com.chareem.master.databinding.ItemReviewBinding
import com.chareem.master.feature.main.second.SecondFragment
import com.chareem.master.util.DateOperationUtil
import com.chareem.master.util.UserInterfaceUtil
import com.xwray.groupie.viewbinding.BindableItem


class ItemMovieGenre(val movieGenre: MovieGenre, val viewListener: (MovieGenre) -> Unit): BindableItem<ItemGenreBinding>() {
    override fun bind(viewBinding: ItemGenreBinding, position: Int) {
        viewBinding.genreTv.text = UserInterfaceUtil.capitalizeFirstString(movieGenre.name)
        viewBinding.genreTv.setOnClickListener {
            viewListener.invoke(movieGenre)
        }
    }

    override fun getLayout(): Int = R.layout.item_genre

    override fun initializeViewBinding(view: View): ItemGenreBinding {
        return ItemGenreBinding.bind(view)
    }
}

class ItemMovieByGenre(val movieByGenre: MovieByGenre, val viewDetailListener: (MovieByGenre) -> Unit,
                       val reviewListener: (MovieByGenre) -> Unit, val viewImageListener: (MovieByGenre) -> Unit
): BindableItem<ItemMovieBinding>() {
    override fun bind(viewBinding: ItemMovieBinding, position: Int) {
        viewBinding.titleTv.text = UserInterfaceUtil.capitalizeFirstString(movieByGenre.title)
        viewBinding.descriptionTv.text = UserInterfaceUtil.capitalizeFirstString(movieByGenre.overview)
        viewBinding.releaseDateTv.text = UserInterfaceUtil.capitalizeFirstString(movieByGenre.release_date)
        viewBinding.itemMovieContainer.setOnClickListener {
            viewDetailListener.invoke(movieByGenre)
        }
        viewBinding.reviewBt.setOnClickListener {
            reviewListener.invoke(movieByGenre)
        }
        viewBinding.posterIv.setOnClickListener {
            viewImageListener.invoke(movieByGenre)
        }
        val basePathImmage = SecondFragment.basePathImmage
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.ic_baseline_groups_24)
            .error(R.drawable.ic_baseline_groups_24)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)
        Glide.with(viewBinding.root.context)
            .load("$basePathImmage${movieByGenre.poster_path}")
            .apply(options)
            .into(viewBinding.posterIv)
    }

    override fun getLayout(): Int = R.layout.item_movie

    override fun initializeViewBinding(view: View): ItemMovieBinding {
        return ItemMovieBinding.bind(view)
    }
}

class ItemReview(val movieReview: MovieReview): BindableItem<ItemReviewBinding>() {
    override fun bind(viewBinding: ItemReviewBinding, position: Int) {
        viewBinding.titleTv.text = UserInterfaceUtil.capitalizeFirstString(movieReview.author)
        viewBinding.descriptionTv.text = UserInterfaceUtil.capitalizeFirstString(movieReview.content)
        viewBinding.dateTv.text = DateOperationUtil.dateStrFormat("yyyy-MM-dd HH-mm",movieReview.updated_at)
        val authorAvatar = movieReview.author_details?.avatar_path
        if (authorAvatar != null){
            val basePathImmage = SecondFragment.basePathImmage
            val options: RequestOptions = RequestOptions()
                .placeholder(R.drawable.ic_baseline_groups_24)
                .error(R.drawable.ic_baseline_groups_24)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .circleCrop()
            if (authorAvatar.contains("http")){
                Glide.with(viewBinding.root.context)
                    .load(authorAvatar.substring(1))
                    .apply(options)
                    .into(viewBinding.avatarIv)
            } else {
                Glide.with(viewBinding.root.context)
                    .load("$basePathImmage$authorAvatar")
                    .apply(options)
                    .into(viewBinding.avatarIv)
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_review

    override fun initializeViewBinding(view: View): ItemReviewBinding {
        return ItemReviewBinding.bind(view)
    }
}

