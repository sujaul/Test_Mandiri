package com.chareem.master.feature

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.chareem.core.BaseActivity
import com.chareem.master.R
import com.chareem.master.databinding.ActivityImageViewBinding


class ImageViewActivity : BaseActivity<ActivityImageViewBinding>() {

    val TAG = javaClass.simpleName

    private var position: String = ""
    private var image_name: String = ""
    private var image_url: String = ""
    companion object {
        @JvmStatic
        fun newInstance(context: Context, position: String, image_name : String, image_url : String): Intent {
            val intent = Intent(context, ImageViewActivity::class.java)
            intent.putExtra("position", position)
            intent.putExtra("image_name", image_name)
            intent.putExtra("image_url", image_url)
            return intent
        }
    }
    override fun setBinding(): ActivityImageViewBinding {
        return ActivityImageViewBinding.inflate(layoutInflater)
    }

    override fun getTagName(): String = TAG

    override fun onCreateUI(savedInstanceState: Bundle?) {
        initViews()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun initViews() {
        position = intent.getStringExtra("position") ?: ""
        image_name = intent.getStringExtra("image_name") ?: ""
        image_url = intent.getStringExtra("image_url") ?: ""
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.ic_baseline_groups_24)
            .error(R.drawable.ic_baseline_groups_24)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)
        Glide.with(this@ImageViewActivity)
            .load(image_url)
            .apply(options)
            .into(binding.image)
        binding.date.text = image_name
        //user.text = users
        binding.lblCount.text = position
        binding.imgback.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
