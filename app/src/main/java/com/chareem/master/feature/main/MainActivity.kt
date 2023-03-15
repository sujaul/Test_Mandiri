package com.chareem.master.feature.main

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.chareem.core.BaseActivity
import com.chareem.master.R
import com.chareem.master.databinding.ActivityMainBinding
import com.chareem.master.util.gone
import com.chareem.master.util.visible
import org.koin.core.component.KoinComponent

class MainActivity : BaseActivity<ActivityMainBinding>(), KoinComponent {

    val TAG = javaClass.simpleName


    private val navController by lazy {
        findNavController(R.id.nav_host_fragment)
    }

    override fun getTagName(): String = TAG

    override fun onCreateUI(savedInstanceState: Bundle?) {
        //init()
    }

    override fun onDenyPermission(level: Int) {
        super.onDenyPermission(level)
        rationaleCallback()
    }

    private fun init(){
        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.mobile_navigation)
        navController.setGraph(graph, bundle)
    }

    fun setTittel(tittle: String, isVisibleToolbar: Boolean = true, isVisibleBack : Boolean = true){
        binding.titleBar.text = tittle
        if (isVisibleToolbar) {
            setSupportActionBar(binding.toolbar)
            binding.imgBack.setOnClickListener {
                super.onBackPressed()
            }
            binding.appbar.visible()
            binding.imgBack.isVisible = isVisibleBack
        }
        else {
            setSupportActionBar(binding.toolbar)
            binding.imgBack.setOnClickListener(null)
            binding.appbar.gone()
        }
    }

    override fun setBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }
}