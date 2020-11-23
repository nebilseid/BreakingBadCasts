package com.example.breakb.app.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.breakb.app.R
import com.example.breakb.app.data.remote.BreakingBadService
import com.example.breakb.app.data.util.RetrofitBuilder
import com.example.breakb.app.model.Character
import com.example.breakb.app.util.Resource
import com.example.breakb.app.util.Status
import com.example.breakb.app.util.ViewModelFactory
import com.example.breakb.app.util.loadUrl
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var characterDetails: Resource<List<Character>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val itemId = intent.getIntExtra(EXTRA_ID, 0)

        detailViewModel = ViewModelProvider(
            this,
            ViewModelFactory(BreakingBadService(RetrofitBuilder.apiService))
        ).get(DetailViewModel::class.java)

        detailViewModel.characterDetails.observe(this, Observer {
            when(it.status){
                Status.SUCCESS -> {
                    characterDetails = it
                    assignDataToUi(characterDetails)
                }
                Status.LOADING -> {
                    //
                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        detailViewModel.receiveCharacterDetails(itemId)

    }

    private fun assignDataToUi(character: Resource<List<Character>>){
        characterNameTv.text = character.data?.get(0)?.name
        occupationTv.text ="""${resources.getString(R.string.occupation)}: ${character.data?.get(0)?.occupation.toString()}"""
        statusTv.text ="""${resources.getString(R.string.status)}: ${character.data?.get(0)?.status.toString()}"""
        nicknameTv.text ="""${resources.getString(R.string.nickname)}: ${character.data?.get(0)?.nickname.toString()}"""
        seasonAppearanceTv.text ="""${resources.getString(R.string.season_appearances)}: ${character.data?.get(0)?.appearance.toString().toString()}"""
        characterImageIv.loadUrl(character.data!![0].img)

    }

    companion object {
        const val EXTRA_ID = "DetailActivity:id"
    }
}