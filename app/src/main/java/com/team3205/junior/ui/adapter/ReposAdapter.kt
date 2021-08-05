package com.team3205.junior.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.team3205.junior.R
import com.team3205.junior.data.repository.entities.Repository
import com.team3205.junior.databinding.ItemRepositoryBinding
import com.team3205.junior.ui.util.loadImage

/**
 * Адаптер для репозиториев
 * @param onReposButtonDownloadClick - колбек нажатия на кнопку скачивания репозитория
 * @param onReposClick - колбек нажатия на репозиторий
 */
class ReposAdapter constructor(
    private val onReposClick:(Repository)-> Unit = {},
    private val onReposButtonDownloadClick:(Repository, Int) -> Unit = {_, _ -> }
) : RecyclerView.Adapter<ReposAdapter.Holder>() {
    private val _items: MutableList<Repository> = mutableListOf()

    companion object{
        const val PAYLOAD_SUCCESS_DOWNLOADING = "PAYLOAD_SUCCESS_DOWNLOADING"
        const val PAYLOAD_START_DOWNLOADING = "PAYLOAD_START_DOWNLOADING"
        const val PAYLOAD_FAILED_DOWNLOADING = "PAYLOAD_FAILED_DOWNLOADING"
    }

    /**
     * Добавление элементов
     */
    fun addItems(items: List<Repository>) {
        val positionStart = _items.size
        _items.addAll(items)
        notifyItemRangeInserted(positionStart, items.size)
    }

    /**
     * Очистка элементов
     */
    fun clear(){
        _items.clear()
        notifyDataSetChanged()
    }

    /**
     * Установка статуса репозитория
     */
    fun setStatusOfRepo(position: Int, payload: Any){
        notifyItemChanged(position,payload)
    }

    class Holder(val binding: ItemRepositoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_repository,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val repos = _items[position]
        holder.binding.apply {
            itemRepositoryName.text = repos.name
            itemRepositoryOwnerUsername.text = repos.owner.login
            itemRepositoryLanguage.text = repos.language ?: "?"
            itemRepositoryStargazers.text = repos.stargazers_count.toString()
            itemRepositoryOwnerAvatar.loadImage(repos.owner.avatar_url)
            if(repos.description == null) itemRepositoryDescription.visibility = View.GONE
            else itemRepositoryDescription.text = repos.description
            if(repos.isSaved){
                itemRepositoryIcSaved.visibility = View.VISIBLE
                itemRepositoryButtonDownload.visibility = View.INVISIBLE
                itemRepositoryProgressDownload.visibility = View.INVISIBLE
            }else{
                itemRepositoryIcSaved.visibility = View.INVISIBLE
                itemRepositoryButtonDownload.visibility = View.VISIBLE
                itemRepositoryProgressDownload.visibility = View.INVISIBLE
            }
            if(repos.isDownloading){
                itemRepositoryProgressDownload.visibility = View.VISIBLE
                itemRepositoryIcSaved.visibility = View.INVISIBLE
                itemRepositoryButtonDownload.visibility = View.INVISIBLE
            }
            root.setOnClickListener {
                onReposClick(repos)
            }
            itemRepositoryButtonDownload.setOnClickListener {
                onReposButtonDownloadClick(repos, position)
            }
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int, payload: List<Any>) {
        val repos = _items[position]
        holder.binding.apply {
            if(payload.isNotEmpty()){
                payload.forEach {
                    when(it){
                        PAYLOAD_SUCCESS_DOWNLOADING -> {
                            itemRepositoryProgressDownload.visibility = View.INVISIBLE
                            itemRepositoryButtonDownload.visibility = View.INVISIBLE
                            itemRepositoryIcSaved.visibility = View.VISIBLE
                            repos.isDownloading = false
                            repos.isSaved = true
                        }
                        PAYLOAD_FAILED_DOWNLOADING -> {
                            itemRepositoryProgressDownload.visibility = View.INVISIBLE
                            itemRepositoryButtonDownload.visibility = View.VISIBLE
                            itemRepositoryIcSaved.visibility = View.INVISIBLE
                            repos.isDownloading = false
                            repos.isSaved = false
                        }
                        PAYLOAD_START_DOWNLOADING -> {
                            itemRepositoryProgressDownload.visibility = View.VISIBLE
                            itemRepositoryButtonDownload.visibility = View.INVISIBLE
                            itemRepositoryIcSaved.visibility = View.INVISIBLE
                            repos.isSaved = false
                            repos.isDownloading = true
                        }
                    }
                }
            }else super.onBindViewHolder(holder, position,payload)
        }
    }

    override fun getItemCount(): Int = _items.size

}