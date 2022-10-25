package ru.spiridonov.be.kind.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import ru.spiridonov.be.kind.R
import ru.spiridonov.be.kind.databinding.EachWorkItemBinding
import ru.spiridonov.be.kind.domain.entity.WorkItem

class WorkItemAdapter :
    ListAdapter<WorkItem, WorkItemViewHolder>(WorkItemDiffCallback) {
    var onWorkItemClickListener: ((WorkItem) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkItemViewHolder {
        val layoutID =
            when (viewType) {
                WORK_ITEM -> R.layout.each_work_item
                else -> throw RuntimeException("Unknown view type: $viewType")
            }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layoutID,
            parent,
            false
        )
        return WorkItemViewHolder(binding)
    }

    override fun getItemViewType(position: Int) =
        WORK_ITEM


    override fun onBindViewHolder(holder: WorkItemViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            when (this) {
                is EachWorkItemBinding ->
                    workItem = item

            }
            root.setOnClickListener {
                onWorkItemClickListener?.invoke(item)
            }
        }
    }

    companion object {
        private const val WORK_ITEM = 0
    }
}