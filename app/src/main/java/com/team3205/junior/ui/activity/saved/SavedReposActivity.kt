package com.team3205.junior.ui.activity.saved

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import com.team3205.junior.R
import com.team3205.junior.data.repository.entities.Repository
import com.team3205.junior.databinding.ActivitySavedReposBinding
import com.team3205.junior.ui.adapter.ReposAdapter
import com.team3205.junior.ui.decorators.OffsetItemDecorator
import dagger.hilt.android.AndroidEntryPoint

/**
 *  Активи с сохраненными репозиториями
 *  @property viewModel - вьюМодель
 *  @property binding - биндинг
 *  @property reposAdapter - адаптер с репозиториями
 */
@AndroidEntryPoint
class SavedReposActivity : AppCompatActivity() {
    private val viewModel: SavedReposViewModel by viewModels()
    private lateinit var binding: ActivitySavedReposBinding
    private lateinit var reposAdapter: ReposAdapter

    /**
     * Создание активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initActionBar()
        initAdapters()
        attachAdaptersToRecyclerView()
        attachDecorators()
        attachListenersToLiveDataInViewModel()
    }

    /**
     * Колбек при выборе элемента меню
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return true
    }

    /**
     * Иницилизация биндинга
     */
    private fun initBinding(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_saved_repos)
    }

    /**
     * Иницилизация actionBar
     */
    private fun initActionBar(){
        setSupportActionBar(binding.savedToolBar)
        supportActionBar?.setTitle(R.string.saved_toolbar_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
    }

    /**
     * Иницилизация адаптеров
     */
    private fun initAdapters(){
        reposAdapter = ReposAdapter(
            onReposClick = this::ueOnRepoClick
        )
    }

    /**
     * Присваивание адаптеров к recyclerView
     */
    private fun attachAdaptersToRecyclerView(){
        binding.savedRepositories.adapter = reposAdapter
    }

    /**
     * Присваивание декораторов
     */
    private fun attachDecorators(){
        with(resources) {
            val decoratorRepos = OffsetItemDecorator(
                left = getDimensionPixelSize(R.dimen.item_repository_offset_left),
                right = getDimensionPixelSize(R.dimen.item_repository_offset_right),
                top = getDimensionPixelSize(R.dimen.item_repository_offset_top),
                bottom = getDimensionPixelSize(R.dimen.item_repository_offset_bottom)
            )
            binding.savedRepositories.addItemDecoration(decoratorRepos)
            binding.savedRepositories.addItemDecoration(
                DividerItemDecoration(
                    this@SavedReposActivity, DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    /**
     * Присваивание слушателей данных к источникам живых данных во вьюмодели
     */
    private fun attachListenersToLiveDataInViewModel(){
        viewModel.savedRepo.observe(this){
            reposAdapter.addItems(it)
        }
        viewModel.loading.observe(this){
            binding.savedLoadingIndicator.visibility = if(it) View.VISIBLE else View.INVISIBLE
        }
    }

    /**
     * Пользовательское взаимодействие: нажатие на репозиторий
     */
    private fun ueOnRepoClick(repository: Repository){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(repository.url)
        startActivity(intent)
    }
}