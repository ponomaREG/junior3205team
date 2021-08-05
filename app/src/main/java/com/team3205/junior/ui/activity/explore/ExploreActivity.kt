package com.team3205.junior.ui.activity.explore

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.work.*
import com.team3205.junior.R
import com.team3205.junior.data.repository.entities.RecentSearch
import com.team3205.junior.data.repository.entities.Repository
import com.team3205.junior.databinding.ActivitySearchBinding
import com.team3205.junior.ui.activity.saved.SavedReposActivity
import com.team3205.junior.ui.adapter.RecentSearchesAdapter
import com.team3205.junior.ui.adapter.ReposAdapter
import com.team3205.junior.ui.decorators.OffsetItemDecorator
import com.team3205.junior.ui.service.DownloadForegroundWorkManager
import dagger.hilt.android.AndroidEntryPoint

/**
 *  Активи поиска репозиториев
 *  @property viewModel - вьюмодель
 *  @property recentSearchesAdapter - адаптер найденных репозиториев
 *  @property binding - биндинг
 *  @property PERMISSIONS_STORAGE - разрешения для записи в папку Downloads
 */
@AndroidEntryPoint
class ExploreActivity : AppCompatActivity() {
    private val viewModel: ExploreViewModel by viewModels()
    private lateinit var reposAdapter: ReposAdapter
    private lateinit var recentSearchesAdapter : RecentSearchesAdapter
    private lateinit var binding: ActivitySearchBinding
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    /**
     * Создание активити
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()
        setupActionBar()
        initAdapters()
        attachAdapters()
        attachDecorations()
        initListeners()
        attachTextWatcherToInputField()
        attachListenersToLiveDataInViewModel()
        showRecentSearchedGroup()
    }

    /**
     *  Создание опций меню
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_toolbar_menu, menu)
        return true
    }

    /**
     * Колбек при выборе элемента меню
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_search_ic_saved_repos){
            val intent = Intent(this, SavedReposActivity::class.java)
            startActivity(intent)
        }
        return true
    }

    /**
     * Иниилизация биндинга
     */
    private fun initViewBinding(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
    }

    /**
     * Установка actionBar
     */
    private fun setupActionBar(){
        setSupportActionBar(binding.searchToolBar)
        supportActionBar?.setTitle(R.string.search_toolbar_title)
    }

    /**
     * Иницилизация адаптеров
     */
    private fun initAdapters(){
        reposAdapter = ReposAdapter(
            onReposClick = this::ueOnReposClick,
            onReposButtonDownloadClick = this::ueOnReposDownloadClick
        )
        recentSearchesAdapter = RecentSearchesAdapter(
            onRecentSearchClick = this::ueOnRecentSearchClick
        )
    }

    /**
     * Присваивание адаптеров к recyclerView
     */
    private fun attachAdapters(){
        binding.apply {
            searchRepos.adapter = reposAdapter
            searchRecentSearches.adapter = recentSearchesAdapter
        }
    }

