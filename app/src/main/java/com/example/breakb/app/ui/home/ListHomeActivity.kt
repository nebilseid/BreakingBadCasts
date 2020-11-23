package com.example.breakb.app.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.breakb.app.R
import com.example.breakb.app.data.remote.BreakingBadService
import com.example.breakb.app.data.util.RetrofitBuilder
import com.example.breakb.app.databinding.ActivityMainBinding
import com.example.breakb.app.model.Character
import com.example.breakb.app.ui.detail.DetailActivity
import com.example.breakb.app.util.Status
import com.example.breakb.app.util.ViewModelFactory
import com.example.breakb.app.util.startActivity

class ListHomeActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: ActivityMainBinding
    private val adapter = HomeAdapter { viewModel.onItemClicked(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.charactersRv.layoutManager = LinearLayoutManager(this)
        binding.charactersRv.adapter = adapter
        binding.characterSearch.setOnQueryTextListener(this)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(BreakingBadService(RetrofitBuilder.apiService))
        ).get(HomeViewModel::class.java)

        viewModel.characters.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    it.data?.let { characters -> adapter.characterList = characters }
                }
                Status.LOADING -> {
                    binding.progress.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    //Handle Error
                    binding.progress.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        viewModel.navigateToDetail.observe(this, Observer {
            navigateToDetail(it.getContentIfNotHandled() ?: 0)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val season = when (item.itemId) {
            R.id.filter1 -> 1
            R.id.filter2 -> 2
            R.id.filter3 -> 3
            R.id.filter4 -> 4
            R.id.filter5 -> 5
            else -> 0
        }
        viewModel.onFilterSeasonClicked(season)
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        search(newText)
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        search(query)
        return true
    }

    private fun search(searchTerm: String?) {
        Log.d(TAG, "search: $searchTerm")
        viewModel.onCharacterNameSearch(searchTerm ?: "")
    }

    private fun navigateToDetail(itemId: Int) {
        startActivity<DetailActivity>(DetailActivity.EXTRA_ID to itemId)
    }

    companion object {
        const val TAG = "_TAG"
    }
}