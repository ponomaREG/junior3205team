package com.team3205.junior.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.team3205.junior.R
import com.team3205.junior.data.repository.entities.RecentSearch
import com.team3205.junior.databinding.ItemRecentSearchBinding

/**
 *  Адаптер истории поиска
 *  @param onRecentSearchClick - колбек нажатия на историю поиска
 */
class RecentSearchesAdapter(
    private val onRecentSearchClick:(RecentSearch) -> Unit = {}
): RecyclerView.Adapter<RecentSearchesAdapter.Holder>() {

    private val _items: MutableList<RecentSearch> = mutableListOf()

    /**
     * Очистка элементов
     */
    fun clearAll() {
        _items.clear()
        notifyDataSetChanged()
    }

    /**
     * Добавление элементов
     */
    fun addItems(items: List<RecentSearch>) {
        _items.addAll(0,items)
        notifyItemRangeInserted(0,items.size)
    }


    class Holder(val binding: ItemRecentSearchBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_recent_search,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = _items[position]
        holder.binding.apply {
            itemRecentSearchText.text = item.search
            root.setOnClickListener {
                onRecentSearchClick(item)
            }
        }
    }

    override fun getItemCount(): Int = _items.size

}