    /**
     * Присваивание декораторов recyclerView
     */
    private fun attachDecorations(){
        with(resources) {
            val decoratorRepos = OffsetItemDecorator(
                left = getDimensionPixelSize(R.dimen.item_repository_offset_left),
                right = getDimensionPixelSize(R.dimen.item_repository_offset_right),
                top = getDimensionPixelSize(R.dimen.item_repository_offset_top),
                bottom = getDimensionPixelSize(R.dimen.item_repository_offset_bottom)
            )
            binding.searchRepos.addItemDecoration(decoratorRepos)
            binding.searchRepos.addItemDecoration(
                DividerItemDecoration(
                    this@ExploreActivity, DividerItemDecoration.VERTICAL
                )
            )
            val decoratorRecentSearches = OffsetItemDecorator(
                left = getDimensionPixelSize(R.dimen.item_recent_search_offset_left),
                right = getDimensionPixelSize(R.dimen.item_recent_search_offset_right),
                top = getDimensionPixelSize(R.dimen.item_recent_search_offset_top),
                bottom = getDimensionPixelSize(R.dimen.item_recent_search_offset_bottom)
            )
            binding.searchRecentSearches.addItemDecoration(decoratorRecentSearches)
            binding.searchRecentSearches.addItemDecoration(
                DividerItemDecoration(
                    this@ExploreActivity, DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    /**
     * Иницилизация слушателей
     */
    private fun initListeners(){
        binding.apply {
            searchButtonCancel.setOnClickListener {
                searchButtonCancel.visibility = View.GONE
                searchInput.text = Editable.Factory.getInstance().newEditable("")
                hideKeyboard()
                currentFocus?.clearFocus()
                reposAdapter.clear()
                showRecentSearchedGroup()
            }
            searchButtonCommit.setOnClickListener {
                val input = searchInput.text
                if(input.isNullOrEmpty().not()) {
                    reposAdapter.clear()
                    viewModel.loadRepos(input.toString())
                    hideKeyboard()
                    hideRecentSearchedGroup()
                    currentFocus?.clearFocus()
                }
                else Toast.makeText(this@ExploreActivity,
                    getString(R.string.search_error_empty_input),
                    Toast.LENGTH_SHORT).show()
            }
            searchRecentSearchesButtonClear.setOnClickListener {
                recentSearchesAdapter.clearAll()
                viewModel.clearRecentSearches()
            }
        }
    }

    /**
     * Присваивание слушателя текста к полю поиска
     */
    private fun attachTextWatcherToInputField(){
        binding.searchInput.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
            }

            override fun onTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
                binding.apply {
                    if (p0.isNullOrEmpty().not()) {
                        searchButtonCancel.visibility = View.VISIBLE
                    }else{
                        searchButtonCancel.visibility = View.GONE
                    }
                    if(reposAdapter.itemCount == 0) showRecentSearchedGroup()
                    else hideRecentSearchedGroup()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    /**
     * Присваивание слушателей данных к живым данным во вьюмоделе
     */
    private fun attachListenersToLiveDataInViewModel(){
        viewModel.apply {
            isLoading.observe(this@ExploreActivity){
                binding.searchProgress.visibility = if(it) View.VISIBLE else View.INVISIBLE
                binding.searchButtonCommit.visibility = if(it) View.INVISIBLE else View.VISIBLE
            }
            repos.observe(this@ExploreActivity){ data ->
                reposAdapter.addItems(data)
            }
            recentSearches.observe(this@ExploreActivity){ recentSearches ->
                recentSearchesAdapter.addItems(recentSearches)
            }
        }
    }

    /**
     * Спрятать клавиаутуру
     */
    private fun hideKeyboard() {
        val imm: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Спрятать группу вью истории поиска
     */
    private fun hideRecentSearchedGroup(){
        binding.searchGroupRecentSearches.visibility = View.GONE
    }

    /**
     * Показать группу вью истории поиска
     */
    private fun showRecentSearchedGroup(){
        binding.searchGroupRecentSearches.visibility = View.VISIBLE
    }

    /**
     * Пользовательское взаимодействие: нажатие на историю поиска
     */
    private fun ueOnRecentSearchClick(recentSearch: RecentSearch){
        binding.searchInput.text = Editable.Factory.getInstance().newEditable(recentSearch.search)
    }

    /**
     * Пользовательское взаимодействие: нажатие на репозитории
     */
    private fun ueOnReposClick(repository: Repository){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(repository.url)
        startActivity(intent)
    }

    /**
     * Пользовательское взаимодействие: нажатие на кнопку скачивания репозитория
     */
    private fun ueOnReposDownloadClick(repository: Repository, position: Int){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            if(ifExistsPermissions()){
                startDownloadingRepo(repository, position)
                viewModel.saveRepoToDb(repository)
            }else requestStoragePermissions()
        }else {
            startDownloadingRepo(repository, position)
            viewModel.saveRepoToDb(repository)
        }
    }

    /**
     * Начать скачивание репозитория
     */
    private fun startDownloadingRepo(repository: Repository, position: Int){
        val data = Data.Builder()
            .putString(DownloadForegroundWorkManager.KEY_NAME, repository.name)
            .putInt(DownloadForegroundWorkManager.KEY_ID, repository.id)
            .putString(DownloadForegroundWorkManager.KEY_URL, repository.getUrlForDownload())
            .build()
        val request = OneTimeWorkRequest.Builder(DownloadForegroundWorkManager::class.java)
                .setInputData(data)
                .setConstraints(getConstraintsForDownloading())
                .build()
        val workManager = WorkManager.getInstance(this)
        workManager.getWorkInfoByIdLiveData(request.id)
            .observe(this){ workInfo ->
                when(workInfo.state){
                    WorkInfo.State.SUCCEEDED -> {
                        reposAdapter.setStatusOfRepo(position, ReposAdapter.PAYLOAD_SUCCESS_DOWNLOADING)
                    }
                    WorkInfo.State.RUNNING -> {
                        reposAdapter.setStatusOfRepo(position, ReposAdapter.PAYLOAD_START_DOWNLOADING)
                    }
                    WorkInfo.State.FAILED -> {
                        reposAdapter.setStatusOfRepo(position, ReposAdapter.PAYLOAD_FAILED_DOWNLOADING)
                    }
                    else -> {}
                }
            }
        workManager.enqueue(request)
    }

    /**
     * Получение ограничений на скачивание
     */
    private fun getConstraintsForDownloading(): Constraints{
        return Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
    }

    /**
     * Запрос разрешений на запись данных
     */
    private fun requestStoragePermissions() {
        ActivityCompat.requestPermissions(
            this,
            PERMISSIONS_STORAGE,
            1
        )
    }

    /**
     * Проверка существования разрешений на запись
     */
    private fun ifExistsPermissions(): Boolean{
        val permission = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return permission == PackageManager.PERMISSION_GRANTED
    }
}